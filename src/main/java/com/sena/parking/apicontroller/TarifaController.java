package com.sena.parking.apicontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.parking.dto.TarifaDTO;
import com.sena.parking.model.TipoVehiculo;
import com.sena.parking.service.TarifaService;

@RestController
@RequestMapping("/api/tarifas")
public class TarifaController {

	@Autowired
	private TarifaService tarifaService;

	@GetMapping
	public ResponseEntity<List<TarifaDTO>> listarTarifas() {
		List<TarifaDTO> tarifas = tarifaService.listarTarifas();
		if (tarifas.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(tarifas);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TarifaDTO> obtenerPorId(@PathVariable Long id) {
		TarifaDTO tarifa = tarifaService.obtenerPorId(id);
		return ResponseEntity.ok(tarifa);
	}

	@GetMapping("/tipo/{tipo}")
	public ResponseEntity<TarifaDTO> obtenerPorTipo(@PathVariable String tipo) {
		// Convertir String a enum (manejar excepción si es inválido)
		try {
			TipoVehiculo tipoEnum = TipoVehiculo.valueOf(tipo.toUpperCase());
			TarifaDTO tarifa = tarifaService.obtenerPorTipo(tipoEnum);
			return ResponseEntity.ok(tarifa);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Tipo de vehículo inválido: " + tipo);
		}
	}

	@PostMapping
	public ResponseEntity<TarifaDTO> crearTarifa(@RequestBody TarifaDTO tarifaDTO) {
		TarifaDTO nueva = tarifaService.crearTarifa(tarifaDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TarifaDTO> actualizarTarifa(@PathVariable Long id, @RequestBody TarifaDTO tarifaDTO) {
		TarifaDTO actualizada = tarifaService.actualizarTarifa(id, tarifaDTO);
		return ResponseEntity.ok(actualizada);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarTarifa(@PathVariable Long id) {
		tarifaService.eliminarTarifa(id);
		return ResponseEntity.noContent().build();
	}

}
