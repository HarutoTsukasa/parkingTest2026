package com.sena.parking.apicontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.parking.dto.RegistroEntradaDTO;
import com.sena.parking.dto.RegistroSalidaDTO;
import com.sena.parking.model.Registro;
import com.sena.parking.service.RegistroService;

@RestController
@RequestMapping("/api/registros")
public class RegistroController {

	@Autowired
	private RegistroService registroService;

	@GetMapping("/activos")
	public ResponseEntity<List<Registro>> obtenerActivos() {
		List<Registro> activos = registroService.obtenerRegistrosActivos();
		if (activos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(activos);
	}

	@GetMapping
	public ResponseEntity<List<Registro>> obtenerTodos() {
		List<Registro> todos = registroService.obtenerTodos();
		if (todos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(todos);
	}

	@PostMapping("/entrada")
	public ResponseEntity<Registro> registrarEntrada(@RequestBody RegistroEntradaDTO entradaDTO) {
		Registro registro = registroService.registrarEntrada(entradaDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(registro);
	}

	@PutMapping("/salida/{placa}")
	public ResponseEntity<RegistroSalidaDTO> registrarSalida(@PathVariable String placa) {
		RegistroSalidaDTO salida = registroService.registrarSalida(placa);
		return ResponseEntity.ok(salida);
	}

}
