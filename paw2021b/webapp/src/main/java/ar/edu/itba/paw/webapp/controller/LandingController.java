package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.Recipe;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class LandingController {
    @Autowired
    UserService userService;
    @Autowired
    RecipeService recipeService;

    @Autowired
    CommentService commentService;
    @Autowired
    IngredientService ingredientService;
    @Autowired
    RecipeCategoryService recipeCategoryService;
    @Autowired
    SearchService searchService;
    @Autowired
    ImageService imageService;


    private static final Logger LOGGER = LoggerFactory.getLogger(LandingController.class);

    @RequestMapping("/")
    public ModelAndView landing(@ModelAttribute("searchForm") final SearchForm form) {
        LOGGER.info("In landing page");
        final ModelAndView mav = new ModelAndView("/landing");

        mav.addObject("userService",userService);
        mav.addObject("recipeService", recipeService);
        mav.addObject("recipeCategoryService",recipeCategoryService);
        mav.addObject("imageService", imageService);
        return mav;
    }

}