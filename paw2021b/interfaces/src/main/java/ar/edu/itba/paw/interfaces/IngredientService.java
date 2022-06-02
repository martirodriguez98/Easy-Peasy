package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Ingredient;
import ar.edu.itba.paw.models.Recipe;

import java.util.List;
import java.util.Optional;

public interface IngredientService {
    Optional<Ingredient> findById(long id);
    List<Ingredient> findByRecipeId(Recipe recipe);
    Ingredient create(Recipe recipe, String ingredientName, String ingredientQuantity );

}
