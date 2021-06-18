package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;


public interface ImageService {
    Mono<RecipeCommand> getRecipeImage(String recipeId);
    Mono<Void> UploadImage(MultipartFile file, String recipeId);
}
