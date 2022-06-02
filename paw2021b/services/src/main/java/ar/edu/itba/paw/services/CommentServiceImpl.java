package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.CommentDao;
import ar.edu.itba.paw.interfaces.CommentService;
import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.PaginatedResult;
import ar.edu.itba.paw.models.Recipe;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    final int ITEMS_PER_PAGE = 6;

    @Autowired
    private CommentDao commentDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    public Optional<Comment> findById(long id) {
        return this.commentDao.findById(id);
    }

    @Transactional
    @Override
    public Comment create(Recipe recipe, User user, String commentDesc) {
        LOGGER.debug("Creating comment in recipe with id: {}", recipe.getIdRecipe());
        return this.commentDao.create(recipe, user, commentDesc);
    }


    @Override
    public PaginatedResult<Comment> findByRecipeId(Recipe recipe, int page) {
        final long totalComments = findByRecipeIdCount(recipe);
        final Collection<Comment> comments = commentDao.findByRecipeId(recipe,page);
        return new PaginatedResult<>(page,ITEMS_PER_PAGE,(int)totalComments,recipe.getIdRecipe(),comments);
    }

    @Override
    public Long findByRecipeIdCount(Recipe recipe) {
        return commentDao.findByRecipeIdCount(recipe);
    }

    @Transactional
    @Override
    public void delete(long id) {
        commentDao.delete(id);
    }
}
