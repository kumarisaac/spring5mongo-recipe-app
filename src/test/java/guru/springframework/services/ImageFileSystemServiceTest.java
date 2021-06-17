package guru.springframework.services;


import guru.springframework.commands.CategoryCommand;
import guru.springframework.commands.NotesCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.*;
import guru.springframework.domain.*;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
public class ImageFileSystemServiceTest {

    private String imagePath;

    @BeforeEach
    private void setup(){
        ClassLoader classLoader = ImageFileSystemService.class.getClassLoader();
        imagePath = classLoader.getResource("static/images/tacos400x400.jpg").getPath();
        char imageArray[] = imagePath.toCharArray();
        char newArray[] = Arrays.copyOfRange(imageArray, 1, imageArray.length);

        imagePath = new String(newArray);

        log.debug(imagePath);
    }



    public void testGetRecipeImage() {
        Recipe recipe = new Recipe();
        recipe.setDirections("Directions");
        recipe.setIngredients(new HashSet<Ingredient>());
        recipe.setCategories(new HashSet<Category>());
        recipe.setServings(1);
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setDescription("The characteristics of someone or something");
        recipe.setSource("Source");
        recipe.setImage(new Byte[]{'A'});
        recipe.setId("42");
        recipe.setUrl("https://example.org/example");
        recipe.setCookTime(1);
        recipe.setNotes(new Notes());
        recipe.setPrepTime(1);
        recipe.setImagePath(imagePath);
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        when(recipeRepository.findById(anyString())).thenReturn(Optional.<Recipe>of(recipe));
        CategoryToCategoryCommand categoryConveter = new CategoryToCategoryCommand();
        IngredientToIngredientCommand ingredientConverter = new IngredientToIngredientCommand(
                new UnitOfMeasureToUnitOfMeasureCommand());
        RecipeCommand actualRecipeImage = (new ImageFileSystemService(recipeRepository,
                new RecipeToRecipeCommand(categoryConveter, ingredientConverter, new NotesToNotesCommand())))
                .getRecipeImage("42");
        assertTrue(actualRecipeImage.getCategories().isEmpty());
        assertEquals("https://example.org/example", actualRecipeImage.getUrl());
        assertEquals("Source", actualRecipeImage.getSource());
        assertEquals(1, actualRecipeImage.getServings().intValue());
        assertEquals(1, actualRecipeImage.getPrepTime().intValue());
        assertTrue(actualRecipeImage.getIngredients().isEmpty());
        assertEquals(1, actualRecipeImage.getCookTime().intValue());
        assertEquals(Difficulty.EASY, actualRecipeImage.getDifficulty());
        assertEquals(imagePath, actualRecipeImage.getImagePath());
        assertEquals(67816, actualRecipeImage.getImage().length);
        assertEquals("42", actualRecipeImage.getId());
        assertEquals("Directions", actualRecipeImage.getDirections());
        assertEquals("The characteristics of someone or something", actualRecipeImage.getDescription());
        NotesCommand notes = actualRecipeImage.getNotes();
        assertNull(notes.getId());
        assertNull(notes.getNotes());
        verify(recipeRepository).findById(anyString());
    }


