package guru.springframework.services;

import guru.springframework.commands.CategoryCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryService {

    Flux<CategoryCommand> getAllCategories();
    Mono<CategoryCommand> findById(String id);
}
