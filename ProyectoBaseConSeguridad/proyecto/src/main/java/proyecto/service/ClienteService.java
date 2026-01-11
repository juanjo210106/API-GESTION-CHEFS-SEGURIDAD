package proyecto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import proyecto.entity.Cliente;
import proyecto.entity.Rol;
import proyecto.entity.Servicio;
import proyecto.respository.ClienteRepository;
import proyecto.security.JWTUtils;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private BCryptPasswordEncoder BCryptPasswordEncoder;

	@Autowired
	private JWTUtils JWTUtils;
	
	@Autowired
	@Lazy
	private ServicioService servicioService;

	public Optional<Cliente> findByUsername(String username) {
		return clienteRepository.findByUsername(username);
	}

	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}

	public Optional<Cliente> findById(int id) {
		return clienteRepository.findById(id);
	}

	public Cliente getClienteLogueado() {
		Cliente clienteLogin = JWTUtils.userLogin();
		return clienteLogin;
	}

	@Transactional
	public Cliente save(Cliente cliente) {
		cliente.setPassword(BCryptPasswordEncoder.encode(cliente.getPassword()));
		cliente.setRol(Rol.CLIENTE);
		cliente.setBaneado(false);

		cliente.setServiciosContratados(new ArrayList<Servicio>());
		return clienteRepository.save(cliente);
	}

	@Transactional
	public Cliente saveBasico(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	@Transactional
	public Cliente update(Cliente cliente) {
		Cliente clienteLogin = JWTUtils.userLogin();
		if (clienteLogin == null) {
			return null;
		} else {
			clienteLogin.setPassword(cliente.getPassword());
			clienteLogin.setNombre(cliente.getNombre());
			clienteLogin.setPrimerApellido(cliente.getPrimerApellido());
			clienteLogin.setSegundoApellido(cliente.getSegundoApellido());
			clienteLogin.setEmail(cliente.getEmail());
			clienteLogin.setTelefono(cliente.getTelefono());
			clienteLogin.setDatosMedicos(cliente.getDatosMedicos());
			clienteLogin.setDireccionPostal(cliente.getDireccionPostal());
			return clienteRepository.save(clienteLogin);
		}
	}

	@Transactional
	public boolean deleteAndAnonymize() {
		Cliente clienteLogin = JWTUtils.userLogin();
		if (clienteLogin != null) {
			clienteLogin.setUsername("ANON_" + clienteLogin.getId());
			clienteLogin.setPassword(null);
			clienteLogin.setNombre("ANONIMO");
			clienteLogin.setPrimerApellido("ANONIMO");
			clienteLogin.setSegundoApellido("ANONIMO");
			clienteLogin.setEmail("anonimo" + clienteLogin.getId() + "@deleteUser.com");
			clienteLogin.setTelefono(null);
			clienteLogin.setDatosMedicos("ANONIMO");
			clienteLogin.setDireccionPostal("ANONIMO");
			clienteLogin.setBaneado(true);
			clienteLogin.setRol(null);

			clienteRepository.save(clienteLogin);
			return true;
		} else {
			return false;
		}
	}
}