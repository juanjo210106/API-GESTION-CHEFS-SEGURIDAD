package proyecto.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import proyecto.entity.Admin;
import java.util.List;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
	@Query("SELECT a FROM Admin a WHERE a.username=?1")
	Optional<Admin> findByUsername(String username);
}
