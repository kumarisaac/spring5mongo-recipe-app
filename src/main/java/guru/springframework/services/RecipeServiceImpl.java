package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeReactiveRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeReactiveRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        log.debug("Inside Get Recipes");

        return recipeRepository.findAll();
    }

    public Mono<Recipe> findById(String id){

//        if(!recipeOptional){
//            //throw new RuntimeException("Recipe Not Found");
//            throw new NotFoundException("Recipe Not Found for recipe id : " + id.toString());
//        }


        return recipeRepository.findById(id);
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand) {
        log.debug("inside save Recipe command method asdf");

        if(!recipeCommand.getId().isEmpty()){
            Recipe dbRecipe = findById(recipeCommand.getId()).block();
            recipeCommand.setImage(dbRecipe.getImage());
        }

        log.debug("End of save Recipe command method");
        return recipeRepository.save(recipeCommandToRecipe.convert(recipeCommand))
                .map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String aLong) {

        return recipeRepository.findById(aLong)
                .map(recipe -> {
                    RecipeCommand command = recipeToRecipeCommand.convert(recipe);
                    command.getIngredients().forEach(ingredientCommand -> ingredientCommand.setRecipeId(recipe.getId()));
                    return command;
                });

        //return Mono.just(recipeToRecipeCommand.convert(findById(aLong).block()));
    }

    @Override
    public Mono<Void> deleteById(String aLong) {
        return recipeRepository.deleteById(aLong);
    }

    @Override
    public Flux<Recipe> findByDescriptionLike(String description) {
        return recipeRepository.findByDescriptionLike(description);
    }
}
