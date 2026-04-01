package com.sena.parking.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.parking.dto.VehiculoDTO;
import com.sena.parking.model.Vehiculo;
import com.sena.parking.repository.IVehiculoRepository;

@Service
public class VehiculoService {

	@Autowired
	private IVehiculoRepository vehiculoRepository;

	public List<VehiculoDTO> findAll() {
		return vehiculoRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public VehiculoDTO findByPlaca(String placa) {
		Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa)
				.orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
		return convertToDTO(vehiculo);
	}

	public VehiculoDTO save(VehiculoDTO vehiculoDTO) {
		if (vehiculoRepository.existsByPlaca(vehiculoDTO.getPlaca())) {
			throw new RuntimeException("Ya existe un vehículo con esa placa");
		}
		Vehiculo vehiculo = convertToEntity(vehiculoDTO);
		vehiculo = vehiculoRepository.save(vehiculo);
		return convertToDTO(vehiculo);
	}

	public void delete(Long id) {
		vehiculoRepository.deleteById(id);
	}

	private VehiculoDTO convertToDTO(Vehiculo vehiculo) {
		VehiculoDTO dto = new VehiculoDTO();
		dto.setIdVehiculo(vehiculo.getIdVehiculo());
		dto.setPlaca(vehiculo.getPlaca());
		dto.setTipo(vehiculo.getTipo());
		dto.setMarca(vehiculo.getMarca());
		dto.setModelo(vehiculo.getModelo());
		return dto;
	}

	private Vehiculo convertToEntity(VehiculoDTO dto) {
		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setPlaca(dto.getPlaca());
		vehiculo.setTipo(dto.getTipo());
		vehiculo.setMarca(dto.getMarca());
		vehiculo.setModelo(dto.getModelo());
		return vehiculo;
	}

}
