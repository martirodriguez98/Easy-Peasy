package ar.edu.itba.paw.webapp.dto.validations;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InvalidImageTypeListValidator implements ConstraintValidator<ImageTypeConstraint, List<FormDataBodyPart>> {


    private ImageTypeConstraint imageTypeConstraint;
    private Collection<String> validTypes;


    @Override
    public void initialize(ImageTypeConstraint imageTypeConstraint) {
        this.imageTypeConstraint = imageTypeConstraint;
        this.validTypes = Collections.unmodifiableCollection(Arrays.asList(this.imageTypeConstraint.contentType()));
    }

    @Override
    public boolean isValid(List<FormDataBodyPart> images, ConstraintValidatorContext context) {
        if (images != null && !images.isEmpty()) {
            for (FormDataBodyPart image : images) {
                if (!validTypes.contains(image.getMediaType().toString()))
                    return false;
            }
        }
        return true;
    }

}

