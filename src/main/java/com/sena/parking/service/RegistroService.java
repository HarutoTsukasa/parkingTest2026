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
import com.sena.parking.model.TipoVehiculo;
import com.sena.parking.model.Vehiculo;
import com.sena.parking.repository.IRegistroRepository;
import com.sena.parking.repository.ITarifaRepository;
import com.sena.parking.repository.IVehiculoRepository;

@Service
public class RegistroService {

	@Autowired
	private IRegistroRepository registroRepository;

	@Autowired
	private IVehiculoRepository vehiculoRepository;

	@Autowired
	private ITarifaRepository tarifaRepository;

	public Registro registrarEntrada(RegistroEntradaDTO entradaDTO) {
		Vehiculo vehiculo = vehiculoRepository.findByPlaca(entradaDTO.getPlaca())
				.orElseThrow(() -> new RuntimeException("Vehículo no registrado"));

		// Verificar si ya tiene un registro activo
		if (registroRepository.findByVehiculoPlacaAndActivoTrue(entradaDTO.getPlaca()).isPresent()) {
			throw new RuntimeException("El vehículo ya se encuentra dentro del parqueadero");
		}

		Registro registro = new Registro();
		registro.setVehiculo(vehiculo);
		registro.setActivo(true);
		registro.setFechaHoraIngreso(LocalDateTime.now());
		return registroRepository.save(registro);
	}

	public RegistroSalidaDTO registrarSalida(String placa) {
		Registro registro = registroRepository.findByVehiculoPlacaAndActivoTrue(placa)
				.orElseThrow(() -> new RuntimeException("No hay registro activo para el vehículo con placa: " + placa));

		registro.setFechaHoraSalida(LocalDateTime.now());
		double costo = calcularCosto(registro);
		registro.setValorPagado(costo);
		registro.setActivo(false);
		registroRepository.save(registro);

		RegistroSalidaDTO salidaDTO = new RegistroSalidaDTO();
		salidaDTO.setIdRegistro(registro.getIdRegistro());
		salidaDTO.setPlaca(placa);
		salidaDTO.setValorCobrado(costo);
		return salidaDTO;
	}

	private double calcularCosto(Registro registro) {
		LocalDateTime entrada = registro.getFechaHoraIngreso();
		LocalDateTime salida = registro.getFechaHoraSalida() != null ? registro.getFechaHoraSalida()
				: LocalDateTime.now();

		long horas = Duration.between(entrada, salida).toHours();
		if (horas == 0)
			horas = 1; // mínimo una hora

		TipoVehiculo tipo = registro.getVehiculo().getTipo();
		Tarifa tarifa = tarifaRepository.findByTipoVehiculo(tipo)
				.orElseThrow(() -> new RuntimeException("Tarifa no configurada para " + tipo));

		if (horas >= 24) {
			long dias = horas / 24;
			return dias * tarifa.getTarifaPorDia();
		} else {
			return horas * tarifa.getTarifaPorHora();
		}
	}

	public List<Registro> obtenerRegistrosActivos() {
		return registroRepository.findByActivoTrue();
	}

}
