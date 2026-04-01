package com.sena.parking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sena.parking.model.Registro;

@Repository
public interface IRegistroRepository extends JpaRepository<Registro, Long> {

	Optional<Registro> findByVehiculoPlacaAndActivoTrue(String placa);

	List<Registro> findByActivoTrue();

}
