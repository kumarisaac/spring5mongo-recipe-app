package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.domain.Category;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryCommandToCategoryTest {


    CategoryCommandToCategory converter;
    final String CATEGORY_ID = "2";
    final String CATEGORY_NAME = "Amercian";

    @Before
    public void setUp() throws Exception {
        converter = new CategoryCommandToCategory();
    }

    @Test
    public void testNotNull(){
        assertNotNull(converter.convert(new CategoryCommand()));
    }

    @Test
    public void testNull(){
        assertNull(converter.convert(null));
    }


    @Test
    public void testConvert() {
        //Given
        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(CATEGORY_ID);
        categoryCommand.setCategoryName(CATEGORY_NAME);

        //When
        Category convertedCategory = converter.convert(categoryCommand);

        //Then
        assertNotNull(convertedCategory);
        assertEquals(CATEGORY_ID, convertedCategory.getId());
        assertEquals(CATEGORY_NAME, convertedCategory.getCategoryName());

    }
}