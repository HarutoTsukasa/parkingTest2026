package com.sena.parking.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.parking.dto.VehiculoDTO;
import com.sena.parking.exception.BusinessRuleException;
import com.sena.parking.exception.DuplicateResourceException;
import com.sena.parking.exception.ResourceNotFoundException;
import com.sena.parking.model.Vehiculo;
import com.sena.parking.repository.IRegistroRepository;
import com.sena.parking.repository.IVehiculoRepository;

@Service
public class VehiculoService {

	@Autowired
	private IVehiculoRepository vehiculoRepository;

	@Autowired
	private IRegistroRepository registroRepository;

	public List<VehiculoDTO> listarTodos() {
		return vehiculoRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
	}

	public VehiculoDTO obtenerPorId(Long id) {
		Vehiculo vehiculo = vehiculoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con id: " + id));
		return convertirADTO(vehiculo);
	}

	public VehiculoDTO obtenerPorPlaca(String placa) {
		String placaNormalizada = normalizarPlaca(placa);
		Vehiculo vehiculo = vehiculoRepository.findByPlaca(placaNormalizada).orElseThrow(
				() -> new ResourceNotFoundException("Vehículo no encontrado con placa: " + placaNormalizada));
		return convertirADTO(vehiculo);
	}

	public VehiculoDTO registrarVehiculo(VehiculoDTO dto) {
		String placaNormalizada = normalizarPlaca(dto.getPlaca());

		if (vehiculoRepository.findByPlaca(placaNormalizada).isPresent()) {
			throw new DuplicateResourceException("Ya existe un vehículo con la placa: " + placaNormalizada);
		}

		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setPlaca(placaNormalizada);
		vehiculo.setTipo(dto.getTipo());
		vehiculo.setMarca(dto.getMarca());
		vehiculo.setModelo(dto.getModelo());

		vehiculo = vehiculoRepository.save(vehiculo);
		return convertirADTO(vehiculo);
	}

	public VehiculoDTO actualizarVehiculo(Long id, VehiculoDTO dto) {
		Vehiculo vehiculo = vehiculoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con id: " + id));

		String placaNormalizada = normalizarPlaca(dto.getPlaca());

		if (!vehiculo.getPlaca().equals(placaNormalizada)
				&& vehiculoRepository.findByPlaca(placaNormalizada).isPresent()) {
			throw new DuplicateResourceException("Ya existe un vehículo con la placa: " + placaNormalizada);
		}

		vehiculo.setPlaca(placaNormalizada);
		vehiculo.setTipo(dto.getTipo());
		vehiculo.setMarca(dto.getMarca());
		vehiculo.setModelo(dto.getModelo());

		vehiculo = vehiculoRepository.save(vehiculo);
		return convertirADTO(vehiculo);
	}

	public void eliminarVehiculo(Long id) {
		if (!vehiculoRepository.existsById(id)) {
			throw new ResourceNotFoundException("Vehículo no encontrado con id: " + id);
		}
		if (registroRepository.existsByVehiculoIdVehiculo(id)) {
			throw new BusinessRuleException("No se puede eliminar el vehículo porque tiene registros asociados.");
		}
		vehiculoRepository.deleteById(id);
	}

	// Antes "abc123", "ABC123" y " ABC123" eran placas distintas para el unique
	// constraint. Ahora todo entra y sale normalizado.
	private String normalizarPlaca(String placa) {
		if (placa == null || placa.isBlank()) {
			throw new BusinessRuleException("La placa no puede estar vacía");
		}
		return placa.trim().toUpperCase();
	}

	private VehiculoDTO convertirADTO(Vehiculo v) {
		VehiculoDTO dto = new VehiculoDTO();
		dto.setIdVehiculo(v.getIdVehiculo());
		dto.setPlaca(v.getPlaca());
		dto.setTipo(v.getTipo());
		dto.setMarca(v.getMarca());
		dto.setModelo(v.getModelo());
		return dto;
	}

}
