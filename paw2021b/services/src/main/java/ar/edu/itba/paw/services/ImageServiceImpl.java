package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.ImageDao;
import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Recipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageDao imageDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Override
    public Optional<Image> findById(long id) {
        return imageDao.findById(id);
    }

    @Override
    public List<Image> findByRecipeId(Recipe recipe) {
        return imageDao.findByRecipeId(recipe);
    }

    @Transactional
    @Override
    public Image create(Recipe recipe, byte[] data, String mimeType) {
        LOGGER.debug("Creating image for recipe with id: {}", recipe.getIdRecipe());
        return imageDao.create(recipe, data, mimeType);
    }
}
