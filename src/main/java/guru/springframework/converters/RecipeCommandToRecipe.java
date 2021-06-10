package guru.springframework.converters;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

@Controller
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

    private final NotesCommandToNotes notesCommandToNotes;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final CategoryCommandToCategory categoryCommandToCategory;

    public RecipeCommandToRecipe(NotesCommandToNotes notesCommandToNotes,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 CategoryCommandToCategory categoryCommandToCategory) {
        this.notesCommandToNotes = notesCommandToNotes;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.categoryCommandToCategory = categoryCommandToCategory;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(RecipeCommand recipeCommand) {

        if(recipeCommand == null) {
            return null;
        }
        final Recipe recipe = new Recipe();
        recipe.setId(recipeCommand.getId());
        recipe.setDescription(recipeCommand.getDescription());
        recipe.setPrepTime(recipeCommand.getPrepTime());
        recipe.setCookTime(recipeCommand.getCookTime());
        recipe.setServings(recipeCommand.getServings());
        recipe.setSource(recipeCommand.getSource());
        recipe.setUrl(recipeCommand.getUrl());
        recipe.setDirections(recipeCommand.getDirections());
        recipe.setPrepTime(recipeCommand.getPrepTime());
        recipe.setDifficulty(recipeCommand.getDifficulty());
        recipe.setImage(recipeCommand.getImage());
        recipe.setImagePath(recipeCommand.getImagePath());
        recipe.setNotes(notesCommandToNotes.convert(recipeCommand.getNotes()));

        if (recipeCommand.getCategories() != null && recipeCommand.getCategories().size() > 0){
            recipeCommand.getCategories().forEach( categoryCommand -> {recipe.getCategories().add(categoryCommandToCategory.convert(categoryCommand));});
        }

        if (recipeCommand.getIngredients() != null && recipeCommand.getIngredients().size() > 0){
            recipeCommand.getIngredients().forEach(ingredient -> {recipe.getIngredients().add(ingredientCommandToIngredient.convert(ingredient));});
        }

        return recipe;
    }
}
