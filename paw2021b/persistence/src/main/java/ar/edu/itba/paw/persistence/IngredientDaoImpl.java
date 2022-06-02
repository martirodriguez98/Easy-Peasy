package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.IngredientDao;
import ar.edu.itba.paw.models.Ingredient;
import ar.edu.itba.paw.models.Recipe;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Repository
public class IngredientDaoImpl implements IngredientDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Ingredient create(Recipe recipe, String ingredientName, String ingredientQuantity) {
        Ingredient ingredient = new Ingredient(recipe, ingredientName, ingredientQuantity);
        em.persist(ingredient);
        return ingredient;
    }

    @Override
    public Optional<Ingredient> findById(long id) {
        return Optional.ofNullable(em.find(Ingredient.class,id));
    }

    @Override
    public List<Ingredient> findByRecipeId(Recipe recipe) {
        final String query = "SELECT ing FROM Ingredient ing WHERE ing.recipe = :recipe";
        TypedQuery<Ingredient> queryResult = em.createQuery(query,Ingredient.class);
        queryResult.setParameter("recipe",recipe);
        return queryResult.getResultList();


    }


}
