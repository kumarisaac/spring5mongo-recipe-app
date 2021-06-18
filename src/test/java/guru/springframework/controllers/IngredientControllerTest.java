package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class IngredientControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    @Mock
    IngredientToIngredientCommand ingredientToIngredientCommand;

    IngredientController ingredientController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientController = new IngredientController(recipeService, ingredientService, unitOfMeasureService, ingredientToIngredientCommand);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    public void testGetIngredientList() throws Exception{
        //given
        Mono<RecipeCommand> recipeCommand = Mono.just(new RecipeCommand());
        when(recipeService.findCommandById(anyString())).thenReturn(recipeCommand);

        //when
        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));

        //then
        verify(recipeService, times(1)).findCommandById(anyString());
    }

    @Test
    public void testupdateIngredient() throws Exception{
        //given
        IngredientCommand command =  new IngredientCommand();
        Flux<UnitOfMeasureCommand> measureCommands = Flux.just();
        when(ingredientService.findByRecipeIdAndIngredientID(anyString(), anyString())).thenReturn(Mono.just(command));
        when(unitOfMeasureService.getUnitOfMeasure()).thenReturn(measureCommands);

        //when
        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"))
                .andExpect(view().name("/recipe/ingredient/ingredientform"));

        //then
        verify(unitOfMeasureService, times(1)).getUnitOfMeasure();
        verify(ingredientService, times(1)).findByRecipeIdAndIngredientID(anyString(), anyString());
    }

    @Test
    public void testGetIngredient() throws Exception{
        //given
        IngredientCommand command = new IngredientCommand();
        when(ingredientService.findByRecipeIdAndIngredientID(anyString(), anyString())).thenReturn(Mono.just(command));


        //when
        mockMvc.perform(get("/recipe/1/ingredient/2/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("/recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));

        //then
        verify(ingredientService, times(1)).findByRecipeIdAndIngredientID(anyString(), anyString());
    }

    @Test
    public void testAddNewIngredient() throws Exception{

        //given
        Mono<RecipeCommand> recipeCommand = Mono.just(new RecipeCommand());
        Flux<UnitOfMeasureCommand> uomCommands = Flux.just();
        IngredientCommand ingredientCommand = new IngredientCommand();
        when(recipeService.findCommandById(anyString())).thenReturn(recipeCommand);
        when(unitOfMeasureService.getUnitOfMeasure()).thenReturn(uomCommands);
        when(ingredientToIngredientCommand.convert(any())).thenReturn(ingredientCommand);

        //when
        mockMvc.perform(get("/recipe/1/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"))
                .andExpect(view().name("recipe/ingredient/ingredientform"));

        //then
        verify(recipeService, times(1)).findCommandById(anyString());
        verify(unitOfMeasureService, times(1)).getUnitOfMeasure();
    }

    @Test
    public void testDeleteIngredient() throws Exception{


        when(ingredientToIngredientCommand.convert(any())).thenReturn(new IngredientCommand());
        when(ingredientService.deleteIngredient(anyString(), anyString())).thenReturn(Mono.empty());
        mockMvc.perform(get("/recipe/1/ingredient/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredients"));

        verify(ingredientService, times(1)).deleteIngredient(anyString(), anyString());

    }

}