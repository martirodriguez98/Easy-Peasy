package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.RecipeCategoryDao;
import ar.edu.itba.paw.interfaces.RecipeDao;
import ar.edu.itba.paw.models.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@Rollback //para volver atras despues del test
@Sql(scripts = "classpath:recipeCategoryTest.sql") //para partir de una base conocida
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = TestConfig.class)
public class RecipeCategoryDaoTest {

    @Autowired
    private DataSource ds;
    @Autowired
    private RecipeCategoryDao recipeCategoryDao;
    @Autowired
    private RecipeDao recipeDao;

    private JdbcTemplate jdbcTemplate;


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testFindByRecipeId() {
        Optional<Recipe> optionalRecipe = recipeDao.findById(1L);
        if(!optionalRecipe.isPresent())
            return;
        List<RecipeCategory> categories = recipeCategoryDao.findByRecipeId(optionalRecipe.get());
        assertFalse(categories.isEmpty());
    }

    @Test
    public void testFindByRecipeIdFailure() {
        Optional<Recipe> optionalRecipe = recipeDao.findById(2L);
        if(!optionalRecipe.isPresent())
            return;
        List<RecipeCategory> categories = recipeCategoryDao.findByRecipeId(optionalRecipe.get());
        assertTrue(categories.isEmpty());
    }

    @Test
    public void testCreateRecipeCategory(){
        Optional<Recipe> optionalRecipe = recipeDao.findById(2L);
        if(!optionalRecipe.isPresent())
            return;
        RecipeCategory cat = recipeCategoryDao.create(optionalRecipe.get(), 2);
        assertEquals(cat.getCategory(), 2);
        List<RecipeCategory> found = recipeCategoryDao.findByRecipeId(optionalRecipe.get());
        assertFalse(found.isEmpty());
        assertEquals(found.get(0).getCategory(), 2);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "recipe_categories", "id_recipe = 2"));

    }

    @Test
    public void testFindAllCategories(){
        Optional<Recipe> optionalRecipe = recipeDao.findById(1L);
        if(!optionalRecipe.isPresent())
            return;
        List<RecipeCategory> found = recipeCategoryDao.findByRecipeId(optionalRecipe.get());
        assertFalse(found.isEmpty());
        assertEquals(found.get(0).getCategory(), 1);
    }

}
