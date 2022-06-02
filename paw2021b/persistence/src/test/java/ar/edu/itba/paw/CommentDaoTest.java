package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.CommentDao;
import ar.edu.itba.paw.interfaces.ImageDao;
import ar.edu.itba.paw.interfaces.RecipeDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.*;
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

import java.sql.Date;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@Rollback //para volver atras despues del test
@Sql(scripts = "classpath:commentTest.sql") //para partir de una base conocida
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class CommentDaoTest {

    @Autowired
    private DataSource ds;
    @Autowired
    private CommentDao commentDao;
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
    public void testFindById() {
        Comment comment = Mockito.mock(Comment.class);
        when(comment.getIdComment()).thenReturn(2L);
        final Optional<Comment> optionalComment = commentDao.findById(comment.getIdComment());
        assertTrue(optionalComment.isPresent());
        assertEquals(optionalComment.get().getIdComment(), comment.getIdComment());
    }

    @Test
    public void testFindByIdFailure() {
        Optional<Comment> cmmnt = commentDao.findById(200);
        assertFalse(cmmnt.isPresent());
    }

    @Test
    public void testFindByRecipeId(){
        Recipe recipe = Mockito.mock(Recipe.class);
        when(recipe.getIdRecipe()).thenReturn(1L);
        Comment comment = Mockito.mock(Comment.class);
        when(comment.getIdComment()).thenReturn(2L);
        Collection<Comment> comments = commentDao.findByRecipeId(recipe, 0);
        List<Comment> commentsList = new ArrayList<>(comments);
        assertEquals(commentsList.get(0).getIdComment(), comment.getIdComment());
    }

    @Test
    public void testFindByRecipeIdCount(){
        Recipe recipe = Mockito.mock(Recipe.class);
        when(recipe.getIdRecipe()).thenReturn(1L);
        long count = commentDao.findByRecipeIdCount(recipe);
        assertEquals(count, 1);
    }

    @Test
    public void testCreateComment(){

        Optional<User> user = userDao.findById(1);
        Optional<Recipe> recipe = recipeDao.findById(2);
        if(!recipe.isPresent() || !user.isPresent())
            return;

        Comment commentTest = commentDao.create(recipe.get(), user.get(), "muy bueno");

        Collection<Comment> comments = commentDao.findByRecipeId(recipe.get(), 0);
        List<Comment> commentsList = new ArrayList<>(comments);

        assertEquals(commentsList.get(0).getIdComment(), commentTest.getIdComment());

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "comment", "id_recipe = " + recipe.get().getIdRecipe()));

    }

    @Test
    public void testDelete(){
        commentDao.delete(2);
        Optional<Comment> cmmnt = commentDao.findById(2);
        assertFalse(cmmnt.isPresent());
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "comment", "id_recipe = 1"));
    }


}
