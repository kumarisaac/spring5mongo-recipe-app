package guru.springframework.repositories.reactive;

import guru.springframework.domain.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@RunWith(SpringRunner.class)
@DataMongoTest
class RecipeReactiveRepositoryTest {

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    Recipe recipe;

    @BeforeEach
    void setUp() {
        log.error("inside setup method");
        recipeReactiveRepository.deleteAll().block();
        recipe = new Recipe();
        recipe.setId("1");
        recipe.setDescription("Rajma Masala");
    }

    @Test
    void testSaverecipe(){
        //Given

        //when
        Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

        //then
        assertEquals(1l, recipeReactiveRepository.count().block());
    }

    @Test
    void findByDescriptionLike() {
        //given
        Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

        //when
        Recipe recipe = recipeReactiveRepository.findByDescriptionLike("Rajma").blockFirst();

        //then
        assertEquals("1", recipe.getId());
        assertEquals("Rajma Masala", recipe.getDescription());

    }
}