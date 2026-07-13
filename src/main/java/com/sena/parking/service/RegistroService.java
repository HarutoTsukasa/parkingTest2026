package com.sena.parking.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sena.parking.dto.RegistroEntradaDTO;
import com.sena.parking.dto.RegistroSalidaDTO;
import com.sena.parking.exception.BusinessRuleException;
import com.sena.parking.exception.ResourceNotFoundException;
import com.sena.parking.model.Registro;
import com.sena.parking.model.Tarifa;
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

	public List<Registro> obtenerRegistrosActivos() {
		return registroRepository.findByActivoTrue();
	}

	public List<Registro> obtenerTodos() {
		return registroRepository.findAll();
	}

	@Transactional
	public Registro registrarEntrada(RegistroEntradaDTO entradaDTO) {
		String placaNormalizada = normalizarPlaca(entradaDTO.getPlaca());

		// findByPlacaForUpdate toma un lock pesimista (SELECT ... FOR UPDATE)
		// sobre la fila del vehículo. Mientras esta transacción no termine,
		// cualquier otra transacción que intente lo mismo con la misma placa
		// queda bloqueada esperando, en vez de leer un estado "no hay entrada
		// activa" que ya dejó de ser cierto.
		Vehiculo vehiculo = vehiculoRepository.findByPlacaForUpdate(placaNormalizada)
				.orElseThrow(() -> new ResourceNotFoundException("Vehículo no registrado: " + placaNormalizada));

		if (registroRepository.findByVehiculoPlacaAndActivoTrue(vehiculo.getPlaca()).isPresent()) {
			throw new BusinessRuleException("El vehículo ya se encuentra en el parqueadero");
		}

		Registro registro = new Registro();
		registro.setVehiculo(vehiculo);
		registro.setFechaHoraIngreso(LocalDateTime.now());
		registro.setActivo(true);
		return registroRepository.save(registro);
	}

	@Transactional
	public RegistroSalidaDTO registrarSalida(String placa) {
		String placaNormalizada = normalizarPlaca(placa);

		Registro registro = registroRepository.findByVehiculoPlacaAndActivoTrue(placaNormalizada).orElseThrow(
				() -> new ResourceNotFoundException("No hay registro activo para la placa " + placaNormalizada));

		registro.setFechaHoraSalida(LocalDateTime.now());

		long horas = calcularHorasEstadia(registro.getFechaHoraIngreso(), registro.getFechaHoraSalida());
		double costo = calcularCosto(registro.getVehiculo(), horas);

		registro.setValorPagado(costo);
		registro.setActivo(false);
		registro = registroRepository.save(registro);

		RegistroSalidaDTO salidaDTO = new RegistroSalidaDTO();
		salidaDTO.setIdRegistro(registro.getIdRegistro()); // antes quedaba siempre null
		salidaDTO.setPlaca(placaNormalizada);
		salidaDTO.setValorCobrado(costo);
		salidaDTO.setHorasEstadia(horas);

		return salidaDTO;
	}

	// Antes esta cuenta se hacía dos veces (una en calcularCosto, otra en
	// registrarSalida) con el riesgo de que diverjan si alguien tocaba una
	// sin tocar la otra. Ahora hay una sola fuente de verdad para las horas.
	private long calcularHorasEstadia(LocalDateTime entrada, LocalDateTime salida) {
		long horas = Duration.between(entrada, salida).toHours();
		return horas == 0 ? 1 : horas; // mínimo una hora
	}

	private double calcularCosto(Vehiculo vehiculo, long horas) {
		Tarifa tarifa = tarifaRepository.findByTipoVehiculo(vehiculo.getTipo())
				.orElseThrow(() -> new ResourceNotFoundException("Tarifa no configurada para " + vehiculo.getTipo()));

		long dias = horas / 24;
		long horasRestantes = horas % 24;
		return dias * tarifa.getTarifaPorDia() + horasRestantes * tarifa.getTarifaPorHora();
	}

	private String normalizarPlaca(String placa) {
		if (placa == null || placa.isBlank()) {
			throw new BusinessRuleException("La placa no puede estar vacía");
		}
		return placa.trim().toUpperCase();
	}

}
