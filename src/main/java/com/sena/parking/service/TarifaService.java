package com.sena.parking.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.parking.dto.TarifaDTO;
import com.sena.parking.model.Tarifa;
import com.sena.parking.model.TipoVehiculo;
import com.sena.parking.repository.IRegistroRepository;
import com.sena.parking.repository.ITarifaRepository;

@Service
public class TarifaService {

	@Autowired
	private ITarifaRepository tarifaRepository;

	@Autowired
	private IRegistroRepository registroRepository;

	public List<TarifaDTO> listarTarifas() {
		return tarifaRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
	}

	public TarifaDTO obtenerPorId(Long id) {
		Tarifa tarifa = tarifaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Tarifa no encontrada con id: " + id));
		return convertirADTO(tarifa);
	}

	public TarifaDTO obtenerPorTipo(TipoVehiculo tipo) {
		Tarifa tarifa = tarifaRepository.findByTipoVehiculo(tipo)
				.orElseThrow(() -> new RuntimeException("Tarifa no configurada para " + tipo));
		return convertirADTO(tarifa);
	}

	public TarifaDTO crearTarifa(TarifaDTO dto) {
		// Validar que no exista ya una tarifa para ese tipo
		if (tarifaRepository.findByTipoVehiculo(dto.getTipoVehiculo()).isPresent()) {
			throw new RuntimeException("Ya existe una tarifa para el tipo: " + dto.getTipoVehiculo());
		}

		Tarifa tarifa = new Tarifa();
		tarifa.setTipoVehiculo(dto.getTipoVehiculo());
		tarifa.setTarifaPorHora(dto.getTarifaPorHora());
		tarifa.setTarifaPorDia(dto.getTarifaPorDia());

		tarifa = tarifaRepository.save(tarifa);
		return convertirADTO(tarifa);
	}

	public TarifaDTO actualizarTarifa(Long id, TarifaDTO dto) {
		Tarifa tarifa = tarifaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Tarifa no encontrada con id: " + id));

		// Si el tipo cambia, verificar que no exista otra con ese tipo
		if (tarifa.getTipoVehiculo() != dto.getTipoVehiculo()
				&& tarifaRepository.findByTipoVehiculo(dto.getTipoVehiculo()).isPresent()) {
			throw new RuntimeException("Ya existe una tarifa para el tipo: " + dto.getTipoVehiculo());
		}

		tarifa.setTipoVehiculo(dto.getTipoVehiculo());
		tarifa.setTarifaPorHora(dto.getTarifaPorHora());
		tarifa.setTarifaPorDia(dto.getTarifaPorDia());

		tarifa = tarifaRepository.save(tarifa);
		return convertirADTO(tarifa);
	}

	public void eliminarTarifa(Long id) {
		Tarifa tarifa = tarifaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Tarifa no encontrada con id: " + id));
		// Verificar si hay algún registro activo o histórico con vehículos de ese tipo
		if (registroRepository.existsByVehiculoTipo(tarifa.getTipoVehiculo())) {
			throw new RuntimeException(
					"No se puede eliminar la tarifa porque hay vehículos de ese tipo con registros.");
		}
		tarifaRepository.deleteById(id);
	}

	private TarifaDTO convertirADTO(Tarifa t) {
		TarifaDTO dto = new TarifaDTO();
		dto.setIdTarifa(t.getIdTarifa());
		dto.setTipoVehiculo(t.getTipoVehiculo());
		dto.setTarifaPorHora(t.getTarifaPorHora());
		dto.setTarifaPorDia(t.getTarifaPorDia());
		return dto;
	}
}
