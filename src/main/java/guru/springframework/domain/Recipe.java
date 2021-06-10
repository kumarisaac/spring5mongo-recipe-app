package guru.springframework.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"ingredients", "notes", "categories"})
@ToString(exclude = {"ingredients", "notes", "categories"})
public class Recipe{

    private String id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String Source;
    private String url;
    private String directions;

    private Set<Ingredient> ingredients = new HashSet<>();

    private Byte[] image;

    private String imagePath;

    private Notes notes;

    private Difficulty difficulty;

    private Set<Category> categories = new HashSet<Category>();

    public Recipe addIngredient(Ingredient ingredient){
        ingredient.setRecipe(this);
        this.ingredients.add(ingredient);
        return this;
    }
}
