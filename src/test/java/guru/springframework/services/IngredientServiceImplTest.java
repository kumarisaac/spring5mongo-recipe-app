package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    IngredientToIngredientCommand commandConverter = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());

    IngredientCommandToIngredient ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    IngredientServiceImpl service;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new IngredientServiceImpl(recipeRepository,commandConverter,
                                        ingredientCommandToIngredient, unitOfMeasureRepository );
    }

    @Test
    public void findByRecipeIdAndIngredientID() {

        //Given
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Ingredient ingredient1 =  new Ingredient();
        ingredient1.setId("2");
        ingredient1.setRecipe(recipe);
        Ingredient ingredient2 =  new Ingredient();
        ingredient2.setId("3");
        ingredient2.setRecipe(recipe);
        Ingredient ingredient3 =  new Ingredient();
        ingredient3.setId("4");
        ingredient3.setRecipe(recipe);
        recipe.getIngredients().add(ingredient1);
        recipe.getIngredients().add(ingredient2);
        recipe.getIngredients().add(ingredient3);
        when(recipeRepository.findById(anyString())).thenReturn(java.util.Optional.of(recipe));

        //when
        IngredientCommand ingredientCommand = service.findByRecipeIdAndIngredientID("1", "4");

        //Then
        assertEquals("1", ingredientCommand.getRecipeId());
        assertEquals("4", ingredientCommand.getId());
        verify(recipeRepository, times(1)).findById(any());

    }

    @Test
    public void testSaveExitingIngredient(){
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId("2");
        ingredientCommand.setRecipeId("3");

        Optional<Recipe> recipeOptional = Optional.of(new Recipe());
        Recipe savedRecipe = new Recipe();
        savedRecipe.setId("3");
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId("2");

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);
        //when
        IngredientCommand savedCommand = service.save(ingredientCommand);
        //then
        assertEquals(ingredientCommand.getId(), savedCommand.getId());
        assertEquals(ingredientCommand.getRecipeId(), savedCommand.getRecipeId());
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }


    @Test
    public void deleteIngredient() {

        //Given
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId("1");
        ingredient1.setRecipe(recipe);
        recipe.getIngredients().add(ingredient1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("2");
        ingredient2.setRecipe(recipe);
        recipe.getIngredients().add(ingredient2);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("3");
        ingredient3.setRecipe(recipe);
        recipe.getIngredients().add(ingredient3);

        Ingredient ingredient4 = new Ingredient();
        ingredient4.setId("4");
        ingredient4.setRecipe(recipe);
        recipe.getIngredients().add(ingredient4);

        Optional<Recipe> recipeOptional = Optional.of(recipe);
        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        //when
        service.deleteIngredient("1", "2");

        //then
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    public void testNewIngredientSave(){

        //given
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId("2");
        Recipe recipeBeforeSave = new Recipe();
        recipeBeforeSave.setId("1");

        Recipe recipeAfterSave = new Recipe();
        recipeAfterSave.setId("1");

        Ingredient mockIngredient = new Ingredient();
        mockIngredient.setId("2");
        mockIngredient.setRecipe(recipeAfterSave);
        mockIngredient.setAmount(new BigDecimal(2));
        mockIngredient.setUom(unitOfMeasure);
        mockIngredient.setDescription("some Description");
        recipeAfterSave.getIngredients().add(mockIngredient);
        IngredientCommand command = commandConverter.convert(mockIngredient);

        Ingredient newIngredient = new Ingredient();
        newIngredient.setRecipe(recipeAfterSave);
        newIngredient.setAmount(new BigDecimal(2));
        newIngredient.setUom(unitOfMeasure);
        newIngredient.setDescription("some Description");

        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipeBeforeSave));
        when(recipeRepository.save(any())).thenReturn(recipeAfterSave);

        //when
        IngredientCommand savedCommand = service.save(commandConverter.convert(newIngredient));

        //then
        assertEquals(command.getRecipeId(), savedCommand.getRecipeId());
        assertEquals(command.getAmount(), savedCommand.getAmount());
        assertEquals(command.getDescription(), savedCommand.getDescription());
        assertEquals(command.getUnitOfMeasure().getId(), savedCommand.getUnitOfMeasure().getId());
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }
}