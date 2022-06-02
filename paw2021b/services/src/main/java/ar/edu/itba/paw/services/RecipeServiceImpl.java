package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {
    final int ITEMS_PER_PAGE = 6;
    @Autowired
    private RecipeDao recipeDao;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private RecipeCategoryService recipeCategoryService;
    @Autowired
    private ImageService imageService;
    @Autowired
    MessageSource messageSource;

    private Locale locale;
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeServiceImpl.class);

    @Override
    public Optional<Recipe> findById(long id) {
        return this.recipeDao.findById(id);
    }

    @Transactional
    @Override
    public Recipe createRecipe(User user, String recipeTitle, String recipeDescription, List<String> recipeIngredients, Integer recipeTime, String recipeSteps, List<ImageUser> imagesToUpload, Integer recipeDifficulty, List<Integer> recipeCategories) {
        LOGGER.debug("Creating recipe: {}", recipeTitle);
        return this.recipeDao.create(recipeTitle, recipeDescription, recipeSteps, user, recipeDifficulty, recipeTime.toString());
    }

    @Transactional
    @Override
    public Recipe manageRecipeCreation(BindingResult errors, User user, String recipeTitle, String recipeDescription, List<String> recipeIngredients, Integer recipeTime, String recipeSteps, List<ImageUser> imagesToUpload, Integer recipeDifficulty, List<Integer> recipeCategories) throws IOException {

        final Recipe recipe = createRecipe(user, recipeTitle, recipeDescription, recipeIngredients, recipeTime, recipeSteps, imagesToUpload, recipeDifficulty, recipeCategories);
        LOGGER.debug("Recipe created is: {}", recipe);

        if (recipeIngredients != null) {
            for (int i = 0; i < recipeIngredients.size() - 1; i++) {
                ingredientService.create(recipe, recipeIngredients.get(i), recipeIngredients.get(i + 1));
                i++;
            }
        }

        if (recipeCategories != null) {
            for (Integer category : recipeCategories) {
                recipeCategoryService.create(recipe, category);
            }
        }
        for (ImageUser image : imagesToUpload) {
            imageService.create(recipe, image.getData(), image.getMimeType());
        }
        return recipe;

    }

    @Override
    public PaginatedResult<Recipe> findByAuthorId(User user, int page) {

        final long totalRecipes = recipeDao.getFindByAuthorCount(user);
        final Collection<Recipe> recipes = recipeDao.findByAuthorId(user, page);
        PaginatedResult<Recipe> recipepag = new PaginatedResult<>(page, ITEMS_PER_PAGE, totalRecipes, user.getIdUser(), recipes);
        if (page >= recipepag.getTotalPages()) {
            recipepag = new PaginatedResult<>(recipepag.getTotalPages() - 1, ITEMS_PER_PAGE, totalRecipes, user.getIdUser(), recipes);
        }
        return recipepag;
    }

    @Override
    public PaginatedResult<Recipe> findFavouritesByUserId(User user, int page) {
        final long totalRecipes = recipeDao.getFindFavouritesCount(user);
        final Collection<Recipe> recipes = recipeDao.findFavouritesByUserId(user, page);
        PaginatedResult<Recipe> recipepag = new PaginatedResult<>(page, ITEMS_PER_PAGE, totalRecipes, user.getIdUser(), recipes);
        if (page >= recipepag.getTotalPages()) {
            recipepag = new PaginatedResult<>(recipepag.getTotalPages() - 1, ITEMS_PER_PAGE, totalRecipes, user.getIdUser(), recipes);
        }
        return recipepag;
    }

    @Transactional
    @Override
    public void highlightRecipe(Recipe recipe) {
        recipeDao.highlightRecipe(recipe);
    }

    @Override
    public List<Recipe> findAllRecipes() {
        return this.recipeDao.findAllRecipes();
    }

    @Override
    public List<Recipe> getSimilarRecipes(Recipe recipe) {
        LOGGER.debug("Retrieving similar recipes");
        return this.recipeDao.getSimilarRecipes(recipe);
    }

    @Transactional
    @Override
    public void likeRecipe(Recipe recipe, User user) {
        LOGGER.info("Liking recipe id {} by user id {}", recipe.getIdRecipe(), user.getIdUser());
        this.recipeDao.likeRecipe(recipe, user);
    }

    @Transactional
    @Override
    public void dislikeRecipe(Recipe recipe, User user) {
        LOGGER.info("Disliking recipe id {} by user id {}", recipe.getIdRecipe(), user.getIdUser());
        this.recipeDao.dislikeRecipe(recipe, user);
    }

    @Override
    public long getLikes(Recipe recipe) {
        LOGGER.info("Retrieving likes for recipe id: {}", recipe.getIdRecipe());
        return this.recipeDao.getLikes(recipe);
    }

    @Override
    public long getDislikes(Recipe recipe) {
        LOGGER.info("Retrieving dislikes for recipe id: {}", recipe.getIdRecipe());
        return this.recipeDao.getDislikes(recipe);
    }

    @Override
    public boolean isLiked(Recipe recipe, User user) {
        return this.recipeDao.isLiked(recipe, user);
    }

    @Override
    public boolean isDisliked(Recipe recipe, User user) {
        return this.recipeDao.isDisliked(recipe, user);
    }

    @Override
    public double likePercentage(Recipe recipe) {
        long likes = getLikes(recipe);
        long dislikes = getDislikes(recipe);
        if (likes == 0 && dislikes == 0) {
            return -1;
        }
        double ans = (double) (likes * 100) / (likes + dislikes);
        int decimal_spaces = 2;
        BigDecimal bd = new BigDecimal(ans);
        bd = bd.setScale(decimal_spaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Transactional
    @Override
    public void favRecipe(Recipe recipe, User user) {
        this.recipeDao.favoriteRecipe(recipe, user);
    }

    @Transactional
    @Override
    public void unfavRecipe(Recipe recipe, User user) {
        this.recipeDao.favoriteRecipe(recipe, user);
    }

    @Override
    public boolean isFavorited(Recipe recipe, User user) {
        return this.recipeDao.isFavorited(recipe, user);
    }

    @Transactional
    @Override
    public void delete(Recipe recipe, User user) {
        this.recipeDao.delete(recipe, user);
    }

    @Override
    public String getRecipeFilterString(int difficulty) {
        locale = LocaleContextHolder.getLocale();
        switch (difficulty) {
            case 1: //easy
                return messageSource.getMessage("newRec.difficulty.easy", null, locale);
            case 2: //medium
                return messageSource.getMessage("newRec.difficulty.medium", new Object[]{}, locale);
            case 3: //hard
                return messageSource.getMessage("newRec.difficulty.hard", new Object[]{}, locale);
        }
        return null;
    }

    @Override
    public String getRecipeOrderString(String order) {
        locale = LocaleContextHolder.getLocale();
        String option = OrderOptions.valueOf(order).toString();
        if (option.equals(OrderOptions.TITLE_ASC.toString())) {
            return messageSource.getMessage("search.orderBy.title.asc", null, locale);
        } else if (option.equals(OrderOptions.TITLE_DESC.toString())) {
            return messageSource.getMessage("search.orderBy.title.desc", null, locale);

        } else if (option.equals(OrderOptions.DATE_ASC.toString())) {
            return messageSource.getMessage("search.orderBy.date.asc", null, locale);

        } else if (option.equals(OrderOptions.DATE_DESC.toString())) {
            return messageSource.getMessage("search.orderBy.date.desc", null, locale);

        } else if (option.equals(OrderOptions.LIKES_ASC.toString())) {
            return messageSource.getMessage("search.orderBy.likes.asc", null, locale);

        } else if (option.equals(OrderOptions.LIKES_DESC.toString())) {
            return messageSource.getMessage("search.orderBy.likes.desc", null, locale);
        }
        return null;
    }
}