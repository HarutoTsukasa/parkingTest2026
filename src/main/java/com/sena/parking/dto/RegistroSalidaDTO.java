package com.sena.parking.dto;

import lombok.Data;

@Data
public class RegistroSalidaDTO {
	private Long idRegistro;
	private String placa;
	private Double valorCobrado;
	private long horasEstadia;

}
