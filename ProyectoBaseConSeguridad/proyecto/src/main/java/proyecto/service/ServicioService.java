package proyecto.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import proyecto.entity.Chef;
import proyecto.entity.Cliente;
import proyecto.entity.Servicio;
import proyecto.respository.ServicioRepository;
import proyecto.security.JWTUtils;

@Service
public class ServicioService {
	@Autowired
	private ServicioRepository servicioRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ChefService chefService;

	@Autowired
	private JWTUtils JWTUtils;

	public Optional<Servicio> findById(int id) {
		return servicioRepository.findById(id);
	}

	public Servicio contratarServicio(Servicio servicio, int chefId) {
		Servicio servicioBD = null;

		Optional<Chef> chef = chefService.findById(chefId);
		if (chef.isPresent()) {
			if (!hayServiciosMismoDia(chef.get(), servicio)) {
				servicio.setMomentoContratacion(Date.valueOf(LocalDate.now()));
				servicio.setValoracionServicio(null);
				servicioBD = servicioRepository.save(servicio);

				Cliente clienteLogin = JWTUtils.userLogin();
				clienteLogin.getServiciosContratados().add(servicioBD);
				clienteService.saveBasico(clienteLogin);

				chef.get().getServiciosOfrecidos().add(servicioBD);
				chefService.saveBasico(chef.get());
			}
		}
		return servicioBD;
	}

	private boolean hayServiciosMismoDia(Chef c, Servicio s) {
		boolean res = false;
		for (Servicio servicio : c.getServiciosOfrecidos()) {
			if (servicio.getFechaServicio().equals(s.getFechaServicio())) {
				res = true;
				break;
			}
		}
		return res;
	}

	@Transactional
	public boolean puntuarServicio(Servicio servicio, int puntuacion) {
		boolean res = false;
		int suma = 0;
		int numValoraciones = 0;
		Cliente clienteLogin = JWTUtils.userLogin();

		if (clienteLogin.getServiciosContratados().contains(servicio)) {
			servicio.setValoracionServicio(puntuacion);
			servicioRepository.save(servicio);

			Chef c = chefService.findByServicioId(servicio.getId());
			for (Servicio s : c.getServiciosOfrecidos()) {
				if (s.getValoracionServicio() != null) {
					suma = suma + s.getValoracionServicio();
					numValoraciones++;
				}
			}
			c.setValoracionChef((double) suma / numValoraciones);
			chefService.save(c);

			res = true;
		}
		return res;
	}

	public List<Servicio> getAllByChefLogin() {
		Chef chefLogin = JWTUtils.userLogin();
		return chefLogin.getServiciosOfrecidos();
	}
	
	public List<Servicio> getAllByClienteLogin() {
		Cliente clienteLogin = JWTUtils.userLogin();
		return clienteLogin.getServiciosContratados();
	}
}