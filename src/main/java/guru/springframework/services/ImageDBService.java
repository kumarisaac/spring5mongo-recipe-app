package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@Profile({"db", "default"})
public class ImageDBService implements ImageService {

    private final RecipeRepository recipeRepository;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public ImageDBService(RecipeRepository recipeRepository, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public RecipeCommand getRecipeImage(String recipeId) {
        log.debug("inside Get Recipe Image");
        RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipeRepository.findById(recipeId).get());

        log.debug("Image length is :" + recipeCommand.getImage().length);


        log.debug("end of recipe image");
        return recipeCommand;
    }

    @Override
    public void UploadImage(MultipartFile file, String recipeId) {

        log.debug("Inside upload image");

        try {
            Recipe recipe = recipeRepository.findById(recipeId).get();
            Byte[] convertBytes = new Byte[file.getBytes().length];
            int i = 0;

            for (byte imageByte : file.getBytes()){
                 convertBytes[i++] = imageByte;
            }
            recipe.setImage(convertBytes);
            recipe.setImagePath(file.getOriginalFilename());
            recipeRepository.save(recipe);
        }catch (Exception e){
            log.debug("Exception while saving file " + e.getMessage());
            e.printStackTrace();
        }

        log.debug("end of  upload image");
    }
}
