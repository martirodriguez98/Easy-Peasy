package ar.edu.itba.paw.webapp.dto.validations;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class InvalidImageTypeValidator implements ConstraintValidator<ImageTypeConstraint, FormDataBodyPart> {

    private ImageTypeConstraint imageTypeConstraint;
    private Collection<String> validTypes;

    @Override
    public void initialize(ImageTypeConstraint imageTypeConstraint) {
        this.imageTypeConstraint = imageTypeConstraint;
        this.validTypes = Collections.unmodifiableCollection(Arrays.asList(this.imageTypeConstraint.contentType()));
    }

    @Override
    public boolean isValid(FormDataBodyPart image, ConstraintValidatorContext context) {
        if (image != null) {
            return validTypes.contains(image.getMediaType().toString());
        }
        return true;
    }

}

