package ar.edu.itba.paw.webapp.dto.validations;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InvalidImageSizeValidator implements ConstraintValidator<ImageSizeConstraint, FormDataBodyPart> {

    private ImageSizeConstraint imageSizeConstraint;
    private int size;

    @Override
    public void initialize(ImageSizeConstraint imageSizeConstraint) {
        this.imageSizeConstraint = imageSizeConstraint;
        size = imageSizeConstraint.size();
    }

    @Override
    public boolean isValid(FormDataBodyPart image, ConstraintValidatorContext context) {
        return true;
    }
}