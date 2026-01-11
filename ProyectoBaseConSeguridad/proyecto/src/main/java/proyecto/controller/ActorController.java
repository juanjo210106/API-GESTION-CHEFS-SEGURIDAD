package proyecto.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import proyecto.entity.Actor;
import proyecto.entity.ActorLogin;
import proyecto.entity.Chef;
import proyecto.entity.Cliente;
import proyecto.entity.Rol;
import proyecto.security.JWTUtils;
import proyecto.service.ActorService;
import proyecto.service.ChefService;
import proyecto.service.ClienteService;

@RestController
public class ActorController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtils JWTUtils;

	@Autowired
	private ActorService actorService;

	@Autowired
	private ChefService chefService;

	@Autowired
	private ClienteService clienteService;

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody ActorLogin actorLogin) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(actorLogin.getUsername(), actorLogin.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = JWTUtils.generateToken(authentication);
		Map<String, String> response = new HashMap<>();
		response.put("token", token);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Banear a un actor (usuario o chef) por ID")
	@PutMapping("/banear/{actorId}")
	public ResponseEntity<String> banearChef(@PathVariable int actorId) {
		Optional<Actor> actorO = actorService.findById(actorId);
		if (actorO.isPresent() && actorO.get().getRol() != Rol.ADMIN) {
			if (actorO.get().getRol() == Rol.CHEF) {
				Chef c = chefService.findById(actorO.get().getId()).get();
				c.setBaneado(true);
				c.setActivo(false);
				chefService.saveBasico(c);
				return ResponseEntity.ok("Chef con ID " + actorId + " ha sido baneado.");
			} else {
				Cliente cl = clienteService.findById(actorO.get().getId()).get();
				cl.setBaneado(true);
				clienteService.saveBasico(cl);
				return ResponseEntity.ok("Cliente con ID " + actorId + " ha sido baneado.");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró un chef con ID " + actorId);
		}
	}

	@Operation(summary = "Desbanear a un actor (usuario o chef) por ID")
	@PutMapping("/desbanear/{actorId}")
	public ResponseEntity<String> desbanearChef(@PathVariable int actorId) {
		Optional<Actor> actorO = actorService.findById(actorId);
		if (actorO.isPresent() && actorO.get().getRol() != Rol.ADMIN) {
			actorO.get().setBaneado(false);
			actorService.saveBasico(actorO.get());
			return ResponseEntity.ok("Chef con ID " + actorId + " ha sido baneado.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró un chef con ID " + actorId);
		}
	}
}