    public void testGetRecipeImage2() {
        HashSet<Ingredient> ingredientSet = new HashSet<Ingredient>();
        ingredientSet.add(new Ingredient());

        Recipe recipe = new Recipe();
        recipe.setDirections("Directions");
        recipe.setIngredients(ingredientSet);
        recipe.setCategories(new HashSet<Category>());
        recipe.setServings(1);
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setDescription("The characteristics of someone or something");
        recipe.setSource("Source");
        recipe.setImage(new Byte[]{'A'});
        recipe.setId("42");
        recipe.setUrl("https://example.org/example");
        recipe.setCookTime(1);
        recipe.setNotes(new Notes());
        recipe.setPrepTime(1);
        recipe.setImagePath(imagePath);
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        when(recipeRepository.findById(anyString())).thenReturn(Optional.<Recipe>of(recipe));
        CategoryToCategoryCommand categoryConveter = new CategoryToCategoryCommand();
        IngredientToIngredientCommand ingredientConverter = new IngredientToIngredientCommand(
                new UnitOfMeasureToUnitOfMeasureCommand());
        RecipeCommand actualRecipeImage = (new ImageFileSystemService(recipeRepository,
                new RecipeToRecipeCommand(categoryConveter, ingredientConverter, new NotesToNotesCommand())))
                .getRecipeImage("42");
        assertTrue(actualRecipeImage.getCategories().isEmpty());
        assertEquals("https://example.org/example", actualRecipeImage.getUrl());
        assertEquals("Source", actualRecipeImage.getSource());
        assertEquals(1, actualRecipeImage.getServings().intValue());
        assertEquals(1, actualRecipeImage.getPrepTime().intValue());
        assertEquals(1, actualRecipeImage.getIngredients().size());
        assertEquals(1, actualRecipeImage.getCookTime().intValue());
        assertEquals(Difficulty.EASY, actualRecipeImage.getDifficulty());
        assertEquals(imagePath, actualRecipeImage.getImagePath());
        assertEquals(67816, actualRecipeImage.getImage().length);
        assertEquals("42", actualRecipeImage.getId());
        assertEquals("Directions", actualRecipeImage.getDirections());
        assertEquals("The characteristics of someone or something", actualRecipeImage.getDescription());
        NotesCommand notes = actualRecipeImage.getNotes();
        assertNull(notes.getId());
        assertNull(notes.getNotes());
        verify(recipeRepository).findById(anyString());
    }


    public void testGetRecipeImage3() {
        HashSet<Ingredient> ingredientSet = new HashSet<Ingredient>();
        ingredientSet.add(new Ingredient());
        ingredientSet.add(new Ingredient());

        Recipe recipe = new Recipe();
        recipe.setDirections("Directions");
        recipe.setIngredients(ingredientSet);
        recipe.setCategories(new HashSet<Category>());
        recipe.setServings(1);
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setDescription("The characteristics of someone or something");
        recipe.setSource("Source");
        recipe.setImage(new Byte[]{'A'});
        recipe.setId("42");
        recipe.setUrl("https://example.org/example");
        recipe.setCookTime(1);
        recipe.setNotes(new Notes());
        recipe.setPrepTime(1);
        recipe.setImagePath(imagePath);
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        when(recipeRepository.findById(anyString())).thenReturn(Optional.<Recipe>of(recipe));
        CategoryToCategoryCommand categoryConveter = new CategoryToCategoryCommand();
        IngredientToIngredientCommand ingredientConverter = new IngredientToIngredientCommand(
                new UnitOfMeasureToUnitOfMeasureCommand());
        RecipeCommand actualRecipeImage = (new ImageFileSystemService(recipeRepository,
                new RecipeToRecipeCommand(categoryConveter, ingredientConverter, new NotesToNotesCommand())))
                .getRecipeImage("42");
        assertTrue(actualRecipeImage.getCategories().isEmpty());
        assertEquals("https://example.org/example", actualRecipeImage.getUrl());
        assertEquals("Source", actualRecipeImage.getSource());
        assertEquals(1, actualRecipeImage.getServings().intValue());
        assertEquals(1, actualRecipeImage.getPrepTime().intValue());
        assertEquals(2, actualRecipeImage.getIngredients().size());
        assertEquals(1, actualRecipeImage.getCookTime().intValue());
        assertEquals(Difficulty.EASY, actualRecipeImage.getDifficulty());
        assertEquals(imagePath, actualRecipeImage.getImagePath());
        assertEquals(67816, actualRecipeImage.getImage().length);
        assertEquals("42", actualRecipeImage.getId());
        assertEquals("Directions", actualRecipeImage.getDirections());
        assertEquals("The characteristics of someone or something", actualRecipeImage.getDescription());
        NotesCommand notes = actualRecipeImage.getNotes();
        assertNull(notes.getId());
        assertNull(notes.getNotes());
        verify(recipeRepository).findById(anyString());
    }


