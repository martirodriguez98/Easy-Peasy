package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.OrderOptions;
import ar.edu.itba.paw.models.Recipe;
import ar.edu.itba.paw.models.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RecipeDao {
    Optional<Recipe> findById(long id);
    Recipe create(String recipeTitle, String recipeDesc, String recipeSteps, User user, int recipeDifficulty, String recipeTime );
    List<Recipe> findAllRecipes();
    Collection<Recipe> searchRecipes(String query, List<Integer> categoriesQuery, int difficulty, OrderOptions order, int page, boolean isHighlighted, int pageSize);
    Collection<Recipe> findByAuthorId(User user, int page);
    Collection<Recipe> findFavouritesByUserId(User user, int page);
    void likeRecipe(Recipe recipe, User user);
    void dislikeRecipe(Recipe recipe, User user);
    long getLikes(Recipe recipe);
    long getDislikes(Recipe recipe);
    boolean isLiked(Recipe recipe, User user);
    boolean isDisliked(Recipe recipe, User user);
    void delete(Recipe recipe, User user);
    void favoriteRecipe(Recipe recipe, User user);
    boolean isFavorited(Recipe recipe, User user);
    List<Recipe> getSimilarRecipes(Recipe recipe);
    Integer getSearchResultCount(String query, List<Integer> categoriesQuery, int difficulty, boolean isHighlighted);
    Long getFindFavouritesCount(User user);
    Long getFindByAuthorCount(User user);
    void highlightRecipe(Recipe recipe);
}
