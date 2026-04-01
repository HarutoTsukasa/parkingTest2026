package com.sena.parking.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.parking.dto.VehiculoDTO;
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
				.orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));
		return convertirADTO(vehiculo);
	}

	public VehiculoDTO obtenerPorPlaca(String placa) {
		Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa)
				.orElseThrow(() -> new RuntimeException("Vehículo no encontrado con placa: " + placa));
		return convertirADTO(vehiculo);
	}

	public VehiculoDTO registrarVehiculo(VehiculoDTO dto) {
		// Validar que no exista ya una placa igual
		if (vehiculoRepository.findByPlaca(dto.getPlaca()).isPresent()) {
			throw new RuntimeException("Ya existe un vehículo con la placa: " + dto.getPlaca());
		}

		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setPlaca(dto.getPlaca());
		vehiculo.setTipo(dto.getTipo());
		vehiculo.setMarca(dto.getMarca());
		vehiculo.setModelo(dto.getModelo());

		vehiculo = vehiculoRepository.save(vehiculo);
		return convertirADTO(vehiculo);
	}

	public VehiculoDTO actualizarVehiculo(Long id, VehiculoDTO dto) {
		Vehiculo vehiculo = vehiculoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));

		// Si la placa cambia, verificar que no exista otra
		if (!vehiculo.getPlaca().equals(dto.getPlaca()) && vehiculoRepository.findByPlaca(dto.getPlaca()).isPresent()) {
			throw new RuntimeException("Ya existe un vehículo con la placa: " + dto.getPlaca());
		}

		vehiculo.setPlaca(dto.getPlaca());
		vehiculo.setTipo(dto.getTipo());
		vehiculo.setMarca(dto.getMarca());
		vehiculo.setModelo(dto.getModelo());

		vehiculo = vehiculoRepository.save(vehiculo);
		return convertirADTO(vehiculo);
	}

	public void eliminarVehiculo(Long id) {
		if (!vehiculoRepository.existsById(id)) {
			throw new RuntimeException("Vehículo no encontrado con id: " + id);
		}
		if (registroRepository.existsByVehiculoId(id)) {
			throw new RuntimeException("No se puede eliminar el vehículo porque tiene registros asociados.");
		}
		vehiculoRepository.deleteById(id);
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
