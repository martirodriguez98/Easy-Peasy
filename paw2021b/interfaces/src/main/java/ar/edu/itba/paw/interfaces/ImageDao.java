package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Recipe;

import java.util.List;
import java.util.Optional;

public interface ImageDao {
    Optional<Image> findById(long id);
    List<Image> findByRecipeId(Recipe recipe);
    Image create(Recipe recipe, byte[] data, String mimeType);
}
