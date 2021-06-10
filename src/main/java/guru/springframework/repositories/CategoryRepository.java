package guru.springframework.repositories;

import guru.springframework.domain.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface CategoryRepository extends CrudRepository<Category, String> {

    Optional<Category> findByCategoryName(String categoryName);
}
