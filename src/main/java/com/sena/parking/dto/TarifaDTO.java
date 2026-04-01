package com.sena.parking.dto;

import com.sena.parking.model.TipoVehiculo;

import lombok.Data;

@Data
public class TarifaDTO {
	private Long idTarifa;
	private TipoVehiculo tipoVehiculo;
	private Double tarifaPorHora;
	private Double tarifaPorDia;
}
