package guru.springframework.commands;

import guru.springframework.domain.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand {

    private String id;

    @NotBlank
    @Size(min = 3, max = 255)
    @Pattern(regexp = "[a-zA-Z0-9 ]*")
    private String description;

    @Min(1)
    @Max(999)
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @NotNull
    private Integer prepTime;

    @Min(1)
    @Max(999)
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @NotNull
    private Integer cookTime;

    @Min(1)
    @Max(999)
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @NotNull
    private Integer servings;

    @Size(min = 10, max = 255)
    private String source;


    private String url;

    @Size(min = 10, max = 255)
    @NotBlank
    private String directions;
    private Set<IngredientCommand> ingredients = new HashSet<>();
    private Byte[] image;
    private String imagePath;
    private NotesCommand notes;
    private Difficulty difficulty;
    private Set<CategoryCommand> categories = new HashSet<>();
}
