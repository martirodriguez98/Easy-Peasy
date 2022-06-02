package ar.edu.itba.paw.webapp.dto.validations;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class InvalidImageSizeListValidator implements ConstraintValidator<ImageSizeConstraint, List<FormDataBodyPart>> {

    private ImageSizeConstraint imageSizeConstraint;
    private int size;

    @Override
    public void initialize(ImageSizeConstraint imageSizeConstraint) {
        this.imageSizeConstraint = imageSizeConstraint;
        size = imageSizeConstraint.size();
    }

    @Override
    public boolean isValid(List<FormDataBodyPart> images, ConstraintValidatorContext context) {
        if (!images.isEmpty()) {
            for (FormDataBodyPart image : images) {
                if (image.getContentDisposition().getSize() > size)
                    return false;
            }
        }
        return true;
    }


}
