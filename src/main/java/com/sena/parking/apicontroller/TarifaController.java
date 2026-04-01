package com.sena.parking.apicontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	public List<TarifaDTO> listarTodos() {
		return tarifaService.findAll();
	}

	@GetMapping("/{tipo}")
	public ResponseEntity<TarifaDTO> buscarPorTipo(@PathVariable TipoVehiculo tipo) {
		TarifaDTO tarifa = tarifaService.findByTipo(tipo);
		return ResponseEntity.ok(tarifa);
	}

	@PostMapping
	public ResponseEntity<TarifaDTO> crear(@RequestBody TarifaDTO tarifaDTO) {
		TarifaDTO nueva = tarifaService.save(tarifaDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		tarifaService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
