package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.security.Principal;

@ControllerAdvice
public class ErrorController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView pageNotFoundException(Principal principal) {
        final ModelAndView mav = new ModelAndView("/pageNotFound");
        return mav;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ModelAndView badRequestException() {
        final ModelAndView mav = new ModelAndView("/pageNotFound");
        return mav;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView serverException() {
        final ModelAndView mav = new ModelAndView("/500");
        return mav;
    }

}
