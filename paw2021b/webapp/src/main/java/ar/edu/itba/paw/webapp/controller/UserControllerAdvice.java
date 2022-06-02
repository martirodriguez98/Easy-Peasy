package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exceptions.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserControllerAdvice {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerAdvice.class);

    @ModelAttribute("userLogged")
    public User userLogged(Model model) {

        if (model.containsAttribute("userLogged")) {
            LOGGER.debug("Retrieved current user logged by controller");
            return (User) model.asMap().get("userLogged");
        }

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!isAnonymous(auth)){
            if(userService.findByUsername(auth.getName()).isPresent()) {
                LOGGER.debug("Retrieved current user logged by user service");
                return userService.findByUsername(auth.getName()).get();
            }
            else{
                throw new BadRequestException();
            }
        }
        LOGGER.debug("Current user is anonymous");
        return null;
    }

    private boolean isAnonymous(Authentication auth) {
        return auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ANONYMOUS"));
    }



}
