package proyecto.controller;

import java.util.List;
import java.util.Optional;

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

import io.swagger.v3.oas.annotations.Operation;
import proyecto.entity.Chef;
import proyecto.service.ChefService;

@RestController
@RequestMapping("/chef")
public class ChefController {

	@Autowired
	private ChefService chefService;

	@GetMapping("/{id}")
	@Operation(summary = "Obtener datos de un chef concreto")
	public ResponseEntity<Chef> findById(@PathVariable int id) {
		Optional<Chef> chef = chefService.findById(id);

		if (chef.isPresent()) {
			return ResponseEntity.ok(chef.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping()
	@Operation(summary = "Obtener listado de chefs")
	public ResponseEntity<List<Chef>> findAll() {
		List<Chef> chefs = chefService.findAll();
		return ResponseEntity.ok(chefs);
	}
	
	@GetMapping("/miPerfil")
	@Operation(summary = "Obtener datos del chef logueado")
	public ResponseEntity<Chef> getChefLogueado() {
		Chef chef = chefService.getChefLogueado();
		return ResponseEntity.ok(chef);
	}

	@GetMapping("/listadoActivos")
	@Operation(summary = "Obtener listado de chefs en estado activo")
	public ResponseEntity<List<Chef>> getListChefActivos() {
		List<Chef> chefs = chefService.getListChefActivos();
		return ResponseEntity.ok(chefs);
	}

	@PostMapping
	@Operation(summary = "Crear un nuevo chef")
	public ResponseEntity<String> saveChef(@RequestBody Chef newChef) {
		System.out.println("🟢 3. ENTRANDO EN EL CONTROLLER"); // <--- AÑADE ESTO
		Chef savedChef = chefService.save(newChef);

		if (savedChef != null) {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body("Chef creado exitosamente con ID: " + savedChef.getId());
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el chef");
		}
	}

	@PutMapping
	@Operation(summary = "Actualizar un chef logueado")
	public ResponseEntity<String> updateChef(@RequestBody Chef updatedChef) {
		Chef response = chefService.update(updatedChef);

		if (response != null) {
			return ResponseEntity.ok("Chef actualizado exitosamente");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chef no encontrado");
		}
	}

	@DeleteMapping
	@Operation(summary = "Eliminar un chef logueado (Anonimización de datos)")
	public ResponseEntity<String> deleteChef() {
		boolean result = chefService.deleteAndAnonymize();

		if (result) {
			return ResponseEntity.ok("Chef eliminado (datos anonimizados) exitosamente");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chef no encontrado");
		}
	}

	@Operation(summary = "Activar al chef para ofrecer servicios")
	@PutMapping("/activar")
	public ResponseEntity<String> activarChef() {
		chefService.activarseParaServicio();
		return ResponseEntity.ok("Chef activado para ofrecer servicios.");
	}

	@Operation(summary = "Desactivar al chef para ofrecer servicios")
	@PutMapping("/desactivar")
	public ResponseEntity<String> desactivarChef() {
		chefService.desactivarseParaServicio();
		return ResponseEntity.ok("Chef desactivado para ofrecer servicios.");
	}

}