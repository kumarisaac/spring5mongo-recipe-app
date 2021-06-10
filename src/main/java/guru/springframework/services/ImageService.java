package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import org.springframework.web.multipart.MultipartFile;



public interface ImageService {
    RecipeCommand getRecipeImage(String recipeId);
    void UploadImage(MultipartFile file, String recipeId);
}
