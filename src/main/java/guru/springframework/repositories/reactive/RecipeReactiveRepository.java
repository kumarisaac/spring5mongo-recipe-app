package guru.springframework.repositories.reactive;

import guru.springframework.domain.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface RecipeReactiveRepository extends ReactiveMongoRepository <Recipe, String>{
    Flux<Recipe> findByDescriptionLike(String description);
}
