package springboot.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springboot.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @EntityGraph(attributePaths = "roles")
    @Query("SELECT r FROM Role r WHERE r.role = :roleName")
    Role byRoleName(Role.RoleName roleName);
}
