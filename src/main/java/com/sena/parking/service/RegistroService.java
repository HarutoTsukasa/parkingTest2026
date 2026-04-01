package com.sena.parking.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.parking.dto.RegistroEntradaDTO;
import com.sena.parking.dto.RegistroSalidaDTO;
import com.sena.parking.model.Registro;
import com.sena.parking.model.Tarifa;
import com.sena.parking.model.Vehiculo;
import com.sena.parking.repository.IRegistroRepository;
import com.sena.parking.repository.ITarifaRepository;
import com.sena.parking.repository.IVehiculoRepository;

import jakarta.transaction.Transactional;

@Service
public class RegistroService {

	@Autowired
	private IRegistroRepository registroRepository;

	@Autowired
	private IVehiculoRepository vehiculoRepository;

	@Autowired
	private ITarifaRepository tarifaRepository;

	public List<Registro> obtenerRegistrosActivos() {
		return registroRepository.findByActivoTrue();
	}

	public List<Registro> obtenerTodos() {
		return registroRepository.findAll();
	}

	@Transactional
	public Registro registrarEntrada(RegistroEntradaDTO entradaDTO) {
		Vehiculo vehiculo = vehiculoRepository.findByPlaca(entradaDTO.getPlaca())
				.orElseThrow(() -> new RuntimeException("Vehículo no registrado"));

		// Verificar que no tenga un registro activo
		if (registroRepository.findByVehiculoPlacaAndActivoTrue(vehiculo.getPlaca()).isPresent()) {
			throw new RuntimeException("El vehículo ya se encuentra en el parqueadero");
		}

		Registro registro = new Registro();
		registro.setVehiculo(vehiculo);
		registro.setActivo(true);
		return registroRepository.save(registro);
	}

	@Transactional
	public RegistroSalidaDTO registrarSalida(String placa) {
		Registro registro = registroRepository.findByVehiculoPlacaAndActivoTrue(placa)
				.orElseThrow(() -> new RuntimeException("No hay registro activo para la placa " + placa));

		registro.setFechaHoraSalida(LocalDateTime.now());

		double costo = calcularCosto(registro);
		registro.setValorPagado(costo);
		registro.setActivo(false);

		registro = registroRepository.save(registro);

		RegistroSalidaDTO salidaDTO = new RegistroSalidaDTO();
		salidaDTO.setPlaca(placa);
		salidaDTO.setValorCobrado(costo);

		long horas = Duration.between(registro.getFechaHoraIngreso(), registro.getFechaHoraSalida()).toHours();
		if (horas == 0)
			horas = 1;
		salidaDTO.setHorasEstadia(horas);

		return salidaDTO;
	}

	private double calcularCosto(Registro registro) {
		LocalDateTime entrada = registro.getFechaHoraIngreso();
		LocalDateTime salida = registro.getFechaHoraSalida() != null ? registro.getFechaHoraSalida()
				: LocalDateTime.now();

		long horas = Duration.between(entrada, salida).toHours();
		if (horas == 0)
			horas = 1; // mínimo una hora

		Tarifa tarifa = tarifaRepository.findByTipoVehiculo(registro.getVehiculo().getTipo()).orElseThrow(
				() -> new RuntimeException("Tarifa no configurada para " + registro.getVehiculo().getTipo()));

		// Cálculo con días completos + horas restantes
		long dias = horas / 24;
		long horasRestantes = horas % 24;
		double costo = dias * tarifa.getTarifaPorDia() + horasRestantes * tarifa.getTarifaPorHora();

		return costo;
	}

}
