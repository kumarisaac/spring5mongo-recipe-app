package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService, IngredientToIngredientCommand ingredientToIngredientCommand) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    }

    @GetMapping("recipe/{recipeId}/ingredients")
    public String getIngredientList(@PathVariable String recipeId, Model model){

        log.debug("Inside Get Ingredient List");
        model.addAttribute("recipe",recipeService.findCommandById(recipeId).block());

        log.debug("end of Get Ingredient List");
        return "/recipe/ingredient/list";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{id}/show")
    public String getIngredient(@PathVariable String recipeId,
                                @PathVariable String id,
                                Model model){

        log.debug("start of getting ingredient details");

        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientID(recipeId, id).block());
        log.debug("end of getting ingredient details");
        return "/recipe/ingredient/show";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{id}/update")
    public String updateIngredient(@PathVariable String recipeId,
                                   @PathVariable String id,
                                   Model model){
        log.debug("Inside Update Ingredient");
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientID(recipeId, id).block();
        log.debug("ingredient found is : " + ingredientCommand.toString());
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.getUnitOfMeasure().collect(Collectors.toSet()).block());

        log.debug("End of Update Ingredient");
        return "/recipe/ingredient/ingredientform";

    }
    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdateIngredient(@ModelAttribute IngredientCommand ingredientCommand, @PathVariable String recipeId){
        log.debug("Inside save or Update Ingredient" + ingredientCommand.toString());
        ingredientCommand.setRecipeId(recipeId);
        IngredientCommand command = ingredientService.save(ingredientCommand).block();

        log.debug("Recipe ID is " + command.getRecipeId());
        log.debug("Ingredient ID is " + command.getId());

        log.debug("End of save or Update Ingredient");
        return "redirect:/recipe/" +  command.getRecipeId() + "/ingredients";
    }

    @GetMapping("recipe/{recipeId}/ingredient/new")
    public String addNewIngredient(@PathVariable String recipeId, Model model){

        log.debug("inside Add New Ingredient");
        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId).block();

        IngredientCommand savedIngredient = ingredientToIngredientCommand.convert(new Ingredient()); ;
        savedIngredient.setRecipeId(recipeId);
        savedIngredient.setUnitOfMeasure(new UnitOfMeasureCommand());
        Set<UnitOfMeasureCommand> uomCommands = unitOfMeasureService.getUnitOfMeasure()
                                                                    .collect(Collectors.toSet()).block();
        log.debug("saved Ingredient is : " + savedIngredient.toString());
        model.addAttribute("ingredient", savedIngredient);
        model.addAttribute("uomList", uomCommands);

        log.debug("end of New Ingredient");
        return "recipe/ingredient/ingredientform";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable String recipeId,
                                 @PathVariable String id){

        log.debug("inside Delete Ingredient");
        ingredientService.deleteIngredient(recipeId, id).block();
        log.debug("end of Delete Ingredient");
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}