    public void testGetRecipeImage4() {
        Category category = new Category();
        category.setCategoryName("Inside Get recipe image");
        category.setRecipeSet(null);
        category.setId("42");

        HashSet<Category> categorySet = new HashSet<Category>();
        categorySet.add(category);

        Recipe recipe = new Recipe();
        recipe.setDirections("Directions");
        recipe.setIngredients(new HashSet<Ingredient>());
        recipe.setCategories(categorySet);
        recipe.setServings(1);
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setDescription("The characteristics of someone or something");
        recipe.setSource("Source");
        recipe.setImage(new Byte[]{'A'});
        recipe.setId("42");
        recipe.setUrl("https://example.org/example");
        recipe.setCookTime(1);
        recipe.setNotes(new Notes());
        recipe.setPrepTime(1);
        recipe.setImagePath(imagePath);
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        when(recipeRepository.findById(anyString())).thenReturn(Optional.<Recipe>of(recipe));
        CategoryToCategoryCommand categoryConveter = new CategoryToCategoryCommand();
        IngredientToIngredientCommand ingredientConverter = new IngredientToIngredientCommand(
                new UnitOfMeasureToUnitOfMeasureCommand());
        RecipeCommand actualRecipeImage = (new ImageFileSystemService(recipeRepository,
                new RecipeToRecipeCommand(categoryConveter, ingredientConverter, new NotesToNotesCommand())))
                .getRecipeImage("42");
        List<CategoryCommand> categories = actualRecipeImage.getCategories();
        assertEquals(1, categories.size());
        assertEquals("https://example.org/example", actualRecipeImage.getUrl());
        assertEquals(1, actualRecipeImage.getCookTime().intValue());
        assertEquals("Source", actualRecipeImage.getSource());
        assertEquals(1, actualRecipeImage.getServings().intValue());
        assertEquals(1, actualRecipeImage.getPrepTime().intValue());
        assertTrue(actualRecipeImage.getIngredients().isEmpty());
        assertEquals(Difficulty.EASY, actualRecipeImage.getDifficulty());
        assertEquals(imagePath, actualRecipeImage.getImagePath());
        assertEquals("Directions", actualRecipeImage.getDirections());
        assertEquals(67816, actualRecipeImage.getImage().length);
        assertEquals("42", actualRecipeImage.getId());
        assertEquals("The characteristics of someone or something", actualRecipeImage.getDescription());
        NotesCommand notes = actualRecipeImage.getNotes();
        assertNull(notes.getNotes());
        assertNull(notes.getId());
        CategoryCommand getResult = categories.get(0);
        assertEquals("Inside Get recipe image", getResult.getCategoryName());
        assertEquals("42", getResult.getId());
        verify(recipeRepository).findById(anyString());
    }


    public void testGetRecipeImage5() {
        Category category = new Category();
        category.setCategoryName("Inside Get recipe image");
        category.setRecipeSet(null);
        category.setId("42");

        Category category1 = new Category();
        category1.setCategoryName("Inside Get recipe image");
        category1.setRecipeSet(null);
        category1.setId("42");

        HashSet<Category> categorySet = new HashSet<Category>();
        categorySet.add(category1);
        categorySet.add(category);

        Recipe recipe = new Recipe();
        recipe.setDirections("Directions");
        recipe.setIngredients(new HashSet<Ingredient>());
        recipe.setCategories(categorySet);
        recipe.setServings(1);
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setDescription("The characteristics of someone or something");
        recipe.setSource("Source");
        recipe.setImage(new Byte[]{'A'});
        recipe.setId("42");
        recipe.setUrl("https://example.org/example");
        recipe.setCookTime(1);
        recipe.setNotes(new Notes());
        recipe.setPrepTime(1);
        recipe.setImagePath(imagePath);
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        when(recipeRepository.findById(anyString())).thenReturn(Optional.<Recipe>of(recipe));
        CategoryToCategoryCommand categoryConveter = new CategoryToCategoryCommand();
        IngredientToIngredientCommand ingredientConverter = new IngredientToIngredientCommand(
                new UnitOfMeasureToUnitOfMeasureCommand());
        RecipeCommand actualRecipeImage = (new ImageFileSystemService(recipeRepository,
                new RecipeToRecipeCommand(categoryConveter, ingredientConverter, new NotesToNotesCommand())))
                .getRecipeImage("42");
        List<CategoryCommand> categories = actualRecipeImage.getCategories();
        assertEquals(2, categories.size());
        assertEquals("https://example.org/example", actualRecipeImage.getUrl());
        assertEquals(1, actualRecipeImage.getCookTime().intValue());
        assertEquals(Difficulty.EASY, actualRecipeImage.getDifficulty());
        assertEquals("Source", actualRecipeImage.getSource());
        assertEquals(1, actualRecipeImage.getServings().intValue());
        assertEquals(1, actualRecipeImage.getPrepTime().intValue());
        assertEquals(imagePath, actualRecipeImage.getImagePath());
        assertTrue(actualRecipeImage.getIngredients().isEmpty());
        assertEquals("Directions", actualRecipeImage.getDirections());
        assertEquals("The characteristics of someone or something", actualRecipeImage.getDescription());
        assertEquals(67816, actualRecipeImage.getImage().length);
        assertEquals("42", actualRecipeImage.getId());
        NotesCommand notes = actualRecipeImage.getNotes();
        assertNull(notes.getNotes());
        assertNull(notes.getId());
        CategoryCommand getResult = categories.get(1);
        assertEquals("42", getResult.getId());
        assertEquals("Inside Get recipe image", getResult.getCategoryName());
        CategoryCommand getResult1 = categories.get(0);
        assertEquals("Inside Get recipe image", getResult1.getCategoryName());
        assertEquals("42", getResult1.getId());
        verify(recipeRepository).findById(anyString());
    }


