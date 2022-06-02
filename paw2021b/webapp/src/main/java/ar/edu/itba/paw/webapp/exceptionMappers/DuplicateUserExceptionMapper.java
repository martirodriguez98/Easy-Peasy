package ar.edu.itba.paw.webapp.exceptionMappers;

import ar.edu.itba.paw.interfaces.exceptions.DuplicateUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Singleton
@Component
@Provider
public class DuplicateUserExceptionMapper implements ExceptionMapper<DuplicateUserException> {
    @Autowired
    private MessageSource messageSource;

    @Override
    public Response toResponse(DuplicateUserException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity("This user already exists").build();
    }
}