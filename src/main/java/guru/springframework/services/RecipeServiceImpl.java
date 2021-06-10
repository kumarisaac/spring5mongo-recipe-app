package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {

        Set<Recipe> recipeSet = new HashSet<Recipe>();

        recipeRepository.findAll().forEach(recipe -> {recipeSet.add(recipe);});

        return recipeSet;
    }

    public Recipe findById(String id){


        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if(!recipeOptional.isPresent()){
            //throw new RuntimeException("Recipe Not Found");
            throw new NotFoundException("Recipe Not Found for recipe id : " + id.toString());
        }


        return recipeOptional.get();
    }

    @Override
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {

        Recipe dbRecipe = findById(recipeCommand.getId());

        recipeCommand.setImage(dbRecipe.getImage());
        Recipe recipe = recipeCommandToRecipe.convert(recipeCommand);

        Recipe savedRecipe = recipeRepository.save(recipe);

        log.debug("Saved RecipeID: " + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    public RecipeCommand findCommandById(String aLong) {
        return recipeToRecipeCommand.convert(findById(aLong));
    }

    @Override
    public void deleteById(String aLong) {
        recipeRepository.deleteById(aLong);
    }
}