    public void testGetRecipeImage6() {
        Recipe recipe = new Recipe();
        recipe.setDirections("Directions");
        recipe.setIngredients(new HashSet<Ingredient>());
        recipe.setCategories(new HashSet<Category>());
        recipe.setServings(1);
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setDescription("The characteristics of someone or something");
        recipe.setSource("Source");
        recipe.setImage(new Byte[]{'A'});
        recipe.setId("42");
        recipe.setUrl("https://example.org/example");
        recipe.setCookTime(1);
        recipe.setNotes(null);
        recipe.setPrepTime(1);
        recipe.setImagePath(imagePath);
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        when(recipeRepository.findById(anyString())).thenReturn(Optional.<Recipe>of(recipe));
        CategoryToCategoryCommand categoryConveter = new CategoryToCategoryCommand();
        IngredientToIngredientCommand ingredientConverter = new IngredientToIngredientCommand(
                new UnitOfMeasureToUnitOfMeasureCommand());
        RecipeCommand actualRecipeImage = (new ImageFileSystemService(recipeRepository,
                new RecipeToRecipeCommand(categoryConveter, ingredientConverter, new NotesToNotesCommand())))
                .getRecipeImage("42");
        assertTrue(actualRecipeImage.getCategories().isEmpty());
        assertEquals("https://example.org/example", actualRecipeImage.getUrl());
        assertEquals("Source", actualRecipeImage.getSource());
        assertEquals(1, actualRecipeImage.getServings().intValue());
        assertEquals(1, actualRecipeImage.getPrepTime().intValue());
        assertNull(actualRecipeImage.getNotes());
        assertTrue(actualRecipeImage.getIngredients().isEmpty());
        assertEquals(imagePath, actualRecipeImage.getImagePath());
        assertEquals(67816, actualRecipeImage.getImage().length);
        assertEquals("42", actualRecipeImage.getId());
        assertEquals("Directions", actualRecipeImage.getDirections());
        assertEquals(Difficulty.EASY, actualRecipeImage.getDifficulty());
        assertEquals("The characteristics of someone or something", actualRecipeImage.getDescription());
        assertEquals(1, actualRecipeImage.getCookTime().intValue());
        verify(recipeRepository).findById(anyString());
    }


