package com.sena.parking.dto;

import lombok.Data;

@Data
public class RegistroEntradaDTO {

	private String placa;

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

}
