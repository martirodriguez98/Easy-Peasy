package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.exceptions.DuplicateUserException;
import ar.edu.itba.paw.interfaces.exceptions.RecipeNotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.UserDetailsServiceImpl;
import ar.edu.itba.paw.webapp.dto.request.RegisterDto;
import ar.edu.itba.paw.webapp.dto.request.ResetPasswordDto;
import ar.edu.itba.paw.webapp.dto.request.ResetPasswordEmailDto;
import ar.edu.itba.paw.webapp.dto.response.RecipeDto;
import ar.edu.itba.paw.webapp.dto.response.ReportDto;
import ar.edu.itba.paw.webapp.dto.response.UserDto;
import ar.edu.itba.paw.webapp.dto.validations.ImageTypeConstraint;
import ar.edu.itba.paw.webapp.exceptions.BadRequestException;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.beans.Encoder;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Path("users")
@Component
public class UserController {
    @Autowired
    private UserService us;
    @Autowired
    RecipeService recipeService;
    @Autowired
    private SearchService searchService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RoleService roleService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Context
    private UriInfo uriInfo;
    @Context
    private SecurityContext securityContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getUser(@PathParam("id") final long id) {
        LOGGER.info("In /users/{} GET", id);
        final User user = us.findById(id).orElseThrow(BadRequestException::new);
        return Response.ok(new UserDto(uriInfo, user)).build();
    }

