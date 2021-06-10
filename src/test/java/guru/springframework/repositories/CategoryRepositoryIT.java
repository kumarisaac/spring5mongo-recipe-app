package guru.springframework.repositories;

import guru.springframework.domain.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CategoryRepositoryIT {

    @Autowired
    CategoryRepository categoryRepository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void findByCategoryName() {
        Optional<Category> categoryOptional = categoryRepository.findByCategoryName("American");
        assertEquals("American", categoryOptional.get().getCategoryName());
    }

    @Test
    public void findByCategorySize(){
        Set<Category> categories = new HashSet<>();
        categoryRepository.findAll().forEach(categories::add);
        assertEquals(4, categories.size());
    }

    @Test
    public void findByIdMexican(){
        Optional<Category> category = categoryRepository.findById("3");
        assertEquals("Mexican", category.get().getCategoryName());
    }
}