package com.sena.parking.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.parking.dto.TarifaDTO;
import com.sena.parking.model.Tarifa;
import com.sena.parking.model.TipoVehiculo;
import com.sena.parking.repository.ITarifaRepository;

@Service
public class TarifaService {

	@Autowired
	private ITarifaRepository tarifaRepository;

	public List<TarifaDTO> findAll() {
		return tarifaRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public TarifaDTO findByTipo(TipoVehiculo tipo) {
		Tarifa tarifa = tarifaRepository.findByTipoVehiculo(tipo)
				.orElseThrow(() -> new RuntimeException("Tarifa no encontrada para el tipo: " + tipo));
		return convertToDTO(tarifa);
	}

	public TarifaDTO save(TarifaDTO tarifaDTO) {
		Tarifa tarifa = convertToEntity(tarifaDTO);
		tarifa = tarifaRepository.save(tarifa);
		return convertToDTO(tarifa);
	}

	public void delete(Long id) {
		tarifaRepository.deleteById(id);
	}

	private TarifaDTO convertToDTO(Tarifa tarifa) {
		TarifaDTO dto = new TarifaDTO();
		dto.setIdTarifa(tarifa.getIdTarifa());
		dto.setTipoVehiculo(tarifa.getTipoVehiculo());
		dto.setTarifaPorHora(tarifa.getTarifaPorHora());
		dto.setTarifaPorDia(tarifa.getTarifaPorDia());
		return dto;
	}

	private Tarifa convertToEntity(TarifaDTO dto) {
		Tarifa tarifa = new Tarifa();
		tarifa.setTipoVehiculo(dto.getTipoVehiculo());
		tarifa.setTarifaPorHora(dto.getTarifaPorHora());
		tarifa.setTarifaPorDia(dto.getTarifaPorDia());
		return tarifa;
	}
}
