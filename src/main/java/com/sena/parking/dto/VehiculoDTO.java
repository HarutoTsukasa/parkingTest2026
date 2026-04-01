package com.sena.parking.dto;

import com.sena.parking.model.TipoVehiculo;

import lombok.Data;

@Data
public class VehiculoDTO {
	private Long idVehiculo;
	private String placa;
	private TipoVehiculo tipo;
	private String marca;
	private String modelo;

}