    @GET
    @Path("/{id}/avatar")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getUserAvatar(@PathParam("id") final long id, @Context Request request) {
        LOGGER.info("In /users/{}/avatar GET", id);
        final User user = us.findById(id).orElseThrow(BadRequestException::new);
        final byte[] image = user.getAvatar();
        if (image == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        final EntityTag eTag = new EntityTag(String.valueOf(Arrays.toString(image).hashCode()));
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(eTag);
        if (responseBuilder == null){
            responseBuilder = Response.ok(image).type(user.getMimeType()).tag(eTag);
        }
        return responseBuilder.cacheControl(cacheControl).build();
    }

    @GET
    @Path("{id}/recipes")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getUserRecipes(@PathParam("id") final long id, @QueryParam("page") @DefaultValue("0") int page, @QueryParam("pageSize") @DefaultValue("4") int pageSize) {
        LOGGER.info("In /users/{}/recipes getting user's recipes", id);
        User user = us.findById(id).orElseThrow(BadRequestException::new);
        final PaginatedResult<Recipe> recipes = recipeService.findByAuthorId(user, page);
        final Collection<RecipeDto> recipeDtos = RecipeDto.mapRecipeToDto(recipes.getResults(), uriInfo);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().queryParam("pageSize", pageSize);
        return setupPaginatedResponse(recipes, new GenericEntity<Collection<RecipeDto>>(recipeDtos) {
        }, uriBuilder);
    }

    @GET
    @Path("{id}/favourites")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getUserFavourites(@PathParam("id") final long id, @QueryParam("page") @DefaultValue("0") int page, @QueryParam("pageSize") @DefaultValue("4") int pageSize) {
        LOGGER.info("In /users/{}/favourites getting user's favourites", id);
        User user = us.findById(id).orElseThrow(BadRequestException::new);
        final PaginatedResult<Recipe> favourites = recipeService.findFavouritesByUserId(user, page);
        final Collection<RecipeDto> recipeDtos = RecipeDto.mapRecipeToDto(favourites.getResults(), uriInfo);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().queryParam("pageSize", pageSize);
        return setupPaginatedResponse(favourites, new GenericEntity<Collection<RecipeDto>>(recipeDtos) {
        }, uriBuilder);
    }

    @PUT
    @Path("/verification")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response verifyAccount(@QueryParam("token") String token){
        LOGGER.info("In /verification with token {}", token);
        long successId = us.verifyAccount(token);
        if(successId != -1){
            LOGGER.info("Updating authorities");
            Optional<User> user = us.findById(successId);
            if(!user.isPresent())
                throw new BadRequestException();
            userDetailsService.updateVerifiedAuthority(user.get().getUsername());
            LOGGER.info("Verification successful");
            return Response.ok().header("verified", "true").build();
        }
        else{
            LOGGER.info("Failed to verify");
            return Response.ok().header("verified", "false").build();
        }
    }

    @POST
    @Path("/verification")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response resendVerification(){

        LOGGER.info("In /verify/resend");
        Optional<User> user = us.findByEmail(securityContext.getUserPrincipal().getName());
        if(!user.isPresent())
            throw new BadRequestException();
        boolean error = us.resendVerificationToken(user.get().getUsername());
        if(error){
            throw new BadRequestException();
        }
        return Response.ok().build();
    }

    @GET
    @Path("/admin")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getAdmins(@QueryParam("page") @DefaultValue("0") int page, @QueryParam("pageSize") @DefaultValue("6") int pageSize, @QueryParam("orderBy") @DefaultValue("BY_ID") String order) {

        LOGGER.info("In /users/admin/ getting all admins");
        final PaginatedResult<User> admins = us.findAdmins(order, page);
        final Collection<UserDto> adminDtos = UserDto.mapUserToDto(admins.getResults(), uriInfo);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().queryParam("pageSize", pageSize).queryParam("order", order);
        return setupPaginatedResponse(admins, new GenericEntity<Collection<UserDto>>(adminDtos) {
        }, uriBuilder);
    }

    @GET
    @Path("/banned")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getBanned(@QueryParam("page") @DefaultValue("0") int page, @QueryParam("pageSize") @DefaultValue("6") int pageSize, @QueryParam("orderBy") @DefaultValue("BY_ID") String order) {
        LOGGER.info("In /users/banned/ getting all banned users");
        final PaginatedResult<User> banned = us.findBannedUsers(order, page);
        final Collection<UserDto> bannedDtos = UserDto.mapUserToDto(banned.getResults(), uriInfo);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().queryParam("pageSize", pageSize).queryParam("order", order);
        return setupPaginatedResponse(banned, new GenericEntity<Collection<UserDto>>(bannedDtos) {
        }, uriBuilder);
    }

    @PUT
    @Path("/banned/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response banUser(@PathParam("id") long id) {
        LOGGER.info("In /users/banned/{}", id);
        if (!us.banUser(id)) {
            throw new BadRequestException();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/banned/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response unbanUser(@PathParam("id") long id) {
        LOGGER.info("In /users/banned/{}", id);
        if (!us.unbanUser(id)) {
            throw new BadRequestException();
        }
        return Response.ok().build();
    }

    @PUT
    @Path("/admin/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response makeAdminUser(@PathParam("id") long id) {
        LOGGER.info("In /users/admin/{} put", id);
        if (!roleService.makeAdmin(id)) {
            throw new BadRequestException();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/admin/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response removeAdminUser(@PathParam("id") long id) {
        LOGGER.info("In /users/admin/{} delete", id);
        if (!roleService.removeAdmin(id)) {
            throw new BadRequestException();
        }
        return Response.ok().build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/passwordResetEmail")
    public Response reqResetPassword(@Valid final ResetPasswordEmailDto resetPasswordEmailDto) {
        LOGGER.info("Accessed /users/passwordResetEmail POST controller");
        if (resetPasswordEmailDto == null) {
            throw new NullPointerException();
        }

        final User user = us.findByEmail(resetPasswordEmailDto.getEmail()).orElseThrow(UserNotFoundException::new);
        us.sendPasswordToken(user.getEmail());
        return Response.noContent().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/passwordReset")
    public Response resetPassword(@Valid final ResetPasswordDto resetPasswordDto) {
        LOGGER.info("Accessed /users/passwordReset PUT controller");
        if (resetPasswordDto == null) {
            throw new NullPointerException();
        }
        us.resetPassword(resetPasswordDto.getToken(), resetPasswordDto.getPassword());
        return Response.noContent().build();
    }


    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response registerUser(@Valid final RegisterDto registerDto) throws DuplicateUserException {
        LOGGER.info("Accessed /users/ POST controller");
        if (registerDto == null) {
            throw new NullPointerException();
        }
        User user;
        try {
            user = us.create(registerDto.getUsername(), registerDto.getEmail(), registerDto.getPassword());
        } catch (DuplicateUserException e) {
            LOGGER.warn("Error in registerForm, email is already used");
            throw new DuplicateUserException();
        }
        return Response.created(UserDto.getUserUriBuilder(user, uriInfo).build()).build();
    }

    static <T, K> Response setupPaginatedResponse(PaginatedResult<T> res, GenericEntity<K> resultDto, UriBuilder uriBuilder) {
        if (res.getResults().isEmpty()) {
            if (res.getPage() != 0 && res.getPage() != -1) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.noContent().build();
            }
        }
        Response.ResponseBuilder responseBuilder = Response.ok(resultDto);
        int first = 0;
        int last = res.getTotalPages() - 1;
        int page = res.getPage();
        int prev = page - 1;
        int next = page + 1;

        responseBuilder.link(uriBuilder.clone().queryParam("page", first).build(), "first");
        responseBuilder.link(uriBuilder.clone().queryParam("page", last).build(), "last");
        if (page != first) {
            responseBuilder.link(uriBuilder.clone().queryParam("page", prev).build(), "prev");
        }
        if (page != last) {
            responseBuilder.link(uriBuilder.clone().queryParam("page", next).build(), "next");
        }

        return responseBuilder.build();
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getUsers(
            @QueryParam("query") @DefaultValue("") String query,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("admins") @DefaultValue("false") boolean admins,
            @QueryParam("order") @DefaultValue("NAME_ASC") String order,
            @QueryParam("highlighted") @DefaultValue("false") boolean highlighted
    ) {
        LOGGER.info("Accessed /users/ GET controller");
        final PaginatedResult<User> results = searchService.searchUsers(query, highlighted, admins, order, page);

        if (results == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final Collection<UserDto> userDto = UserDto.mapUserToDto(results.getResults(), uriInfo);

        final UriBuilder uriBuilder = uriInfo
                .getAbsolutePathBuilder();

        return setupPaginatedResponse(results, new GenericEntity<Collection<UserDto>>(userDto) {
        }, uriBuilder);

//        return Response.ok(new GenericEntity<Collection<UserDto>>(userDto){}).build();
    }

    @PUT
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/{id}/profileImage")
    public Response changeImage(@Context final HttpServletRequest request,
                                @NotNull(message = "{NotEmpty.profile.image}")
                                @ImageTypeConstraint(contentType = {"image/png", "image/jpeg", "image/gif"}, message = "{ContentType.profile.image}")
                                @FormDataParam("userImage") FormDataBodyPart profileImage,
                                @PathParam("id") final long id) throws IOException {
        LOGGER.info("Accessed /{}/profileImage PUT controller", id);
        final User user = us.findByEmail(securityContext.getUserPrincipal().getName()).orElseThrow(UserNotFoundException::new);
        InputStream in=profileImage.getEntityAs(InputStream.class);
        ImageUser userimg=new ImageUser(user.getIdUser(), StreamUtils.copyToByteArray(in),profileImage.getMediaType().toString());
        us.changeAvatar(id, userimg.getData(),userimg.getMimeType());

        return Response.ok().build();
    }

    @POST
    @Path("/reports")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response reportUser(
            @Context final HttpServletRequest request,
            @FormDataParam("reportDesc") final String desc,
            @FormDataParam("reportedUsername") final String reportedUsername,
            @FormDataParam("commentId") final String commentId
    ){
        final User reporter = us.findByEmail(securityContext.getUserPrincipal().getName()).orElseThrow(RuntimeException::new);
        final User reported = us.findByUsername(reportedUsername).orElseThrow(RuntimeException::new);
        final Comment comment = commentService.findById(Long.parseLong(commentId)).orElseThrow(RuntimeException::new);
        LOGGER.info("Posting report");
        final Reports report = us.reportUser(desc,reporter,reported,comment);
        return Response.created(ReportDto.getReportUriBuilder(report,uriInfo).build()).build();

    }

    @GET
    @Path("/reports")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getReports(@QueryParam("page") @DefaultValue("0") int page, @QueryParam("pageSize") @DefaultValue("6") int pageSize, @QueryParam("orderBy") @DefaultValue("BY_ID") String order) {
        LOGGER.info("In /users/reports/ getting all reports");
        final PaginatedResult<Reports> reports = us.getReports(order, page);
        final Collection<ReportDto> reportDtos = ReportDto.mapReportToDto(reports.getResults(), uriInfo);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().queryParam("pageSize", pageSize).queryParam("order", order);
        return setupPaginatedResponse(reports, new GenericEntity<Collection<ReportDto>>(reportDtos) {
        }, uriBuilder);
    }

    @DELETE
    @Path("/reports/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response deleteReport(@PathParam("id") final long id) {
        LOGGER.info("Deleting report");
        us.deleteReport(id);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{id}/reports")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response deleteUserReports(@PathParam("id") final long id) {
        LOGGER.info("Deleting report");
        final User user = us.findById(id).orElseThrow(RuntimeException::new);
        us.deleteUserReports(user);
        return Response.noContent().build();
    }


}