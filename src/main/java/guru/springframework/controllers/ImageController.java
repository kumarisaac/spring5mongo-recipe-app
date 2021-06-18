package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


@Slf4j
@Controller
public class ImageController {

    private final RecipeService recipeService;
    private final ImageService imageService;

    public ImageController(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    @GetMapping("/recipe/{recipeId}/getimage")
    public void getImage(@PathVariable String recipeId, HttpServletResponse response) throws IOException {
        log.debug("inside Get Image method");
        RecipeCommand recipeCommand = imageService.getRecipeImage(recipeId).block();

        if(recipeCommand.getImage() != null){
            byte[] imageBytes = new byte[recipeCommand.getImage().length];
            int i = 0;
            for (Byte wrappedByte : recipeCommand.getImage()){
                imageBytes[i++] = wrappedByte.byteValue();
            }
            response.setContentType("image/jpeg");
            InputStream inputStream = new ByteArrayInputStream(imageBytes);
            IOUtils.copy(inputStream, response.getOutputStream());
        }
        log.debug("End of Get Image method");
    }

    @GetMapping("/recipe/{recipeId}/uploadimage")
    public String uploadImage(@PathVariable String recipeId, Model model){

        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId).block();

        model.addAttribute("recipe", recipeCommand);

        return "/recipe/uploadimageform";
    }

    @PostMapping("/recipe/{recipeId}/saveimage")
    public String saveImage(@PathVariable String recipeId, @RequestParam("imagefile") MultipartFile file){

        log.debug("inside Save image method");

        imageService.UploadImage(file, recipeId).block();

        log.debug("end of Save image method");
        return "redirect:/recipe/" + recipeId + "/show";
    }

}
