package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.ImageDao;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Recipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Repository
public class ImageDaoImpl implements ImageDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageDaoImpl.class);
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Image> findById(long id) {
        LOGGER.debug("Executing query findById in image dao");
        return Optional.ofNullable(em.find(Image.class, id));
    }

    @Override
    public List<Image> findByRecipeId(Recipe recipe) {
        final String query = "SELECT i FROM Image i WHERE i.recipe = :recipe";
        final TypedQuery<Image> queryResult = em.createQuery(query, Image.class)
                .setParameter("recipe", recipe);
        return queryResult.getResultList();
    }

    @Override
    public Image create(Recipe recipe, byte[] data, String mimeType) {
        if(mimeType.compareTo("application/octet-stream") == 0){
            return new Image(recipe, data, mimeType);
        }else {
            final Image img = new Image(recipe, data, mimeType);
            em.persist(img);
            return img;
        }
    }

}
