package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "recipe_ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_ingredients_id_ingredient_seq")
    @SequenceGenerator(sequenceName = "recipe_ingredients_id_ingredient_seq", name="recipe_ingredients_id_ingredient_seq", allocationSize = 1)
    @Column(name="id_ingredient")
    private long idIngredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recipe")
    private Recipe recipe;

    @Column(name = "ingredient_name", length = 30, nullable = false)
    private String ingredientName;

    @Column(name = "ingredient_quantity", length = 50, nullable = false)
    private String ingredientQuantity;

    public Ingredient(Recipe recipe, String ingredientName, String ingredientQuantity) {
        this.recipe = recipe;
        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
    }

    /* default */
    protected Ingredient() {
        // Just for Hibernate
    }

    public long getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(long idIngredient) {
        this.idIngredient = idIngredient;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(String ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return idIngredient == that.idIngredient && recipe.getIdRecipe() == that.recipe.getIdRecipe() && Objects.equals(ingredientName, that.ingredientName) && Objects.equals(ingredientQuantity, that.ingredientQuantity);
    }

}
