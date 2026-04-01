package com.sena.parking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tarifas")
public class Tarifa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTarifa;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoVehiculo tipoVehiculo; // "AUTO" o "MOTO"

	@Column(nullable = false)
	private Double tarifaPorHora;

	@Column(nullable = false)
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
