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

import com.sena.parking.dto.VehiculoDTO;
import com.sena.parking.service.VehiculoService;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

	@Autowired
	private VehiculoService vehiculoService;

	@GetMapping
	public ResponseEntity<List<VehiculoDTO>> listarTodos() {
		List<VehiculoDTO> vehiculos = vehiculoService.listarTodos();
		if (vehiculos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(vehiculos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<VehiculoDTO> obtenerPorId(@PathVariable Long id) {
		VehiculoDTO vehiculo = vehiculoService.obtenerPorId(id);
		return ResponseEntity.ok(vehiculo);
	}

	@GetMapping("/placa/{placa}")
	public ResponseEntity<VehiculoDTO> obtenerPorPlaca(@PathVariable String placa) {
		VehiculoDTO vehiculo = vehiculoService.obtenerPorPlaca(placa);
		return ResponseEntity.ok(vehiculo);
	}

	@PostMapping
	public ResponseEntity<VehiculoDTO> crearVehiculo(@RequestBody VehiculoDTO vehiculoDTO) {
		VehiculoDTO nuevo = vehiculoService.registrarVehiculo(vehiculoDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
	}

	@PutMapping("/{id}")
	public ResponseEntity<VehiculoDTO> actualizarVehiculo(@PathVariable Long id, @RequestBody VehiculoDTO vehiculoDTO) {
		VehiculoDTO actualizado = vehiculoService.actualizarVehiculo(id, vehiculoDTO);
		return ResponseEntity.ok(actualizado);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarVehiculo(@PathVariable Long id) {
		vehiculoService.eliminarVehiculo(id);
		return ResponseEntity.noContent().build();
	}

}
