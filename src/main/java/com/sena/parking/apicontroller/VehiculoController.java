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

import com.sena.parking.dto.VehiculoDTO;
import com.sena.parking.service.VehiculoService;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

	@Autowired
	private VehiculoService vehiculoService;

	@GetMapping
	public List<VehiculoDTO> listarTodos() {
		return vehiculoService.findAll();
	}

	@GetMapping("/{placa}")
	public ResponseEntity<VehiculoDTO> buscarPorPlaca(@PathVariable String placa) {
		VehiculoDTO vehiculo = vehiculoService.findByPlaca(placa);
		return ResponseEntity.ok(vehiculo);
	}

	@PostMapping
	public ResponseEntity<VehiculoDTO> crear(@RequestBody VehiculoDTO vehiculoDTO) {
		VehiculoDTO nuevo = vehiculoService.save(vehiculoDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		vehiculoService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
