package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Recipe;
import ar.edu.itba.paw.models.User;

import java.util.Collection;
import java.util.Optional;

public interface CommentDao {
    Optional<Comment> findById(long id);
    Comment create(Recipe recipe, User user, String commentDesc);
    Collection<Comment> findByRecipeId(Recipe recipe, int page);
    void delete(long id);
    Long findByRecipeIdCount(Recipe recipe);
}
