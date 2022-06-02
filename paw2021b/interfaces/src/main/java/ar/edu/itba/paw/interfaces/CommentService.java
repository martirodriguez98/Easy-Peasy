package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.PaginatedResult;
import ar.edu.itba.paw.models.Recipe;
import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(long id);
    Comment create(Recipe recipe, User user, String commentDesc);
    PaginatedResult<Comment> findByRecipeId(Recipe recipe, int page);
    Long findByRecipeIdCount(Recipe recipe);
    void delete(long id);
}
