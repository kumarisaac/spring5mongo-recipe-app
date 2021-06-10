package guru.springframework.services;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.converters.CategoryToCategoryCommand;
import guru.springframework.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryToCategoryCommand converter;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryToCategoryCommand converter) {
        this.categoryRepository = categoryRepository;
        this.converter = converter;
    }

    @Override
    public Set<CategoryCommand> getAllCategories() {

        log.debug("inside get All Categories method");

        Set<CategoryCommand> commands = new HashSet<>();

        categoryRepository.findAll().forEach(category -> {commands.add(converter.convert(category)); log.debug(category.getCategoryName());});

        log.debug("End of get All Categories method");
        return commands;
    }

    @Override
    public CategoryCommand findById(String id) {
        return converter.convert(categoryRepository.findById(id).get());
    }
}
