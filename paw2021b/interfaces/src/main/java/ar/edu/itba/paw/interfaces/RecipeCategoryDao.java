package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Recipe;
import ar.edu.itba.paw.models.RecipeCategory;

import java.util.List;

public interface RecipeCategoryDao {
     List<RecipeCategory> findByRecipeId(Recipe recipe);
     RecipeCategory create(Recipe recipe, int category);
     List<RecipeCategory> findAllCategories();
}
