package proyecto.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import proyecto.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
	@Query("SELECT c FROM Cliente c WHERE c.username=?1")
	Optional<Cliente> findByUsername(String username);
}
