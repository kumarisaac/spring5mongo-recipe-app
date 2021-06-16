package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService{

    private final RecipeReactiveRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient commandToIngredient;
    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    public IngredientServiceImpl(RecipeReactiveRepository recipeRepository,
                                 IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient commandToIngredient,
                                 UnitOfMeasureReactiveRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.commandToIngredient = commandToIngredient;
        this.unitOfMeasureRepository = unitOfMeasureRepository;

    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientID(String recipeId, String ingredientId) {
        log.debug("start of findByRecipeIdAndIngredientID");
        log.debug("Ingredient Id is " + ingredientId);
        log.debug("recipe Id is " + recipeId);

        return recipeRepository.findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .single()
                .map(ingredient -> { IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
                                     command.setRecipeId(recipeId);
                                     return command;
                });

//        return recipeRepository.findById(recipeId)
//                .map(recipe -> recipe.getIngredients()
//                               .stream()
//                               .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
//                               .findFirst())
//                .filter(Optional::isPresent)
//                .map(ingredient -> { IngredientCommand command = ingredientToIngredientCommand.convert(ingredient.get());
//                                     command.setRecipeId(recipeId);
//                                     log.debug(command.toString());
//                                     return command;
//                });



//        Mono<Recipe> recipeOptional = recipeRepository.findById(recipeID);
//        if(recipeOptional.hasElement().){
//            log.debug("Recipe ID not Found");
//            return null;
//        }
//        Recipe recipe = recipeOptional.get();
//        log.debug("Size of ingredients = " + recipe.getIngredients().size());
//        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
//                                                        .filter(ingredient -> ingredient.getId().equals(ingredientId))
//                                                        .map(ingredient -> ingredientToIngredientCommand.convert(ingredient))
//                                                        .findFirst();
//        if(!ingredientCommandOptional.isPresent()){
//            log.debug("Ingredient Not present for ingredient id = " + ingredientId);
//            return null;
//        }
//        log.debug("end of findByRecipeIdAndIngredientID");
//        ingredientCommandOptional.get().setRecipeId(recipeID);
//        return ingredientCommandOptional.get();
    }


    @Override
    public Mono<IngredientCommand> save(IngredientCommand ingredientCommand) {
        log.debug("inside Save method of IngredientService");
        log.debug("Ingredient received : " + ingredientCommand.toString());
        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId()).blockOptional();
        if(!recipeOptional.isPresent()){
            log.debug("Recipe Not found : " + ingredientCommand.getRecipeId());
            return Mono.just(new IngredientCommand());
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
//            ingredientFound.setRecipe(recipe);
            log.debug("unit of measure ID = " + ingredientCommand.getUnitOfMeasure().getId());
            Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository.findById(ingredientCommand
                                                     .getUnitOfMeasure()
                                                      .getId()).blockOptional();
            if(!unitOfMeasure.isPresent()){
                throw new RuntimeException("Unit of Measure : " + ingredientCommand.getUnitOfMeasure().getId() + " Not Found");
            }
            ingredientFound.setUom(unitOfMeasure.get());
            log.debug("Updated Ingredient: " + ingredientFound.toString());
         }else{
            log.debug("Ingredient Not found in recipe");
            Ingredient newIngredient = new Ingredient();
            newIngredient = commandToIngredient.convert(ingredientCommand);
            log.debug("ingredient generated is = " + newIngredient.getId());
//            newIngredient.setRecipe(recipe);
            recipe.addIngredient(newIngredient);
        }

        Recipe savedRecipe = recipeRepository.save(recipe).block();

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
                    .findFirst();
            log.debug("Saved Ingredient for new : " + savedIngredient.get().toString());
        }

        IngredientCommand returnCommand = ingredientToIngredientCommand.convert(savedIngredient.get());
        returnCommand.setRecipeId(savedRecipe.getId());
        return Mono.just(returnCommand);

    }

    @Override
    public Mono<Void> deleteIngredient(String recipeId, String ingredientId){
        log.debug("Start of delete Ingredient");


        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId).blockOptional();

        if(recipeOptional.isPresent()){
            Recipe recipe = recipeOptional.get();
            Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();
            if(ingredientOptional.isPresent()){
                log.debug("ingredeint id found is :" + ingredientOptional.get().getId().toString());
                log.debug(ingredientOptional.get().toString());
                Ingredient ingredientTodelete = ingredientOptional.get();
//                ingredientTodelete.setRecipe(null);
                recipe.getIngredients().remove(ingredientOptional.get());
                recipeRepository.save(recipe).block();
            }else{
                log.error("Ingredient id not found : " + ingredientId);
            }
        }else {
            log.error("recipe id not found : " + recipeId);
        }
        log.debug("End of delete Ingredient");
        return Mono.empty();

    }
}
