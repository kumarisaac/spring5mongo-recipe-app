package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.CategoryService;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.stream.Collectors;


@Slf4j
@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private final ImageService imageService;
    private final CategoryService categoryService;

    public RecipeController(RecipeService recipeService, ImageService imageService, CategoryService categoryService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
        this.categoryService = categoryService;
    }

    @GetMapping({"/recipe/{id}/show"})
    public String getRecipe(@PathVariable String id, Model model){

       model.addAttribute("recipe", recipeService.findById(id).block());
       return "recipe/show";
    }

    @GetMapping("recipe/new")
    public String saveRecipe(Model model){
        model.addAttribute("recipe", new RecipeCommand());
        model.addAttribute("categoryList", categoryService.getAllCategories()
                                               .collect(Collectors.toSet())
                                                .block());
        return "recipe/recipeform";
    }

    @PostMapping("recipe")
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand recipeCommand,
                               BindingResult result){
        log.debug("inside saveorUpdate method");
        if(result.hasErrors()){
            result.getAllErrors().forEach(error -> log.debug(error.toString()));

            return "recipe/recipeform";
        }

        RecipeCommand saveRecipe = recipeService.saveRecipeCommand(recipeCommand).block();

        log.debug("end of saveorUpdate method");
        return "redirect:/recipe/" + saveRecipe.getId()+"/show";
    }

    @GetMapping("recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model){
        log.debug("inside Update Recipe");

        RecipeCommand command = recipeService.findCommandById(id).block();
        model.addAttribute("recipe",command);
        model.addAttribute("categoryList", categoryService.getAllCategories()
                                                            .collect(Collectors.toSet())
                                                            .block());
        log.debug("End of Update Recipe");
        return "/recipe/recipeform";
    }

    @GetMapping("recipe/{id}/delete")
    public String deleteRecipe(@PathVariable String id, Model model){

        log.debug("Deleting recipe id: " + id);
        recipeService.deleteById(id).block();
        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
        public ModelAndView NotFoundHandler(Exception exception){

        log.error("Not found exception");
        log.error(exception.getMessage());
        //log.error(exception.getStackTrace().toString());
        ModelAndView mv = new ModelAndView();
        mv.setViewName("404error");
        mv.addObject("exception", exception);
        return mv;
    }

}
