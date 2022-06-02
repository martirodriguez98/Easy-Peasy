package ar.edu.itba.paw.webapp.dto.validations;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = { InvalidImageTypeValidator.class, InvalidImageTypeListValidator.class })
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageTypeConstraint {

    String message() default "Invalid image type";
    String[] contentType();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}