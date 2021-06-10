package guru.springframework.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(exclude = {"recipe", "uom"})
public class Ingredient {


    private String id;
    private String description;
    private BigDecimal amount;

    private Recipe recipe;


    private UnitOfMeasure uom;

}
