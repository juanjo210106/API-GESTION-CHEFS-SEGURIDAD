
package proyecto.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import proyecto.entity.Admin;
import proyecto.entity.Rol;
import proyecto.respository.AdminRepository;
import proyecto.security.JWTUtils;

@Service
public class AdminService {
	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private BCryptPasswordEncoder BCryptPasswordEncoder;

	@Autowired
	private JWTUtils JWTUtils;

	public Optional<Admin> findByUsername(String username) {
		return adminRepository.findByUsername(username);
	}
	
	public Admin getAdminLogueado() {
		Admin adminLogin = JWTUtils.userLogin();
		return adminLogin;
	}
	
	@Transactional
	public Admin save(Admin admin) {
		admin.setPassword(BCryptPasswordEncoder.encode(admin.getPassword()));
		admin.setRol(Rol.ADMIN);
		admin.setBaneado(false);
		return adminRepository.save(admin);
	}

	@Transactional
	public Admin update(Admin admin) {
		Admin adminLogin = JWTUtils.userLogin();
		if (adminLogin == null) {
			return null;
		} else {
			adminLogin.setPassword(admin.getPassword());
			adminLogin.setNombre(admin.getNombre());
			adminLogin.setPrimerApellido(admin.getPrimerApellido());
			adminLogin.setSegundoApellido(admin.getSegundoApellido());
			adminLogin.setEmail(admin.getEmail());
			adminLogin.setTelefono(admin.getTelefono());
			return adminRepository.save(adminLogin);
		}
	}

	@Transactional
	public boolean deleteAndAnonymize() {
		Admin adminLogin = JWTUtils.userLogin();
		if (adminLogin != null) {
			adminLogin.setUsername("ANON_" + adminLogin.getId());
			adminLogin.setPassword(null);
			adminLogin.setNombre("ANONIMO");
			adminLogin.setPrimerApellido("ANONIMO");
			adminLogin.setSegundoApellido("ANONIMO");
			adminLogin.setEmail("anonimo" + adminLogin.getId() + "@deleteUser.com");
			adminLogin.setTelefono(null);
			adminLogin.setRol(null);
			adminLogin.setBaneado(true);

			adminRepository.save(adminLogin);
			return true;
		} else {
			return false;
		}
	}
}
