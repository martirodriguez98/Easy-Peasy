package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.RecipeDao;
import ar.edu.itba.paw.models.Recipe;
import ar.edu.itba.paw.services.RecipeServiceImpl;
import ar.edu.itba.paw.services.UserServiceImpl;
import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceImplTest {

    @Mock
    private Recipe mockRecipe;
    @Mock
    private RecipeDao mockRecipeDao;

    @InjectMocks
    private final RecipeServiceImpl recipeService = new RecipeServiceImpl();

    @Before
    public void setTest() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
    }

    //el resto de los métodos son únicamente intermediarios para el DAO
    @Test
    public void testLikePercentage(){
        when(mockRecipeDao.getLikes(mockRecipe)).thenReturn(1L);
        when(mockRecipeDao.getDislikes(mockRecipe)).thenReturn(1L);
        double percentage = recipeService.likePercentage(mockRecipe);
        assertEquals(50.0, percentage, 0.01);
    }

}
