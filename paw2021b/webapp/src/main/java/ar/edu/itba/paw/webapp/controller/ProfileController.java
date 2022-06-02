package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.RecipeCategoryService;
import ar.edu.itba.paw.interfaces.RecipeService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.exceptions.BadRequestException;
import ar.edu.itba.paw.webapp.exceptions.ResourceNotFoundException;
import ar.edu.itba.paw.webapp.form.PaginationForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
public class ProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    RecipeService recipeService;
    @Autowired
    ImageService imageService;
    @Autowired
    RecipeCategoryService recipeCategoryService;

    private static final int MAX_SIZE_PER_FILE = 5000000;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

    @RequestMapping("/profile")
    public ModelAndView myProfile(@ModelAttribute("userForm") final UserForm form, @ModelAttribute("paginationForm") final PaginationForm paginationForm, final Integer error, Principal principal) {
        LOGGER.info("In /profile");
        final ModelAndView mav = new ModelAndView("/profile");
        final User user;

        if(userService.findByUsername(principal.getName()).isPresent()){
            user = userService.findByUsername(principal.getName()).get();
            LOGGER.debug("User in profile is {}", user);
        }else{
            throw new BadRequestException();
        }

        PaginatedResult<Recipe> recipes = recipeService.findByAuthorId(user, paginationForm.getPage());
        PaginatedResult<Recipe> faves = recipeService.findFavouritesByUserId(user, paginationForm.getPageFaves());

        mav.addObject("userLoggedId",userService.findByUsername(principal.getName()).get().getIdUser());
        mav.addObject("userService",userService);
        mav.addObject("recipes", recipes);
        mav.addObject("faves", faves);
        mav.addObject("error",error);
        mav.addObject("recipeService", recipeService);
        mav.addObject("recipeCategoryService", recipeCategoryService);
        mav.addObject("imageService", imageService);
        return mav;
    }

    @RequestMapping("profile/{idUser}")
    public ModelAndView otherProfile(@PathVariable("idUser") final long idUser, @ModelAttribute("paginationForm") final PaginationForm paginationForm, final Integer error, Principal principal) {
        LOGGER.info("In /profile/{}", idUser);
        if(principal != null){
            Optional<User> aux = userService.findByUsername(principal.getName());
            if(aux.isPresent()){
                if(idUser == aux.get().getIdUser()){
                    return new ModelAndView("redirect:/profile");
                }
            }else{
                throw new BadRequestException();
            }
        }
        final ModelAndView mav = new ModelAndView("/otherProfile");
        final User user;
        if(userService.findById(idUser).isPresent()){
            user = userService.findById(idUser).get();
            LOGGER.debug("Other user is: {}", user);
        }else{
            throw new BadRequestException();
        }
        if(user.getUsername() == null)
            throw new BadRequestException();

        PaginatedResult<Recipe> recipes = recipeService.findByAuthorId(user,paginationForm.getPage());
        if(paginationForm.getPage() >= recipes.getTotalPages()){
            recipes = recipeService.findByAuthorId(user,(int)recipes.getTotalPages()-1);
        }

        if(principal != null){
            mav.addObject("userLoggedId",userService.findByUsername(principal.getName()).get().getIdUser());
        }
        mav.addObject("userService",userService);
        mav.addObject("recipes", recipes);
        mav.addObject("user", user);
        mav.addObject("recipeService", recipeService);
        mav.addObject("error",error);
        mav.addObject("recipeCategoryService", recipeCategoryService);
        mav.addObject("imageService", imageService);
        return mav;
    }

    @RequestMapping(value = "/profile/changeAvatar", method = {RequestMethod.POST})
    public ModelAndView changeImage(@Valid @ModelAttribute("userForm") final UserForm form, Principal principal, final BindingResult error){
        LOGGER.info("In /profile/changeAvatar");
        User user;
        if(userService.findByUsername(principal.getName()).isPresent()){
            user = userService.findByUsername(principal.getName()).get();
            try {
                if(form.getAvatar().getSize() < MAX_SIZE_PER_FILE)
                    userService.changeAvatar(user.getIdUser(), form.getAvatar().getBytes(), form.getAvatar().getContentType());
                else
                    LOGGER.debug("Can't upload image in change avatar");
            } catch (IOException e) {
                LOGGER.warn("Error in uploading profile image");
            }
        }

        return new ModelAndView("redirect:/profile");
    }

    @RequestMapping(path = "/profile/avatar/{userId}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE},
            method = RequestMethod.GET)
    @ResponseBody
    public byte[] getUserAvatar(@PathVariable("userId") long userId){
        ImageUser imageUser = userService.findImageById(userId);
        if(imageUser == null){
            throw new ResourceNotFoundException();
        }
        return imageUser.getData();
    }
}
