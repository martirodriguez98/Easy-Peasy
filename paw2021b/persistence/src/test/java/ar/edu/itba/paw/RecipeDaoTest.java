package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.RecipeDao;
import ar.edu.itba.paw.interfaces.RoleDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import javax.swing.text.html.Option;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@Rollback //para volver atras despues del test
@Sql(scripts = "classpath:recipeTest.sql") //para partir de una base conocida
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = TestConfig.class)
public class RecipeDaoTest {

    @Autowired
    private DataSource ds;
    @Autowired
    private RecipeDao recipeDao;
    @Autowired
    private UserDao userDao;

    private JdbcTemplate jdbcTemplate;


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testSearchRecipes(){
        Collection<Recipe> found = recipeDao.searchRecipes("title", new LinkedList<Integer>(), 1, OrderOptions.DATE_ASC, 0, false,6);
        assertEquals(found.size(), 4);
    }

    @Test
    public void testSearchRecipesCategories(){
        LinkedList<Integer> categories = new LinkedList<>();
        categories.add(5);
        Collection<Recipe> found = recipeDao.searchRecipes("title", categories, 1, OrderOptions.DATE_ASC, 0, false,6);
        assertEquals(found.size(), 1);
    }

    @Test
    public void testSimilarRecipes(){
        Optional<Recipe> recipe = recipeDao.findById(1);
        if(!recipe.isPresent())
            return;
        Recipe res = recipe.get();
        List<Recipe> found = recipeDao.getSimilarRecipes(res);
        assertEquals(found.size(), 3);
    }

    @Test
    public void testFindById() {
        Recipe recipe = Mockito.mock(Recipe.class);
        when(recipe.getIdRecipe()).thenReturn(1L);
        final Optional<Recipe> optionalRecipe = recipeDao.findById(recipe.getIdRecipe());
        if(optionalRecipe.isPresent())
            assertEquals(recipe.getIdRecipe(), optionalRecipe.get().getIdRecipe());
    }
    @Test
    public void testFindByIdFailure() {
        Optional<Recipe> recipe = recipeDao.findById(80);
        assertFalse(recipe.isPresent());
    }

    @Test
    public void testCreateRecipe(){
//
//        Optional<User> optionalUser = userDao.findById(1L);
//        if(!optionalUser.isPresent())
//            return;
//        final Recipe recipe = recipeDao.create("recipe", "description", "steps", optionalUser.get(), 1, "5");
//        assertEquals(1, recipe.getIdRecipe());
//        assertEquals(recipe.getRecipeTitle(), "recipe");
//        assertEquals(recipe.getRecipeDesc(), "description");
//        assertEquals(recipe.getRecipeSteps(), "steps");
//        assertEquals(recipe.getUser().getIdUser(), optionalUser.get().getIdUser());
//        assertEquals(recipe.getRecipeDifficulty(), 1);
//        assertEquals(recipe.getRecipeTime(), "5");
//        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "recipe", "id_recipe = 2"));

    }


    @Test
    public void testFindByAuthorId(){
        Recipe recipe = Mockito.mock(Recipe.class);
        when(recipe.getIdRecipe()).thenReturn(1L);
        User user = Mockito.mock(User.class);
        when(user.getIdUser()).thenReturn(1L);

        Collection<Recipe> recipes = recipeDao.findByAuthorId(user, 0);
        List<Recipe> recipeList= new ArrayList<>(recipes);
        assertEquals(recipeList.get(0).getIdRecipe(), recipe.getIdRecipe());

    }
    @Test
    public void testLikeRecipe(){
        Optional<Recipe> optionalRecipe = recipeDao.findById(1L);
        Optional<User> optionalUser = userDao.findById(1L);
        if(!optionalUser.isPresent() || !optionalRecipe.isPresent())
            return;
        recipeDao.likeRecipe(optionalRecipe.get(), optionalUser.get());
        assertEquals(recipeDao.getLikes(optionalRecipe.get()), 1);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "liked_recipes", "id_recipe = 1"));

    }
    @Test
    public void testDislikeRecipe(){
        Optional<Recipe> optionalRecipe = recipeDao.findById(1L);
        Optional<User> optionalUser = userDao.findById(1L);
        if(!optionalUser.isPresent() || !optionalRecipe.isPresent())
            return;
        recipeDao.dislikeRecipe(optionalRecipe.get(), optionalUser.get());
        assertEquals(recipeDao.getDislikes(optionalRecipe.get()), 1);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "disliked_recipes", "id_recipe = 1"));

    }

    @Test
    public void testIsLiked(){
        Optional<Recipe> optionalRecipe = recipeDao.findById(2L);
        Optional<User> optionalUser = userDao.findById(1L);
        if(!optionalUser.isPresent() || !optionalRecipe.isPresent())
            return;
        assertTrue(recipeDao.isLiked(optionalRecipe.get(), optionalUser.get()));
    }
    @Test
    public void testIsDisliked(){
        Optional<Recipe> optionalRecipe = recipeDao.findById(3L);
        Optional<User> optionalUser = userDao.findById(1L);
        if(!optionalUser.isPresent() || !optionalRecipe.isPresent())
            return;
        assertTrue(recipeDao.isDisliked(optionalRecipe.get(), optionalUser.get()));
    }

    @Test
    public void testFavoriteRecipe(){
        Optional<Recipe> optionalRecipe = recipeDao.findById(3L);
        Optional<User> optionalUser = userDao.findById(1L);
        if(!optionalUser.isPresent() || !optionalRecipe.isPresent())
            return;
        recipeDao.favoriteRecipe(optionalRecipe.get(), optionalUser.get());
        assertTrue(recipeDao.isFavorited(optionalRecipe.get(), optionalUser.get()));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "favourites", "id_recipe = 3"));
    }

    @Test
    public void testIsFavourited(){
        Optional<Recipe> optionalRecipe = recipeDao.findById(1L);
        Optional<User> optionalUser = userDao.findById(1L);
        if(!optionalUser.isPresent() || !optionalRecipe.isPresent())
            return;
        assertTrue(recipeDao.isFavorited(optionalRecipe.get(), optionalUser.get()));
    }

    @Test
    public void testDeleteRecipe(){
        Optional<Recipe> optionalRecipe = recipeDao.findById(4L);
        Optional<User> optionalUser = userDao.findById(2L);
        if(!optionalUser.isPresent() || !optionalRecipe.isPresent())
            return;
        recipeDao.delete(optionalRecipe.get(),optionalUser.get());
        optionalRecipe = recipeDao.findById(4L);
        assertFalse(optionalRecipe.isPresent());
    }

}
