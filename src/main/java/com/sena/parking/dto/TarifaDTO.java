package com.sena.parking.dto;

import com.sena.parking.model.TipoVehiculo;

import lombok.Data;

@Data
public class TarifaDTO {
	private Long idTarifa;
	private TipoVehiculo tipoVehiculo;
	private Double tarifaPorHora;
	private Double tarifaPorDia;

	public Long getIdTarifa() {
		return idTarifa;
	}

	public void setIdTarifa(Long idTarifa) {
		this.idTarifa = idTarifa;
	}

	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public Double getTarifaPorHora() {
		return tarifaPorHora;
	}

	public void setTarifaPorHora(Double tarifaPorHora) {
		this.tarifaPorHora = tarifaPorHora;
	}

	public Double getTarifaPorDia() {
		return tarifaPorDia;
	}

	public void setTarifaPorDia(Double tarifaPorDia) {
		this.tarifaPorDia = tarifaPorDia;
	}

}
