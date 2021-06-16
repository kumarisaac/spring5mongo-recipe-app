package guru.springframework.bootstrap;

import guru.springframework.domain.*;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import guru.springframework.repositories.reactive.CategoryReactiveRepository;
import guru.springframework.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Slf4j
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final RecipeRepository recipeRepository;

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @Value("${spring.profiles.active}")
    private String profileName;

    public DataLoader(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository, RecipeRepository recipeRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent)  {
        loadCategories();
        loadUoms();

        log.error("count of UOM : " + unitOfMeasureReactiveRepository.count().block().toString());
        log.error("count of Categories : " + categoryReactiveRepository.count().block().toString());
        recipeReactiveRepository.deleteAll().block();
        loadGuacamoleRecipe();
        loadSpicyChickenRecipe();
        log.error("count of Recipes : " + recipeReactiveRepository.count().block().toString());
    }

    private void loadCategories(){
        categoryReactiveRepository.deleteAll().block();
        List<Category> categories = new ArrayList<>();
        Category american = new Category();
        american.setCategoryName("American");
        categories.add(american);

        Category mexican = new Category();
        mexican.setCategoryName("Mexican");
        categories.add(mexican);

        Category italian = new Category();
        italian.setCategoryName("Italian");
        categories.add(italian);

        Category fastFood = new Category();
        fastFood.setCategoryName("Fast Food");
        categories.add(fastFood);

        categoryReactiveRepository.saveAll(categories).collectList().block();


    }

    private void loadUoms(){
        unitOfMeasureReactiveRepository.deleteAll().block();
        Optional<UnitOfMeasure> teaSpoonOptional = unitOfMeasureReactiveRepository.findByDescription("Teaspoon").blockOptional();
        if(!teaSpoonOptional.isPresent()){
            unitOfMeasureReactiveRepository.save(UnitOfMeasure.builder().description("Teaspoon").build()).block();
        }

        Optional<UnitOfMeasure> tablespoonOptional = unitOfMeasureReactiveRepository.findByDescription("Tablespoon").blockOptional();
        if(!tablespoonOptional.isPresent()){
            unitOfMeasureReactiveRepository.save(UnitOfMeasure.builder().description("Tablespoon").build()).block();
        }

        Optional<UnitOfMeasure> cupOptional = unitOfMeasureReactiveRepository.findByDescription("Cup").blockOptional();
        if(!cupOptional.isPresent()){
            unitOfMeasureReactiveRepository.save(UnitOfMeasure.builder().description("Cup").build()).block();
        }

        Optional<UnitOfMeasure> pinchOptional = unitOfMeasureReactiveRepository.findByDescription("Pinch").blockOptional();
        if(!pinchOptional.isPresent()){
            unitOfMeasureReactiveRepository.save(UnitOfMeasure.builder().description("Pinch").build()).block();
        }

        Optional<UnitOfMeasure> ounceOptional = unitOfMeasureReactiveRepository.findByDescription("Ounce").blockOptional();
        if(!ounceOptional.isPresent()){
            unitOfMeasureReactiveRepository.save(UnitOfMeasure.builder().description("Ounce").build()).block();
        }

        Optional<UnitOfMeasure> serranoOptional = unitOfMeasureReactiveRepository.findByDescription("serrano").blockOptional();
        if(!serranoOptional.isPresent()){
            unitOfMeasureReactiveRepository.save(UnitOfMeasure.builder().description("Serrano").build()).block();
        }

        Optional<UnitOfMeasure> dashOptional = unitOfMeasureReactiveRepository.findByDescription("dash").blockOptional();
        if(!dashOptional.isPresent()){
            unitOfMeasureReactiveRepository.save(UnitOfMeasure.builder().description("Dash").build()).block();
        }

        Optional<UnitOfMeasure> numberOptional = unitOfMeasureReactiveRepository.findByDescription("Number").blockOptional();
        if(!numberOptional.isPresent()){
            unitOfMeasureReactiveRepository.save(UnitOfMeasure.builder().description("Number").build()).block();
        }

    }

    private void loadGuacamoleRecipe() {
        List<Recipe> recipeList = (List<Recipe>) recipeRepository.findByDescriptionLike("Perfect Guacamole");
        if(recipeList.isEmpty()) {

            Optional<Category> americanCategory = categoryRepository.findByCategoryName("American");
            Optional<Category> mexicanCategory = categoryRepository.findByCategoryName("Mexican");
            Optional<Category> italianCategory = categoryRepository.findByCategoryName("Italian");
            Optional<UnitOfMeasure> numberUom = unitOfMeasureRepository.findByDescription("Number");
            Optional<UnitOfMeasure> teaSpoonUom = unitOfMeasureRepository.findByDescription("Teaspoon");
            Optional<UnitOfMeasure> tableSpoonUom = unitOfMeasureRepository.findByDescription("Tablespoon");
            Optional<UnitOfMeasure> dashUom = unitOfMeasureRepository.findByDescription("Dash");
            Optional<UnitOfMeasure> serranoUom = unitOfMeasureRepository.findByDescription("Serrano");

            Recipe guacamole = new Recipe();
            guacamole.getCategories().add(americanCategory.get());
            guacamole.getCategories().add(italianCategory.get());
            guacamole.setDescription("Perfect Guacamole");
            guacamole.setSource("Simply Recipes");
            guacamole.setCookTime(0);
            guacamole.setPrepTime(10);
            guacamole.setServings(4);
            guacamole.setDifficulty(Difficulty.EASY);
            guacamole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
            String directions = "1. Cut the avocado, remove flesh \n" +
                    "2. Mash with a fork \n" +
                    "3. Add salt, lime juice, and the rest \n" +
                    "4. Serve";
            guacamole.setDirections(directions);
            Notes guacamoleNotes = new Notes();
            guacamoleNotes.setNotes("Be careful handling chiles if using. Wash your hands thoroughly " +
                    "after handling and do not touch your eyes or the area near your eyes " +
                    "with your hands for several hours.");
            guacamole.setNotes(guacamoleNotes);
//        guacamoleNotes.setRecipe(guacamole);

            Ingredient avocado = new Ingredient();
            avocado.setDescription("ripe avocados");
            avocado.setAmount(new BigDecimal(1));
            avocado.setUom(numberUom.get());
//        avocado.setRecipe(guacamole);

            Ingredient salt = new Ingredient();
            salt.setAmount(new BigDecimal(0.25));
            salt.setDescription("salt, more to taste");
            salt.setUom(teaSpoonUom.get());
//        salt.setRecipe(guacamole);

            Ingredient lime = new Ingredient();
            lime.setAmount(new BigDecimal(1));
            lime.setDescription("Fresh lime juice or lemon juice");
            lime.setUom(tableSpoonUom.get());
//        lime.setRecipe(guacamole);

            Ingredient onion = new Ingredient();
            onion.setAmount(new BigDecimal(2));
            onion.setDescription("Minced red onion or thinly sliced green onion");
            onion.setUom(tableSpoonUom.get());
//        onion.setRecipe(guacamole);

            Ingredient chilly = new Ingredient();
            chilly.setAmount(new BigDecimal(2));
            chilly.setDescription("Chiles, stems and seeds removed, minced");
            chilly.setUom(serranoUom.get());
//        chilly.setRecipe(guacamole);

            Ingredient pepper = new Ingredient();
            pepper.setAmount(new BigDecimal(1));
            pepper.setDescription(" Freshly grated black pepper");
            pepper.setUom(serranoUom.get());
//        pepper.setRecipe(guacamole);

            Set<Ingredient> ingredientSet = new HashSet<Ingredient>();

            ingredientSet.add(avocado);
            ingredientSet.add(salt);
            ingredientSet.add(lime);
            ingredientSet.add(onion);
            ingredientSet.add(chilly);
            ingredientSet.add(pepper);

            guacamole.setIngredients(ingredientSet);
            guacamole.setImagePath("c:/images/guacamole400x400.jpg");
            try {

                if (!profileName.equals("filestore")) {
                    Path path = Paths.get("c:/images/guacamole400x400.jpg");
                    byte[] imageByte = Files.readAllBytes(path);
                    Byte[] saveImage = new Byte[imageByte.length];
                    int i = 0;
                    for (byte eachByte : imageByte) {
                        saveImage[i++] = eachByte;
                    }
                    guacamole.setImage(saveImage);
                }
            } catch (Exception e) {
                log.debug("exception occurred while uploading file");
            }

            recipeRepository.save(guacamole);

            log.debug("Guacamole recipe loaded successfully");
        }else{
            log.debug("Recipe Guacamole already present");
        }
    }

    private void loadSpicyChickenRecipe() {
        List<Recipe> recipeList = (List<Recipe>) recipeRepository.findByDescriptionLike("Spicy Grilled Chicken Tacos");
        if(recipeList.isEmpty()) {
            Optional<Category> americanCategory = categoryRepository.findByCategoryName("American");
            Optional<Category> mexicanCategory = categoryRepository.findByCategoryName("Mexican");
            Optional<Category> italianCategory = categoryRepository.findByCategoryName("Italian");
            Optional<UnitOfMeasure> numberUom = unitOfMeasureRepository.findByDescription("Number");
            Optional<UnitOfMeasure> teaSpoonUom = unitOfMeasureRepository.findByDescription("Teaspoon");
            Optional<UnitOfMeasure> tableSpoonUom = unitOfMeasureRepository.findByDescription("Tablespoon");
            Optional<UnitOfMeasure> dashUom = unitOfMeasureRepository.findByDescription("Dash");
            Optional<UnitOfMeasure> serranoUom = unitOfMeasureRepository.findByDescription("Serrano");

            Recipe spicyChicken = new Recipe();
            spicyChicken.getCategories().add(americanCategory.get());
            spicyChicken.getCategories().add(mexicanCategory.get());
            spicyChicken.getCategories().add(italianCategory.get());
            spicyChicken.setDescription("Spicy Grilled Chicken Tacos");
            spicyChicken.setSource("Simply Recipes");
            spicyChicken.setCookTime(15);
            spicyChicken.setPrepTime(20);
            spicyChicken.setServings(6);
            spicyChicken.setDifficulty(Difficulty.MODERATE);
            spicyChicken.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
            String directions = "1. Prepare a gas or charcoal grill for medium-high, direct heat \n" +
                    "2. Make the marinade and coat the chicken \n" +
                    "3. Grill the chicken \n" +
                    "4. Warm the tortillas \n" +
                    "5. Assemble the tacos";
            spicyChicken.setDirections(directions);
            Notes guacamoleNotes = new Notes();
            guacamoleNotes.setNotes("Look for ancho chile powder with the Mexican ingredients at your grocery store, " +
                    "on buy it online. (If you can't find ancho chili powder, you replace the ancho chili, the oregano, " +
                    "and the cumin with 2 1/2 tablespoons regular chili powder, though the flavor won't be quite the same.");
            spicyChicken.setNotes(guacamoleNotes);
//        guacamoleNotes.setRecipe(spicyChicken);

            Ingredient cPowder = new Ingredient();
            cPowder.setAmount(new BigDecimal(2));
            cPowder.setDescription("Ancho chili powder");
            cPowder.setUom(tableSpoonUom.get());
//        cPowder.setRecipe(spicyChicken);

            Ingredient oregano = new Ingredient();
            oregano.setAmount(new BigDecimal(1));
            oregano.setDescription("Dried oregano");
            oregano.setUom(teaSpoonUom.get());
//        oregano.setRecipe(spicyChicken);

            Ingredient cumin = new Ingredient();
            cumin.setAmount(new BigDecimal(1));
            cumin.setDescription("Dried cumin");
            cumin.setUom(teaSpoonUom.get());
//        cumin.setRecipe(spicyChicken);

            Ingredient sugar = new Ingredient();
            sugar.setAmount(new BigDecimal(1));
            sugar.setDescription("Sugar");
            sugar.setUom(teaSpoonUom.get());
//        sugar.setRecipe(spicyChicken);

            Ingredient salt = new Ingredient();
            salt.setAmount(new BigDecimal(0.5));
            salt.setDescription("Salt, more to taste");
            salt.setUom(teaSpoonUom.get());
//        salt.setRecipe(spicyChicken);

            Ingredient garlic = new Ingredient();
            garlic.setAmount(new BigDecimal(1));
            garlic.setDescription("garlic, finely chopped");
            garlic.setUom(numberUom.get());
//        garlic.setRecipe(spicyChicken);

            Ingredient orange = new Ingredient();
            orange.setAmount(new BigDecimal(1));
            orange.setDescription("Finely grated orange zest");
            orange.setUom(tableSpoonUom.get());
//        orange .setRecipe(spicyChicken);

            Ingredient olive = new Ingredient();
            olive.setAmount(new BigDecimal(2));
            olive.setDescription("2  olive oil");
            olive.setUom(tableSpoonUom.get());
//        olive.setRecipe(spicyChicken);

            Ingredient chicken = new Ingredient();
            chicken.setAmount(new BigDecimal(6));
            chicken.setDescription("Skinless, boneless chicken thighs (1 1/4 pounds)");
            chicken.setUom(numberUom.get());
//        chicken.setRecipe(spicyChicken);

            Set<Ingredient> ingredientSet = new HashSet<Ingredient>();

            ingredientSet.add(cPowder);
            ingredientSet.add(oregano);
            ingredientSet.add(cumin);
            ingredientSet.add(sugar);
            ingredientSet.add(salt);
            ingredientSet.add(garlic);
            ingredientSet.add(orange);
            ingredientSet.add(olive);
            ingredientSet.add(chicken);

            spicyChicken.setImagePath("c:/images/guacamole400x400.jpg");
            try {

                if (!profileName.equals("filestore")) {
                    Path path = Paths.get("c:/images/tacos400x400.jpg");
                    byte[] imageByte = Files.readAllBytes(path);
                    Byte[] saveImage = new Byte[imageByte.length];
                    int i = 0;
                    for (byte eachByte : imageByte) {
                        saveImage[i++] = eachByte;
                    }
                    spicyChicken.setImage(saveImage);
                }
            } catch (Exception e) {
                log.debug("exception occured while uploading file");
            }

            spicyChicken.setIngredients(ingredientSet);

            recipeRepository.save(spicyChicken);

            log.debug("Spicy Grilled Chicken recipe loaded successfully");
        } else{
            log.debug("Recipe Spicy Grilled chicken already present");
        }
    }
}
