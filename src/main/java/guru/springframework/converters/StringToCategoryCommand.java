package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StringToCategoryCommand implements Converter<String, CategoryCommand> {

    private final CategoryService categoryService;

    public StringToCategoryCommand(CategoryService categoryService) {
        this.categoryService = categoryService;
     }

    @Override
    public CategoryCommand convert(String s) {

        log.debug("start of String to Category Converter");

        log.debug("end of String to Category Converter");
        return categoryService.findById(s);
    }
}
