package proyecto.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import proyecto.entity.Chef;

@Repository
public interface ChefRepository extends JpaRepository<Chef, Integer>{
	// Buscar por usuario (Login)
	@Query("SELECT c FROM Chef c WHERE c.username=?1")
	Optional<Chef> findByUsername(String username);
	
	// Buscar solo chefs activos
	@Query("SELECT c FROM Chef c WHERE c.activo=true")
	List<Chef> getListChefActivos();
	
	// Buscar al chef dueño de un servicio concreto
	@Query("SELECT c FROM Chef c JOIN c.serviciosOfrecidos s WHERE s.id = ?1")
	Chef findByServicioId(int idServicio);
}
