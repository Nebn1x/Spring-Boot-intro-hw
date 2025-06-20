package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
