package guru.springframework.services;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.converters.CategoryToCategoryCommand;
import guru.springframework.repositories.reactive.CategoryReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryReactiveRepository categoryRepository;
    private final CategoryToCategoryCommand converter;

    public CategoryServiceImpl(CategoryReactiveRepository categoryRepository, CategoryToCategoryCommand converter) {
        this.categoryRepository = categoryRepository;
        this.converter = converter;
    }

    @Override
    public Flux<CategoryCommand> getAllCategories() {

        log.debug("inside get All Categories method");

        Set<CategoryCommand> commands = new HashSet<>();

        categoryRepository.findAll().toIterable().forEach(category -> {commands.add(converter.convert(category)); log.debug(category.getCategoryName());});

        log.debug("End of get All Categories method");

        return Flux.fromIterable(commands);
    }

    @Override
    public Mono<CategoryCommand> findById(String id) {
        return Mono.just(converter.convert(categoryRepository.findById(id).block()));
    }
}
