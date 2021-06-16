package guru.springframework.repositories;

import guru.springframework.domain.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryRepositoryIT {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;


    @Before
    public void setUp() throws Exception {
     //   DataLoader dataLoader = new DataLoader(categoryRepository, unitOfMeasureRepository, recipeRepository);

//        recipeRepository.deleteAll();
//        unitOfMeasureRepository.deleteAll();
//        categoryRepository.deleteAll();

     //   dataLoader.onApplicationEvent(null);
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
    public void findByCategoryNameLikeMexican(){
        Optional<Category> category = categoryRepository.findByCategoryNameLike("Mexi");
        assertEquals("Mexican", category.get().getCategoryName());
    }
}