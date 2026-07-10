package com.sena.parking.dto;

import com.sena.parking.model.TipoVehiculo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TarifaDTO {

	private Long idTarifa;

	@NotNull(message = "El tipo de vehículo es obligatorio")
	private TipoVehiculo tipoVehiculo;

	@NotNull(message = "La tarifa por hora es obligatoria")
	@Positive(message = "La tarifa por hora debe ser mayor a cero")
	private Double tarifaPorHora;

	@NotNull(message = "La tarifa por día es obligatoria")
	@Positive(message = "La tarifa por día debe ser mayor a cero")
	private Double tarifaPorDia;

}
