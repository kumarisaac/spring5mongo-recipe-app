package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import reactor.core.publisher.Mono;

public interface IngredientService {

    Mono<IngredientCommand> findByRecipeIdAndIngredientID(String recipeID, String ingredientId);
    Mono<IngredientCommand> save(IngredientCommand ingredientCommand);
    Mono<Void> deleteIngredient(String recipeId, String ingredientId);
}
