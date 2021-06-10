package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

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

    IngredientController ingredientController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientController = new IngredientController(recipeService, ingredientService, unitOfMeasureService);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    public void testGetIngredientList() throws Exception{
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
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
        Set<UnitOfMeasureCommand> measureCommands = new HashSet<>();
        when(ingredientService.findByRecipeIdAndIngredientID(anyString(), anyString())).thenReturn(command);
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
        when(ingredientService.findByRecipeIdAndIngredientID(anyString(), anyString())).thenReturn(command);


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
        RecipeCommand recipeCommand = new RecipeCommand();
        Set<UnitOfMeasureCommand> uomCommands = new HashSet<>();

        when(recipeService.findCommandById(anyString())).thenReturn(recipeCommand);
        when(unitOfMeasureService.getUnitOfMeasure()).thenReturn(uomCommands);

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


        mockMvc.perform(get("/recipe/1/ingredient/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredients"));

        verify(ingredientService, times(1)).deleteIngredient(anyString(), anyString());

    }

}