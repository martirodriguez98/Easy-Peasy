package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RecipeService {
    Optional<Recipe> findById(long id);

    Recipe createRecipe(User user, String recipeTitle, String recipeDescription, List<String> recipeIngredients, Integer recipeTime, String recipeSteps, List<ImageUser> imagesToUpload, Integer recipeDifficulty, List<Integer> recipeCategories);

    List<Recipe> findAllRecipes();

    void likeRecipe(Recipe recipe, User user);

    void dislikeRecipe(Recipe recipe, User user);

    long getLikes(Recipe recipe);

    long getDislikes(Recipe recipe);

    boolean isLiked(Recipe recipe, User user);

    boolean isDisliked(Recipe recipe, User user);

    double likePercentage(Recipe recipe);

    void favRecipe(Recipe recipe, User user);

    void unfavRecipe(Recipe recipe, User user);

    boolean isFavorited(Recipe recipe, User user);

    void delete(Recipe recipe, User user);

    Recipe manageRecipeCreation(BindingResult errors, User user, String recipeTitle, String recipeDescription, List<String> recipeIngredients, Integer recipeTime, String recipeSteps, List<ImageUser> imagesToUpload, Integer recipeDifficulty, List<Integer> recipeCategories) throws IOException;

    List<Recipe> getSimilarRecipes(Recipe recipe);

    String getRecipeFilterString(int difficulty);

    PaginatedResult<Recipe> findByAuthorId(User user, int page);

    PaginatedResult<Recipe> findFavouritesByUserId(User user, int page);

    void highlightRecipe(Recipe recipe);

    String getRecipeOrderString(String order);

}
