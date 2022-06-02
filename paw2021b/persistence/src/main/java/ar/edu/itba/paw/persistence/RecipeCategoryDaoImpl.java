package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.RecipeCategoryDao;
import ar.edu.itba.paw.models.Recipe;
import ar.edu.itba.paw.models.RecipeCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RecipeCategoryDaoImpl implements RecipeCategoryDao {
    @PersistenceContext
    private EntityManager em;

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeCategoryDaoImpl.class);

    @Override
    public List<RecipeCategory> findByRecipeId(Recipe recipe) {
        final String query = "SELECT rc FROM RecipeCategory rc WHERE rc.recipe = :recipe";
        LOGGER.debug("Executing query {} in recipe category dao", query);
        TypedQuery<RecipeCategory> resultQuery = em.createQuery(query,RecipeCategory.class);
        resultQuery.setParameter("recipe",recipe);
        return resultQuery.getResultList();
    }

    @Override
    public RecipeCategory create(Recipe recipe, int category) {
        RecipeCategory recipeCategory = new RecipeCategory(category,recipe);
        em.persist(recipeCategory);

        LOGGER.debug("Added category {} for recipe with id {}",category,recipe.getIdRecipe());
        return recipeCategory;
    }

    @Override
    public List<RecipeCategory> findAllCategories() {
        TypedQuery<RecipeCategory> query = em.createQuery("SELECT rc FROM RecipeCategory rc",RecipeCategory.class);
        return query.getResultList();
    }

}
