package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.services.CategoryService;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
public class RecipeControllerTest extends TestCase {

    @Mock
    RecipeService recipeService;

    @Mock
    ImageService imageService;

    @Mock
    CategoryService categoryService;

    @Mock
    Model model;

    MockMvc mockMvc;

    RecipeController recipeController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        recipeController = new RecipeController(recipeService, imageService, categoryService);

        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void testGetRecipe() throws Exception{

        //Set<Recipe> recipeSet = new HashSet<>();
        Recipe recipe = new Recipe();
        recipe.setId("1");
        //recipeSet.add(recipe);

        Mono<Recipe> recipeMono = Mono.just(recipe);
        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        when(recipeService.findById(anyString())).thenReturn(recipeMono);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));

        String viewName = recipeController.getRecipe("1", model);

        verify(model).addAttribute(eq("recipe"), argumentCaptor.capture());
        assertEquals(recipeMono.block(), argumentCaptor.getValue());
    }

    @Test
    public void testGetRecipeForm() throws Exception{

        when(categoryService.getAllCategories()).thenReturn(Flux.empty());


        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"));
    }

//    @Test
//    public void testSaveOrUpdate() throws Exception{
//        RecipeCommand recipeCommand = new RecipeCommand();
//        recipeCommand.setId(2L);
//        recipeCommand.setDescription("some descrip");
//
//        when(recipeService.saveRecipeCommand(any())).thenReturn(recipeCommand);
//
//        mockMvc.perform(post("/recipe")
//                .param("id", "")
//                .param("description", "sasdfdasf")
//                .param("directions", "afdsfdfafdasfadsfasf")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/recipe/2/show"));
//
//    }

    @Test
    public void testGetUpdateForm() throws Exception{
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("2");

        when(recipeService.findCommandById(any())).thenReturn(Mono.just(recipeCommand));
        when(categoryService.getAllCategories()).thenReturn(Flux.empty());

        mockMvc.perform(get("/recipe/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testDeleteRecipe() throws Exception{

        when(recipeService.deleteById(anyString())).thenReturn(Mono.empty());
        mockMvc.perform(get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
        verify(recipeService, times(1)).deleteById(anyString());
    }

//    @Test
//    public void testNumberformatException() throws Exception{
//
//        mockMvc.perform(get("/recipe/adf/show"))
//                .andExpect(status().isBadRequest())
//                .andExpect(view().name("400error"));
//    }

}