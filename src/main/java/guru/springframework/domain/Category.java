package guru.springframework.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category{

    private String id;
    private String categoryName;

    private Set<Recipe> recipeSet = new HashSet<Recipe>();

       public String toString() {
        return "Category(id=" + this.getId() + ", categoryName=" + this.getCategoryName() +  ")";
    }
}
