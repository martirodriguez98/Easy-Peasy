package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.PaginatedResult;
import ar.edu.itba.paw.models.Recipe;
import ar.edu.itba.paw.models.User;

import java.util.List;

public interface SearchService {
    PaginatedResult<Recipe> searchRecipes(String query, List<Integer> categoriesQuery,int difficulty, String order, int page, boolean isHighlighted, int pageSize);
    PaginatedResult<User> searchUsers(String query,Boolean highlighted, Boolean admins, String order, int page);
}
