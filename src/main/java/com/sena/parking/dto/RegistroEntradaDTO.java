package com.sena.parking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistroEntradaDTO {

	@NotBlank(message = "La placa es obligatoria")
	private String placa;

}
