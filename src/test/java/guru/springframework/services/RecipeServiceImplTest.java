package guru.springframework.services;

import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;


    @Mock
    RecipeReactiveRepository recipeRepository;
    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;
    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe ;
    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    public void testGetRecipes() {
        Recipe recipe = new Recipe();
        recipe.setDescription("Kumar Sambar");
        recipe.setPrepTime(10);
        Set<Recipe> recipeData = new HashSet<>();
        recipeData.add(recipe);

        when(recipeRepository.findAll()).thenReturn(Flux.fromIterable(recipeData));

        for (Recipe recipe1: recipeService.getRecipes().toIterable())
        {
            Assert.assertEquals("Kumar Sambar", recipe1.getDescription());
            Assert.assertEquals(new Integer(10), recipe1.getPrepTime());
        }

        verify(recipeRepository, times(1)).findAll();
        verify(recipeRepository, never()).findById(anyString());
    }

    @Test
    public void testGetRecipesById() {
        String id = "2";
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setDescription("Kumar Sambar");
        recipe.setPrepTime(10);
        Set<Recipe> recipeData = new HashSet<>();
        recipeData.add(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        Recipe returnRecipe = recipeService.findById("2").block();

        Assert.assertEquals("Kumar Sambar", returnRecipe.getDescription());
        Assert.assertEquals(new Integer(10), returnRecipe.getPrepTime());

        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, never()).findAll();

    }

    @Test
    public void testDeleteById(){
        //given
        String id = "2";

        //when
        recipeService.deleteById(id);

        //then
        verify(recipeRepository, times(1)).deleteById(anyString());

    }

    //@Test(expected = NotFoundException.class)
    public void getRecipeNotFound(){

        Recipe recipe = recipeService.findById("1").block();

    }
}