    public void testGetRecipeImage7() {
        Notes notes = mock(Notes.class);
        when(notes.getNotes()).thenReturn("foo");
        when(notes.getId()).thenReturn("foo");

        Recipe recipe = new Recipe();
        recipe.setDirections("Directions");
        recipe.setIngredients(new HashSet<Ingredient>());
        recipe.setCategories(new HashSet<Category>());
        recipe.setServings(1);
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setDescription("The characteristics of someone or something");
        recipe.setSource("Source");
        recipe.setImage(new Byte[]{'A'});
        recipe.setId("42");
        recipe.setUrl("https://example.org/example");
        recipe.setCookTime(1);
        recipe.setNotes(notes);
        recipe.setPrepTime(1);
        recipe.setImagePath(imagePath);
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        when(recipeRepository.findById(anyString())).thenReturn(Optional.<Recipe>of(recipe));
        CategoryToCategoryCommand categoryConveter = new CategoryToCategoryCommand();
        IngredientToIngredientCommand ingredientConverter = new IngredientToIngredientCommand(
                new UnitOfMeasureToUnitOfMeasureCommand());
        RecipeCommand actualRecipeImage = (new ImageFileSystemService(recipeRepository,
                new RecipeToRecipeCommand(categoryConveter, ingredientConverter, new NotesToNotesCommand())))
                .getRecipeImage("42");
        assertTrue(actualRecipeImage.getCategories().isEmpty());
        assertEquals("https://example.org/example", actualRecipeImage.getUrl());
        assertEquals("Source", actualRecipeImage.getSource());
        assertEquals(1, actualRecipeImage.getServings().intValue());
        assertEquals(1, actualRecipeImage.getPrepTime().intValue());
        assertTrue(actualRecipeImage.getIngredients().isEmpty());
        assertEquals(1, actualRecipeImage.getCookTime().intValue());
        assertEquals(Difficulty.EASY, actualRecipeImage.getDifficulty());
        assertEquals(imagePath, actualRecipeImage.getImagePath());
        assertEquals(67816, actualRecipeImage.getImage().length);
        assertEquals("42", actualRecipeImage.getId());
        assertEquals("Directions", actualRecipeImage.getDirections());
        assertEquals("The characteristics of someone or something", actualRecipeImage.getDescription());
        NotesCommand notes1 = actualRecipeImage.getNotes();
        assertEquals("foo", notes1.getId());
        assertEquals("foo", notes1.getNotes());
        verify(recipeRepository).findById(anyString());
        verify(notes).getId();
        verify(notes).getNotes();
    }


    public void testGetRecipeImage8() {
        Notes notes = mock(Notes.class);
        when(notes.getNotes()).thenReturn("foo");
        when(notes.getId()).thenReturn("foo");

        Recipe recipe = new Recipe();
        recipe.setDirections("Directions");
        recipe.setIngredients(new HashSet<Ingredient>());
        recipe.setCategories(new HashSet<Category>());
        recipe.setServings(1);
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setDescription("The characteristics of someone or something");
        recipe.setSource("Source");
        recipe.setImage(new Byte[]{'A'});
        recipe.setId("42");
        recipe.setUrl("https://example.org/example");
        recipe.setCookTime(1);
        recipe.setNotes(notes);
        recipe.setPrepTime(1);
        recipe.setImagePath(null);
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        when(recipeRepository.findById(anyString())).thenReturn(Optional.<Recipe>of(recipe));
        CategoryToCategoryCommand categoryConveter = new CategoryToCategoryCommand();
        IngredientToIngredientCommand ingredientConverter = new IngredientToIngredientCommand(
                new UnitOfMeasureToUnitOfMeasureCommand());
        RecipeCommand actualRecipeImage = (new ImageFileSystemService(recipeRepository,
                new RecipeToRecipeCommand(categoryConveter, ingredientConverter, new NotesToNotesCommand())))
                .getRecipeImage("42");
        assertTrue(actualRecipeImage.getCategories().isEmpty());
        assertEquals("https://example.org/example", actualRecipeImage.getUrl());
        assertEquals("Source", actualRecipeImage.getSource());
        assertEquals(1, actualRecipeImage.getServings().intValue());
        assertEquals(1, actualRecipeImage.getPrepTime().intValue());
        assertTrue(actualRecipeImage.getIngredients().isEmpty());
        assertEquals(1, actualRecipeImage.getCookTime().intValue());
        assertEquals(Difficulty.EASY, actualRecipeImage.getDifficulty());
        assertNull(actualRecipeImage.getImagePath());
        assertEquals(1, actualRecipeImage.getImage().length);
        assertEquals("42", actualRecipeImage.getId());
        assertEquals("Directions", actualRecipeImage.getDirections());
        assertEquals("The characteristics of someone or something", actualRecipeImage.getDescription());
        NotesCommand notes1 = actualRecipeImage.getNotes();
        assertEquals("foo", notes1.getId());
        assertEquals("foo", notes1.getNotes());
        verify(recipeRepository).findById(anyString());
        verify(notes).getId();
        verify(notes).getNotes();
    }


