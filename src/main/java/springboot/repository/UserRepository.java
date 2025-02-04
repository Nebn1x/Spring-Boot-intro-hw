package springboot.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import springboot.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
