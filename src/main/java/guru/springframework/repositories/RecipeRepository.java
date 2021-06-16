package guru.springframework.repositories;

import guru.springframework.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;


public interface RecipeRepository extends CrudRepository<Recipe, String> {
    Collection<Recipe> findByDescriptionLike(String description);
}
