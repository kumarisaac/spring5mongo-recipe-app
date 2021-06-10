package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@Profile("filestore")
public class ImageFileSystemService implements ImageService {

    private final RecipeRepository recipeRepository;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    @Value("${Image.file.path}")
    private String folderPath;

    public ImageFileSystemService(RecipeRepository recipeRepository, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public RecipeCommand getRecipeImage(String recipeId){
        log.debug("Inside Get recipe image");
        RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipeRepository.findById(recipeId).get());

        if(recipeCommand.getImagePath() != null){
                try{
                    Path path = Paths.get(recipeCommand.getImagePath());

                    byte[] imageBytes = Files.readAllBytes(path);

                    Byte[] wrappedByte = new Byte[imageBytes.length];
                    int i = 0;

                    for (byte iByte : imageBytes){

                        wrappedByte[i++] = iByte;
                    }
                    recipeCommand.setImage(wrappedByte);
                }catch (Exception e){
                    log.debug("Exception while converting file : " + e.getMessage());
                    e.printStackTrace();
                }
        } else{
            log.debug("Recipe image path not found");
        }
        return recipeCommand;
    }

    @Override
    public void UploadImage(MultipartFile file, String recipeId) {
        log.debug("Inside Upload recipe image");

        String fileName = folderPath  + file.getOriginalFilename();
        try {
            Path path = Paths.get(fileName);
            Files.write(path, file.getBytes());

            Recipe recipe = recipeRepository.findById(recipeId).get();
            recipe.setImagePath(fileName);
            Recipe saveRecipe = recipeRepository.save(recipe);
            log.debug("Saved File path is : " + saveRecipe.getImagePath());
        }catch (Exception e){
            log.debug("exception found while writing into file :" + e.getMessage());
            e.printStackTrace();
        }

    }
}
