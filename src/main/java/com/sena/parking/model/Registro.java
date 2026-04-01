package com.sena.parking.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "registros")
public class Registro {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idRegistro;
	@Column(nullable = false)
	private LocalDateTime fechaHoraIngreso;
	private LocalDateTime fechaHoraSalida;
	private Double valorPagado;
	@Column(nullable = false)
	private Boolean activo = true; // true = dentro del parqueadero

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idVehiculo", nullable = false)
	private Vehiculo vehiculo;

	public Long getIdRegistro() {
		return idRegistro;
	}

	public void setIdRegistro(Long idRegistro) {
		this.idRegistro = idRegistro;
	}

	public LocalDateTime getFechaHoraIngreso() {
		return fechaHoraIngreso;
	}

	public void setFechaHoraIngreso(LocalDateTime fechaHoraIngreso) {
		this.fechaHoraIngreso = fechaHoraIngreso;
	}

	public LocalDateTime getFechaHoraSalida() {
		return fechaHoraSalida;
	}

	public void setFechaHoraSalida(LocalDateTime fechaHoraSalida) {
		this.fechaHoraSalida = fechaHoraSalida;
	}

	public Double getValorPagado() {
		return valorPagado;
	}

	public void setValorPagado(Double valorPagado) {
		this.valorPagado = valorPagado;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

}
