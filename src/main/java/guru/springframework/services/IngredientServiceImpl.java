package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService{

    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient commandToIngredient;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(RecipeRepository recipeRepository,
                                 IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient commandToIngredient,
                                 UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.commandToIngredient = commandToIngredient;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientID(String recipeID, String ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeID);
        if(!recipeOptional.isPresent()){
            log.debug("Recipe ID not Found");
            return null;
        }
        Recipe recipe = recipeOptional.get();
        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                                                        .filter(ingredient -> ingredient.getId().equals(ingredientId))
                                                        .map(ingredient -> ingredientToIngredientCommand.convert(ingredient))
                                                        .findFirst();
        if(!ingredientCommandOptional.isPresent()){
            log.debug("Ingredient Not present");
            return null;
        }
        return ingredientCommandOptional.get();
    }


    @Override
    public IngredientCommand save(IngredientCommand ingredientCommand) {
        log.debug("inside Save method of IngredientService");
        log.debug("Ingredient received : " + ingredientCommand.toString());
        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());
        if(!recipeOptional.isPresent()){
            log.debug("Recipe Not found : " + ingredientCommand.getRecipeId());
            return new IngredientCommand();
        }
        log.debug("Recipe Found :" + recipeOptional.get().toString());
        Recipe recipe = recipeOptional.get();
        Optional<Ingredient> ingredient = recipe.getIngredients().stream()
                                                .filter(ingredient1 -> ingredient1.getId().equals(ingredientCommand.getId()))
                                                .findFirst();

        if(ingredient.isPresent()){
            log.debug("Ingredient found");
            Ingredient ingredientFound = ingredient.get();
            ingredientFound.setId(ingredientCommand.getId());
            ingredientFound.setAmount(ingredientCommand.getAmount());
            ingredientFound.setDescription(ingredientCommand.getDescription());
            ingredientFound.setRecipe(recipe);
            log.debug("unit of measure ID = " + ingredientCommand.getUnitOfMeasure().getId());
            Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository.findById(ingredientCommand.getUnitOfMeasure().getId());
            if(!unitOfMeasure.isPresent()){
                throw new RuntimeException("Unit of Measure : " + ingredientCommand.getUnitOfMeasure().getId() + " Not Found");
            }
            ingredientFound.setUom(unitOfMeasure.get());
            log.debug("Updated Ingredient: " + ingredientFound.toString());
         }else{
            log.debug("Ingredient Not found in recipe");
            Ingredient newIngredient =commandToIngredient.convert(ingredientCommand);
            newIngredient.setRecipe(recipe);
            recipe.addIngredient(newIngredient);
        }

        Recipe savedRecipe = recipeRepository.save(recipe);
        Optional<Ingredient> savedIngredient = Optional.of(new Ingredient());
        if(ingredientCommand.getId() != null){
            savedIngredient = savedRecipe.getIngredients().stream()
                    .filter(ingredient1 -> ingredientCommand.getId().equals(ingredient1.getId()))
                    .findFirst();
            log.debug("Saved Ingredient for update : " + savedIngredient.get().toString());
        }else{
            savedIngredient = savedRecipe.getIngredients().stream()
                    .filter(ingredient1 -> ingredientCommand.getDescription().equals(ingredient1.getDescription()))
                    .filter(ingredient1 -> ingredientCommand.getAmount().equals(ingredient1.getAmount()))
                    .filter(ingredient1 -> ingredientCommand.getUnitOfMeasure().getId().equals(ingredient1.getUom().getId()))
                    .findFirst();
            log.debug("Saved Ingredient for new : " + savedIngredient.get().toString());
        }

        return ingredientToIngredientCommand.convert(savedIngredient.get());

    }

    @Override
    public void deleteIngredient(String recipeId, String ingredientId){
        log.debug("Start of delete Ingredient");


        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(recipeOptional.isPresent()){
            Recipe recipe = recipeOptional.get();
            Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();
            if(ingredientOptional.isPresent()){
                log.debug("ingredeint id found is :" + ingredientOptional.get().getId().toString());
                log.debug(ingredientOptional.get().toString());
                Ingredient ingredientTodelete = ingredientOptional.get();
                ingredientTodelete.setRecipe(null);
                recipe.getIngredients().remove(ingredientOptional.get());
                recipeRepository.save(recipe);
            }else{
                log.error("Ingredient id not found : " + ingredientId);
            }
        }else {
            log.error("recipe id not found : " + recipeId);
        }

        log.debug("End of delete Ingredient");
    }
}