    public void testGetRecipeImage9() {
        Notes notes = mock(Notes.class);
        when(notes.getNotes()).thenReturn("foo");
        when(notes.getId()).thenReturn("foo");

        Recipe recipe = new Recipe();
        recipe.setDirections("Directions");
        recipe.setIngredients(new HashSet<Ingredient>());
        recipe.setCategories(new HashSet<Category>());
        recipe.setServings(1);
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setDescription("The characteristics of someone or something");
        recipe.setSource("Source");
        recipe.setImage(new Byte[]{'A'});
        recipe.setId("42");
        recipe.setUrl("https://example.org/example");
        recipe.setCookTime(1);
        recipe.setNotes(notes);
        recipe.setPrepTime(1);
        recipe.setImagePath("Exception while converting file : ");
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        when(recipeRepository.findById(anyString())).thenReturn(Optional.<Recipe>of(recipe));
        CategoryToCategoryCommand categoryConveter = new CategoryToCategoryCommand();
        IngredientToIngredientCommand ingredientConverter = new IngredientToIngredientCommand(
                new UnitOfMeasureToUnitOfMeasureCommand());
        RecipeCommand actualRecipeImage = (new ImageFileSystemService(recipeRepository,
                new RecipeToRecipeCommand(categoryConveter, ingredientConverter, new NotesToNotesCommand())))
                .getRecipeImage("42");
        assertTrue(actualRecipeImage.getCategories().isEmpty());
        assertEquals("https://example.org/example", actualRecipeImage.getUrl());
        assertEquals("Source", actualRecipeImage.getSource());
        assertEquals(1, actualRecipeImage.getServings().intValue());
        assertEquals(1, actualRecipeImage.getPrepTime().intValue());
        assertTrue(actualRecipeImage.getIngredients().isEmpty());
        assertEquals(1, actualRecipeImage.getCookTime().intValue());
        assertEquals(Difficulty.EASY, actualRecipeImage.getDifficulty());
        assertEquals("Exception while converting file : ", actualRecipeImage.getImagePath());
        assertEquals(1, actualRecipeImage.getImage().length);
        assertEquals("42", actualRecipeImage.getId());
        assertEquals("Directions", actualRecipeImage.getDirections());
        assertEquals("The characteristics of someone or something", actualRecipeImage.getDescription());
        NotesCommand notes1 = actualRecipeImage.getNotes();
        assertEquals("foo", notes1.getId());
        assertEquals("foo", notes1.getNotes());
        verify(recipeRepository).findById(anyString());
        verify(notes).getId();
        verify(notes).getNotes();
    }


    public void testUploadImage() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        CategoryToCategoryCommand categoryConveter = new CategoryToCategoryCommand();
        IngredientToIngredientCommand ingredientConverter = new IngredientToIngredientCommand(
                new UnitOfMeasureToUnitOfMeasureCommand());
        ImageFileSystemService imageFileSystemService = new ImageFileSystemService(recipeRepository,
                new RecipeToRecipeCommand(categoryConveter, ingredientConverter, new NotesToNotesCommand()));
        imageFileSystemService.UploadImage(
                new MockMultipartFile("Name", new ByteArrayInputStream("AAAAAAAAAAAAAAAAAAAAAAAA".getBytes("UTF-8"))), "42");
    }


    public void testUploadImage2() {
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        CategoryToCategoryCommand categoryConveter = new CategoryToCategoryCommand();
        IngredientToIngredientCommand ingredientConverter = new IngredientToIngredientCommand(
                new UnitOfMeasureToUnitOfMeasureCommand());
        ImageFileSystemService imageFileSystemService = new ImageFileSystemService(recipeRepository,
                new RecipeToRecipeCommand(categoryConveter, ingredientConverter, new NotesToNotesCommand()));
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("foo");
        imageFileSystemService.UploadImage(multipartFile, "42");
        verify(multipartFile).getOriginalFilename();
    }

    public void testUploadImage3() {
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        CategoryToCategoryCommand categoryConveter = new CategoryToCategoryCommand();
        IngredientToIngredientCommand ingredientConverter = new IngredientToIngredientCommand(
                new UnitOfMeasureToUnitOfMeasureCommand());
        ImageFileSystemService imageFileSystemService = new ImageFileSystemService(recipeRepository,
                new RecipeToRecipeCommand(categoryConveter, ingredientConverter, new NotesToNotesCommand()));
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("exception found while writing into file :");
        imageFileSystemService.UploadImage(multipartFile, "42");
        verify(multipartFile).getOriginalFilename();
    }
}

