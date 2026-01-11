package proyecto.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import proyecto.entity.Servicio;
import proyecto.service.ServicioService;

@RestController
@RequestMapping("/servicio")
public class ServicioController {

	@Autowired
	private ServicioService servicioService;

	@Operation(summary = "Contratar un servicio")
	@PostMapping("/contratar/{chefId}")
	public ResponseEntity<String> contratarServicio(@PathVariable int chefId, @RequestBody Servicio servicio) {
		Servicio servicioContratado = servicioService.contratarServicio(servicio, chefId);

		if (servicioContratado != null) {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body("Servicio creado exitosamente con ID: " + servicioContratado.getId());
		} else {
			return ResponseEntity.badRequest()
					.body("No se pudo contratar el servicio. El chef ya tiene un servicio ese día o no existe.");
		}
	}

	@Operation(summary = "Puntuar un servicio")
	@GetMapping("/puntuar/{servicioId}/{puntuacion}")
	public ResponseEntity<String> puntuarServicio(@PathVariable int servicioId, @PathVariable int puntuacion) {
		Optional<Servicio> servicio = servicioService.findById(servicioId);

		if (servicio.isPresent()) {
			if (puntuacion > 0 && puntuacion < 5) {
				if (servicioService.puntuarServicio(servicio.get(), puntuacion)) {
					return ResponseEntity.status(HttpStatus.CREATED)
							.body("Servicio con ID: " + servicio.get().getId() + " puntuado correctamente");
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Se esta intentando puntuar un servicio no consumido");
				}
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("La puntuacion tiene que estar comprendidad entre 0 y 5");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@Operation(summary = "Obtener todos los servicios ofrecidos por el chef logueado")
	@GetMapping("/misServiciosChef")
	public ResponseEntity<List<Servicio>> getAllByChefLogin() {
		List<Servicio> servicios = servicioService.getAllByChefLogin();
		return ResponseEntity.ok(servicios);
	}
	
	@Operation(summary = "Obtener todos los servicios contratados por el cliente logueado")
	@GetMapping("/misServiciosCliente")
	public ResponseEntity<List<Servicio>> getAllByClienteLogin() {
		List<Servicio> servicios = servicioService.getAllByClienteLogin();
		return ResponseEntity.ok(servicios);
	}
}