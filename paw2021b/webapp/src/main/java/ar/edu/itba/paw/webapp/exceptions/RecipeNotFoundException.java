package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.interfaces.exceptions.CustomRuntimeException;

public class RecipeNotFoundException extends CustomRuntimeException {
    public RecipeNotFoundException() {
        super("error.recipeNotFound", 404);
    }
}
