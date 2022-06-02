package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.IngredientDao;
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
@Sql(scripts = "classpath:ingredientTest.sql") //para partir de una base conocida
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = TestConfig.class)
public class IngredientDaoTest {

    @Autowired
    private DataSource ds;
    @Autowired
    private IngredientDao ingredientDao;
    @Autowired
    private RecipeDao recipeDao;

    private JdbcTemplate jdbcTemplate;



    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testFindByRecipeId() {
        Ingredient ingredient = Mockito.mock(Ingredient.class);
        when(ingredient.getIdIngredient()).thenReturn(5L);
        when(ingredient.getIngredientName()).thenReturn("queso");
        when(ingredient.getIngredientQuantity()).thenReturn("200gr");

        Optional<Recipe> optionalRecipe = recipeDao.findById(1L);
        if(!optionalRecipe.isPresent())
            return;
        List<Ingredient> ing = ingredientDao.findByRecipeId(optionalRecipe.get());
        assertFalse(ing.isEmpty());
        assertEquals(ing.get(0).getIdIngredient(),ingredient.getIdIngredient());
        assertEquals(ing.get(0).getIngredientName(),ingredient.getIngredientName());
        assertEquals(ing.get(0).getIngredientQuantity(),ingredient.getIngredientQuantity());
    }

    @Test
    public void testFindById(){
        Ingredient ingredient = Mockito.mock(Ingredient.class);
        when(ingredient.getIdIngredient()).thenReturn(5L);
        when(ingredient.getIngredientName()).thenReturn("queso");
        when(ingredient.getIngredientQuantity()).thenReturn("200gr");

        Optional<Ingredient> ing = ingredientDao.findById(5);
        assertTrue(ing.isPresent());
        assertEquals(ing.get().getIdIngredient(),ingredient.getIdIngredient());
        assertEquals(ing.get().getIngredientName(),ingredient.getIngredientName());
        assertEquals(ing.get().getIngredientQuantity(),ingredient.getIngredientQuantity());

    }

    @Test
    public void testFindByIdFailure(){
        Optional<Ingredient> ing = ingredientDao.findById(200);
        assertFalse(ing.isPresent());
    }

    @Test
    public void testCreateIngredient(){
        Optional<Recipe> optionalRecipe = recipeDao.findById(2L);
        if(!optionalRecipe.isPresent())
            return;

        Ingredient ing = ingredientDao.create(optionalRecipe.get(), "salame", "100gr");
        assertEquals(ing.getRecipe().getIdRecipe(), optionalRecipe.get().getIdRecipe());
        assertEquals(ing.getIngredientName(), "salame");
        assertEquals(ing.getIngredientQuantity(), "100gr");
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "recipe_ingredients", "id_recipe = 1"));

    }




}
