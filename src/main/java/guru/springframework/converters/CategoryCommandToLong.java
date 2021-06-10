package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CategoryCommandToLong implements Converter<CategoryCommand, String> {

    @Override
    public String convert(CategoryCommand categoryCommand) {
        log.debug("inside Category command to String converter");


        log.debug("inside Category command to String converter");
        return categoryCommand.getId();
    }
}
