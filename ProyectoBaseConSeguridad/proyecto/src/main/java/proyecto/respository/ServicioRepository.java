package proyecto.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import proyecto.entity.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
	
}
