package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.IngredientDao;
import ar.edu.itba.paw.interfaces.IngredientService;
import ar.edu.itba.paw.models.Ingredient;
import ar.edu.itba.paw.models.Recipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {
    @Autowired
    private IngredientDao ingredientDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientServiceImpl.class);

    @Override
    public Optional<Ingredient> findById(long id){
        return this.ingredientDao.findById(id);
    }
    @Override
    public List<Ingredient> findByRecipeId(Recipe recipe){
        return this.ingredientDao.findByRecipeId(recipe);
    }

    @Transactional
    @Override
    public Ingredient create(Recipe recipe, String ingredientName, String ingredientQuantity) {
        LOGGER.debug("Creating ingredient for recipe with id: {}", recipe.getIdRecipe());
        return this.ingredientDao.create(recipe,ingredientName,ingredientQuantity);
    }
}
