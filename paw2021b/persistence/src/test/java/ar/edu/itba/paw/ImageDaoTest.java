package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.ImageDao;
import ar.edu.itba.paw.interfaces.RecipeDao;
import ar.edu.itba.paw.models.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Rollback //para volver atras despues del test
@Sql(scripts = "classpath:imageTest.sql") //para partir de una base conocida
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = TestConfig.class)
public class ImageDaoTest {

    @Autowired
    private DataSource ds;
    @Autowired
    private ImageDao imagedao;
    @Autowired
    private RecipeDao recipeDao;

    private JdbcTemplate jdbcTemplate;


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreateAndFind() {
        Optional<Recipe> optionalRecipe = recipeDao.findById(1L);
        if(!optionalRecipe.isPresent())
            return;
        Image imgtest = imagedao.create(optionalRecipe.get(), new byte[]{1, 0, 1}, "png");

        Optional<Image> img = imagedao.findById(imgtest.getImageId());
        if(img.isPresent()){
            assertEquals(img.get().getImageId(), imgtest.getImageId());
            assertArrayEquals(img.get().getData(), imgtest.getData());
            assertEquals(img.get().getMimeType(), imgtest.getMimeType());
            assertEquals(img.get().getRecipe(), imgtest.getRecipe());
        }
    }

    @Test
    public void testFindByIdFailure() {

        Optional<Image> img = imagedao.findById(200);
        assertFalse(img.isPresent());
    }

    @Test
    public void testFindByRecipeId(){
        Optional<Recipe> optionalRecipe = recipeDao.findById(2L);
        if(!optionalRecipe.isPresent())
            return;
        Image imgtest = imagedao.create(optionalRecipe.get(), new byte[]{1, 0, 1}, "png");
        List<Image> imgs= imagedao.findByRecipeId(optionalRecipe.get());
        assertFalse(imgs.isEmpty());
        assertEquals(imgs.get(0).getImageId(), imgtest.getImageId());
        assertArrayEquals(imgs.get(0).getData(), imgtest.getData());
        assertEquals(imgs.get(0).getMimeType(), imgtest.getMimeType());
        assertEquals(imgs.get(0).getRecipe(), imgtest.getRecipe());

    }

    @Test
    public void testFindByRecipeIdFailure(){
        Optional<Recipe> optionalRecipe = recipeDao.findById(2L);
        if(!optionalRecipe.isPresent())
            return;
        List<Image> imgs= imagedao.findByRecipeId(optionalRecipe.get());
        assertTrue(imgs.isEmpty());
    }

}