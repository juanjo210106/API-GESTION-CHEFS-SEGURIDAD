package proyecto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import proyecto.entity.Chef;
import proyecto.entity.Rol;
import proyecto.entity.Servicio;
import proyecto.respository.ChefRepository;
import proyecto.security.JWTUtils;

@Service
public class ChefService {

	@Autowired
	private ChefRepository chefRepository;

	@Autowired
	private BCryptPasswordEncoder BCryptPasswordEncoder;

	@Autowired
	private JWTUtils JWTUtils;

	public Optional<Chef> findByUsername(String username) {
		return chefRepository.findByUsername(username);
	}
	
	public List<Chef> findAll() {
		return chefRepository.findAll();
	}
	
	
	public Chef findByServicioId (int idServicio) {
		return chefRepository.findByServicioId(idServicio);
	}

	public Optional<Chef> findById(int id) {
		return chefRepository.findById(id);
	}

	public Chef getChefLogueado() {
		Chef chefLogin = JWTUtils.userLogin();
		return chefLogin;
	}
	
	public List<Chef> getListChefActivos() {
		return chefRepository.getListChefActivos();
	}
	
	@Transactional
	public Chef save(Chef chef) {
		chef.setPassword(BCryptPasswordEncoder.encode(chef.getPassword()));
		chef.setRol(Rol.CHEF);
		chef.setBaneado(false);
		
		chef.setActivo(true);
		chef.setValoracionChef(0.0);
		chef.setServiciosOfrecidos(new ArrayList<Servicio>());
		return chefRepository.save(chef);
	}
	
	@Transactional
	public Chef saveBasico(Chef chef) {
		return chefRepository.save(chef);
	}

	@Transactional
	public Chef update(Chef chef) {
		Chef chefLogin = JWTUtils.userLogin();
		if (chefLogin == null) {
			return null;
		} else {
			chefLogin.setPassword(chef.getPassword());
			chefLogin.setNombre(chef.getNombre());
			chefLogin.setPrimerApellido(chef.getPrimerApellido());
			chefLogin.setSegundoApellido(chef.getSegundoApellido());
			chefLogin.setEmail(chef.getEmail());
			chefLogin.setTelefono(chef.getTelefono());
			return chefRepository.save(chefLogin);
		}
	}

	@Transactional
	public boolean deleteAndAnonymize() {
		Chef chefLogin = JWTUtils.userLogin();
		if (chefLogin != null) {
			chefLogin.setUsername("ANON_" + chefLogin.getId());
			chefLogin.setPassword(null);
			chefLogin.setNombre("ANONIMO");
			chefLogin.setPrimerApellido("ANONIMO");
			chefLogin.setSegundoApellido("ANONIMO");
			chefLogin.setEmail("anonimo" + chefLogin.getId() + "@deleteUser.com");
			chefLogin.setTelefono(null);
			chefLogin.setRol(null);
			chefLogin.setActivo(false);
			chefLogin.setBaneado(true);

			chefRepository.save(chefLogin);
			return true;
		} else {
			return false;
		}
	}
	
	@Transactional
	public void activarseParaServicio() {
		Chef chefLogin = JWTUtils.userLogin();
		chefLogin.setActivo(true);
		this.saveBasico(chefLogin);
	}
	
	@Transactional
	public void desactivarseParaServicio() {
		Chef chefLogin = JWTUtils.userLogin();
		chefLogin.setActivo(false);
		this.saveBasico(chefLogin);
	}
}