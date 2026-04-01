package com.sena.parking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sena.parking.model.Vehiculo;

@Repository
public interface IVehiculoRepository extends JpaRepository<Vehiculo, Long> {

	Optional<Vehiculo> findByPlaca(String placa);

	boolean existsByPlaca(String placa);

}
