package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.RecipeCategoryDao;
import ar.edu.itba.paw.interfaces.RecipeCategoryService;
import ar.edu.itba.paw.models.Categories;
import ar.edu.itba.paw.models.Recipe;
import ar.edu.itba.paw.models.RecipeCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class RecipeCategoryServiceImpl implements RecipeCategoryService {
    @Autowired
    private RecipeCategoryDao recipeCategoryDao;

    @Autowired
    MessageSource messageSource;

    private Locale locale;
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeCategoryServiceImpl.class);

    @Override
    public List<RecipeCategory> findByRecipeId(Recipe recipe) {
        return this.recipeCategoryDao.findByRecipeId(recipe);
    }

    @Transactional
    @Override
    public RecipeCategory create(Recipe recipe, int category) {
        LOGGER.debug("Creating recipe category for recipe with id: {}", recipe.getIdRecipe());
        return this.recipeCategoryDao.create(recipe, category);
    }

    @Override
    public List<RecipeCategory> findAllCategories() {
        return this.recipeCategoryDao.findAllCategories();
    }

    @Override
    public String getRecipeFilterString(int category) {
        locale = LocaleContextHolder.getLocale();
        Categories choice = Categories.values()[category];
        switch (choice) {
            case BREAKFAST:
                return messageSource.getMessage("category.daytime.breakfast", null, locale);
            case DINNER:
                return messageSource.getMessage("category.daytime.dinner", null, locale);
            case LUNCH:
                return messageSource.getMessage("category.daytime.lunch", null, locale);
            case SNACK:
                return messageSource.getMessage("category.daytime.snack", null, locale);
            case BITTER:
                return messageSource.getMessage("category.flavour.bitter", null, locale);
            case BITTERSWEET:
                return messageSource.getMessage("category.flavour.bittersweet", null, locale);
            case SAVOURY:
                return messageSource.getMessage("category.flavour.savoury", null, locale);
            case SPICY:
                return messageSource.getMessage("category.flavour.spicy", null, locale);
            case SWEET:
                return messageSource.getMessage("category.flavour.sweet", null, locale);
            case BAKERY:
                return messageSource.getMessage("category.bakery", null, locale);
            case FISH:
                return messageSource.getMessage("category.fish", null, locale);
            case GLUTEN_FREE:
                return messageSource.getMessage("category.glutenFree", null, locale);
            case HEALTHY:
                return messageSource.getMessage("category.healthy", null, locale);
            case KOSHER:
                return messageSource.getMessage("category.kosher", null, locale);
            case MEAT:
                return messageSource.getMessage("category.meat", null, locale);
            case PASTA:
                return messageSource.getMessage("category.pasta", null, locale);
            case PASTRIES:
                return messageSource.getMessage("category.pastries", null, locale);
            case SALADS:
                return messageSource.getMessage("category.salads", null, locale);
            case SIDE_DISHES:
                return messageSource.getMessage("category.sideDishes", null, locale);
            case VEGAN:
                return messageSource.getMessage("category.vegan", null, locale);
            case VEGETARIAN:
                return messageSource.getMessage("search.category.vegetarian", null, locale);
        }
        return null;
    }
}
