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

        Flux<Recipe> recipeSet = recipeRepository.findAll();

        return recipeSet;
    }

    public Mono<Recipe> findById(String id){


        Mono<Recipe> recipeOptional = recipeRepository.findById(id);
//        if(!recipeOptional){
//            //throw new RuntimeException("Recipe Not Found");
//            throw new NotFoundException("Recipe Not Found for recipe id : " + id.toString());
//        }


        return recipeOptional;
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand) {
        log.debug("inside save Recipe command method asdf");

        if(!recipeCommand.getId().isEmpty()){
            Recipe dbRecipe = findById(recipeCommand.getId()).block();
            recipeCommand.setImage(dbRecipe.getImage());
        }

        Recipe recipe = recipeCommandToRecipe.convert(recipeCommand);

        Recipe savedRecipe = recipeRepository.save(recipe).block();

        log.debug("Saved RecipeID: " + savedRecipe.getId());
        log.debug("End of save Recipe command method");
        return Mono.just(recipeToRecipeCommand.convert(savedRecipe));
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String aLong) {
        return Mono.just(recipeToRecipeCommand.convert(findById(aLong).block()));
    }

    @Override
    public Mono<Void> deleteById(String aLong) {
        recipeRepository.deleteById(aLong).block();
        return Mono.empty();
    }

    @Override
    public Flux<Recipe> findByDescriptionLike(String description) {
        return recipeRepository.findByDescriptionLike(description);
    }
}
