package guru.springframework.controllers;

import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@Controller
public class IndexController {

    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping({"/"," ","/index", "/index.html"})
    public String getRecipeList(Model model){

        log.debug("Start of getRecipeList Page Method with in Recipe controller");
        model.addAttribute("recipes",recipeService.getRecipes());
        log.debug("End of getRecipeList Page Method with in Recipe controller");
        return "index";
    }
}
