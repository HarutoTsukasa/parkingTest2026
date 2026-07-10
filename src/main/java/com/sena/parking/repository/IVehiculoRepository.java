package com.sena.parking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sena.parking.model.Vehiculo;

import jakarta.persistence.LockModeType;

@Repository
public interface IVehiculoRepository extends JpaRepository<Vehiculo, Long> {

	Optional<Vehiculo> findByPlaca(String placa);

	boolean existsByPlaca(String placa);

	/**
	 * Bloquea la fila del vehículo (SELECT ... FOR UPDATE) durante la transacción.
	 * Se usa en RegistroService.registrarEntrada para que dos requests concurrentes
	 * con la misma placa no puedan pasar ambos la validación de "no tiene entrada
	 * activa" antes de que cualquiera haga commit.
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT v FROM Vehiculo v WHERE v.placa = :placa")
	Optional<Vehiculo> findByPlacaForUpdate(String placa);

}
