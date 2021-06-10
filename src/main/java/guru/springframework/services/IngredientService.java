package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientID(String recipeID, String ingredientId);
    IngredientCommand save(IngredientCommand ingredientCommand);
    void deleteIngredient(String recipeId, String ingredientId);
}
