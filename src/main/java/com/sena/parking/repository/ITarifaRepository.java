package com.sena.parking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sena.parking.model.Tarifa;
import com.sena.parking.model.TipoVehiculo;

@Repository
public interface ITarifaRepository extends JpaRepository<Tarifa, Long> {

	Optional<Tarifa> findByTipoVehiculo(TipoVehiculo tipoVehiculo);

}
