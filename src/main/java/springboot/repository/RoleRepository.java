package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
