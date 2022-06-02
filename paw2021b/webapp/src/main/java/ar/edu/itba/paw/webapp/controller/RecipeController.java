package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.exceptions.RecipeNotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.dto.response.CategoryDto;
import ar.edu.itba.paw.webapp.dto.response.CommentDto;
import ar.edu.itba.paw.webapp.dto.response.IngredientDto;
import ar.edu.itba.paw.webapp.dto.response.RecipeDto;
import ar.edu.itba.paw.webapp.dto.validations.ImageTypeConstraint;
import org.glassfish.jersey.server.internal.scanning.ResourceFinderException;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.lang.reflect.Array;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static ar.edu.itba.paw.webapp.controller.UserController.setupPaginatedResponse;

@Path("recipes")
@Component
public class RecipeController {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private RecipeCategoryService recipeCategoryService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private MailService mailService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private SearchService searchService;
    @Autowired
    private int maxRequestSize;

    @Context
    private UriInfo uriInfo;
    @Context
    private SecurityContext securityContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeController.class);

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response recipe(@PathParam("id") final long idRecipe) {
        LOGGER.info("In /recipe/{} GET", idRecipe);
        final Recipe recipe = recipeService.findById(idRecipe).orElseThrow(BadRequestException::new);
        RecipeDto recipeDto = new RecipeDto(uriInfo, recipe);
        return Response.ok(recipeDto).build();
    }

    @GET
    @Path("/{id}/ingredients")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getRecipeIngredients(@PathParam("id") final long idRecipe) {
        LOGGER.info("In /recipe/{}/ingredients GET", idRecipe);
        final Recipe recipe = recipeService.findById(idRecipe).orElseThrow(BadRequestException::new);
        Set<Ingredient> ingredients = recipe.getIngredients();
        final Collection<IngredientDto> ingredientDtos = IngredientDto.mapIngredientToDto(ingredients, uriInfo);
        return Response.ok(new GenericEntity<Collection<IngredientDto>>(ingredientDtos) {
        }).build();
    }

    @GET
    @Path("/{id}/categories")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getRecipeCategories(@PathParam("id") final long idRecipe) {
        LOGGER.info("In /recipe/{}/categories GET", idRecipe);
        final Recipe recipe = recipeService.findById(idRecipe).orElseThrow(BadRequestException::new);
        Set<RecipeCategory> categories = recipe.getCategories();
        final Collection<CategoryDto> categoryDtos = CategoryDto.mapCategoryToDto(categories, uriInfo);
        return Response.ok(new GenericEntity<Collection<CategoryDto>>(categoryDtos) {
        }).build();
    }

    @GET
    @Path("/{id}/likes")
    @Produces(value = {MediaType.TEXT_PLAIN})
    public Response getRecipeLikes(@PathParam("id") final long idRecipe) {
        LOGGER.info("In /recipe/{}/likes GET", idRecipe);
        final Recipe recipe = recipeService.findById(idRecipe).orElseThrow(BadRequestException::new);
        long likes = recipeService.getLikes(recipe);
        return Response.ok(likes).build();
    }

    @GET
    @Path("/{id}/dislikes")
    @Produces(value = {MediaType.TEXT_PLAIN})
    public Response getRecipeDislikes(@PathParam("id") final long idRecipe) {
        LOGGER.info("In /recipe/{}/dislikes GET", idRecipe);
        final Recipe recipe = recipeService.findById(idRecipe).orElseThrow(BadRequestException::new);
        long dislikes = recipeService.getDislikes(recipe);
        return Response.ok(dislikes).build();
    }

    @GET
    @Path("/{recipeId}/images/{id}")
    @Produces({"image/*", MediaType.APPLICATION_JSON})
    public Response getRecipeImage(@PathParam("recipeId") final long idRecipe, @PathParam("id") long id) {
        LOGGER.info("In /recipe/{}/images/{} GET", idRecipe, id);
        final Recipe recipe = recipeService.findById(idRecipe).orElseThrow(BadRequestException::new);
        final Image image = imageService.findById(id).orElseThrow(ResourceFinderException::new);
        final byte[] img = image.getData();
        LOGGER.info("Got image with id {}", id);

        final CacheControl cacheControl = new CacheControl();
        cacheControl.setNoTransform(false);
        cacheControl.getCacheExtension().put("public",null);
        cacheControl.setMaxAge(31536000);
        cacheControl.getCacheExtension().put("mutable",null);

        return Response.ok(img).type(image.getMimeType()).cacheControl(cacheControl).build();
    }


    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getRecipes(
            @QueryParam("query") @DefaultValue("") String query,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("categories") List<Integer> categoriesQuery,
            @QueryParam("difficulty") @DefaultValue("0") int difficulty,
            @QueryParam("order") @DefaultValue("DATE_DESC") String order,
            @QueryParam("highlighted") @DefaultValue("false") boolean highlighted,
            @QueryParam("pageSize") @DefaultValue("4") int pageSize
    ) {
        LOGGER.info("Accessed /recipes/ GET controller");
        final PaginatedResult<Recipe> results = searchService.searchRecipes(query, categoriesQuery, difficulty, order, page, highlighted, pageSize);
        if (results == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final Collection<RecipeDto> recipeDto = RecipeDto.mapRecipeToDto(results.getResults(), uriInfo);

        final UriBuilder uriBuilder = uriInfo
                .getAbsolutePathBuilder()
                .queryParam("pageSize", pageSize);

        if (categoriesQuery != null) {
            uriBuilder.queryParam("category", categoriesQuery);
        }

        return setupPaginatedResponse(results, new GenericEntity<Collection<RecipeDto>>(recipeDto) {
        }, uriBuilder);
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response newRecipe(
            @Context final HttpServletRequest request,
            @Size(min = 2, max = 100, message = "{{Size.newRecipe.recipe_title}}")
            @NotEmpty(message = "{{NotEmpty.newRecipe.recipe_title}}")
            @FormDataParam("title") final String recipeTitle,
            @Size(min = 3, max = 500, message = "{{Size.newRecipe.recipe_title}}")
            @NotEmpty(message = "{{NotEmpty.newRecipe.recipe_desc}}")
            @FormDataParam("description") final String recipeDescription,
            @FormDataParam("ingredients") final List<String> recipeIngredients,
            @FormDataParam("time") final Integer recipeTime,
            @Size(min = 3, max = 500, message = "{{Size.newRecipe.recipe_title}}")
            @NotEmpty(message = "{{NotEmpty.newRecipe.recipe_steps}}")
            @FormDataParam("steps") final String recipeSteps,
            @Size(max = 10, message = "{Size.newRecipe.images}")
            @ImageTypeConstraint(contentType = {"image/png", "image/jpg", "image/jpeg"}, message = "{ContentType.newRecipe.images}")
            @FormDataParam("images") final List<FormDataBodyPart> recipeImages,
            @FormDataParam("difficulty") final Integer recipeDifficulty,
            @FormDataParam("categories") final List<Integer> recipeCategories
    ) throws IOException {

        final User user = userService.findByEmail(securityContext.getUserPrincipal().getName()).orElseThrow(RuntimeException::new);
        List<ImageUser> imagesToUpload = new LinkedList<>();
        if (recipeImages != null) {
            for (FormDataBodyPart part : recipeImages) {
                InputStream in = part.getEntityAs(InputStream.class);
                imagesToUpload.add(new ImageUser(user.getIdUser(), StreamUtils.copyToByteArray(in), part.getMediaType().toString()));
            }
        }

        final Recipe recipe = recipeService.manageRecipeCreation(null, user, recipeTitle, recipeDescription, recipeIngredients, recipeTime, recipeSteps, imagesToUpload, recipeDifficulty, recipeCategories);
        return Response.created(RecipeDto.getRecipeUriBuilder(recipe, uriInfo).build()).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRecipe(@PathParam("id") long id) {
        final User user = userService.findByEmail(securityContext.getUserPrincipal().getName()).orElseThrow(RuntimeException::new);
        final Recipe recipe = recipeService.findById(id).orElseThrow(RecipeNotFoundException::new);
        if(recipe.getUser().getIdUser() != user.getIdUser()){
            if(!userService.isAdmin(user))
                throw new BadRequestException();
        }
        recipeService.delete(recipe, user);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/comments")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response postComments(
            @Context final HttpServletRequest request,
            @PathParam("id") final long id,
            @FormDataParam("commentDesc") final String commentDesc) {
        final Recipe recipe;
        if (recipeService.findById(id).isPresent()) {
            recipe = recipeService.findById(id).get();
        } else {
            throw new BadRequestException();
        }
        final User user = userService.findByEmail(securityContext.getUserPrincipal().getName()).orElseThrow(RuntimeException::new);
        LOGGER.info("Posting a comment");
        final Comment comment = commentService.create(recipe, user, commentDesc);
        return Response.created(CommentDto.getCommentUriBuilder(comment, uriInfo).build()).build();
    }

    @DELETE
    @Path("/{id}/comments/{idComment}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteComment(@PathParam("id") long id, @PathParam("idComment") long idComment) {
        LOGGER.info("Deleting comment");
        final Optional<Comment> comment = commentService.findById(idComment);
        if(!comment.isPresent()){
            LOGGER.info("Comment not found");
            throw new BadRequestException();
        }

        final User user = userService.findByEmail(securityContext.getUserPrincipal().getName()).orElseThrow(RuntimeException::new);
        if(comment.get().getUser().getIdUser() != user.getIdUser()){
            if(!userService.isAdmin(user)){
                LOGGER.info("Not allowed to delete");
                throw new BadRequestException();
            }

        }
        commentService.delete(idComment);
        return Response.noContent().build();
    }

    @GET
    @Path("{id}/suggested")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getSuggestedRecipes(@PathParam("id") final long id) {
        LOGGER.info("Getting suggested recipes");
        final Recipe recipe;
        if (recipeService.findById(id).isPresent()) {
            recipe = recipeService.findById(id).get();
        } else {
            throw new BadRequestException();
        }
        final List<Recipe> suggested = recipeService.getSimilarRecipes(recipe);
        final Collection<RecipeDto> recipeDtos = RecipeDto.mapRecipeToDto(suggested, uriInfo);
        return Response.ok(new GenericEntity<Collection<RecipeDto>>(recipeDtos) {
        }).build();
    }

    @GET
    @Path("/{id}/comments/")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getComments(@PathParam("id") final long id, @QueryParam("page") @DefaultValue("0") int page, @QueryParam("pageSize") @DefaultValue("6") int pageSize) {
        final Recipe recipe;
        if (recipeService.findById(id).isPresent()) {
            recipe = recipeService.findById(id).get();
        } else {
            throw new BadRequestException();
        }
        LOGGER.info("Getting comments");
        final PaginatedResult<Comment> comments = commentService.findByRecipeId(recipe, page);
        final Collection<CommentDto> commentDtos = CommentDto.mapCommentToDto(comments.getResults(), uriInfo);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().queryParam("pageSize", pageSize);
        return setupPaginatedResponse(comments, new GenericEntity<Collection<CommentDto>>(commentDtos){},uriBuilder);
    }

    @PUT
    @Path("/{id}/likes")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response like(@PathParam("id") final long idRecipe) {
        LOGGER.info("In /recipe/{}/rate for like", idRecipe);
        final User user = userService.findByEmail(securityContext.getUserPrincipal().getName()).orElseThrow(UserNotFoundException::new);

        final Recipe recipe = recipeService.findById(idRecipe).orElseThrow(RecipeNotFoundException::new);
        recipeService.likeRecipe(recipe, user);

        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/liked")
    public Response isLiked(@PathParam("id") long id) {
        final User user = userService.findByEmail(securityContext.getUserPrincipal().getName()).orElseThrow(UserNotFoundException::new);

        final Recipe recipe = recipeService.findById(id).orElseThrow(RecipeNotFoundException::new);

        final boolean isLiked = recipeService.isLiked(recipe, user);

        return Response.ok(isLiked).build();
    }

    @PUT
    @Path("/{id}/dislikes")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response dislike(@PathParam("id") final long idRecipe) {
        LOGGER.info("In /recipe/{} for dislike", idRecipe);
        final User user = userService.findByEmail(securityContext.getUserPrincipal().getName()).orElseThrow(UserNotFoundException::new);

        final Recipe recipe = recipeService.findById(idRecipe).orElseThrow(RecipeNotFoundException::new);
        recipeService.dislikeRecipe(recipe, user);

        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/disliked")
    public Response isDisliked(@PathParam("id") long id) {
        final User user = userService.findByEmail(securityContext.getUserPrincipal().getName()).orElseThrow(UserNotFoundException::new);

        final Recipe recipe = recipeService.findById(id).orElseThrow(RecipeNotFoundException::new);

        final boolean isDisliked = recipeService.isDisliked(recipe, user);

        return Response.ok(isDisliked).build();
    }

    @GET
    @Path("/{id}/likedPercentage")
    public Response likedPercentage(@PathParam("id") long id) {
        final Recipe recipe = recipeService.findById(id).orElseThrow(RecipeNotFoundException::new);

        final double likedPercentage = recipeService.likePercentage(recipe);

        return Response.ok(likedPercentage).build();
    }

    @GET
    @Path("/{id}/likedReviews")
    public Response likedReviews(@PathParam("id") long id) {
        final Recipe recipe = recipeService.findById(id).orElseThrow(RecipeNotFoundException::new);

        long totalReviews = recipeService.getLikes(recipe) + recipeService.getDislikes(recipe);

        return Response.ok(totalReviews).build();
    }

    @DELETE
    @Path("/{id}/favorite")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response unfav(@PathParam("id") final long idRecipe) {
        LOGGER.info("In /recipe/{}/favorite", idRecipe);
        final User user = userService.findByEmail(securityContext.getUserPrincipal().getName()).orElseThrow(UserNotFoundException::new);

        final Recipe recipe = recipeService.findById(idRecipe).orElseThrow(RecipeNotFoundException::new);
        recipeService.unfavRecipe(recipe, user);

        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/favorite")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response fav(@PathParam("id") final long idRecipe) {
        LOGGER.info("In /recipe/{}/favorite", idRecipe);
        final User user = userService.findByEmail(securityContext.getUserPrincipal().getName()).orElseThrow(UserNotFoundException::new);

        final Recipe recipe = recipeService.findById(idRecipe).orElseThrow(RecipeNotFoundException::new);
        recipeService.favRecipe(recipe, user);

        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/faved")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isFaved(@PathParam("id") long id) {
        final User user = userService.findByEmail(securityContext.getUserPrincipal().getName()).orElseThrow(UserNotFoundException::new);

        final Recipe recipe = recipeService.findById(id).orElseThrow(RecipeNotFoundException::new);

        final boolean isFaved = recipeService.isFavorited(recipe, user);

        if (!isFaved) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/shared")
    public Response share(
            @Context final HttpServletRequest request,
            @Size(min = 2, max = 100, message = "{{Size.newRecipe.recipe_title}}")
            @NotEmpty(message = "{{NotEmpty.newRecipe.recipe_title}}")
            @FormDataParam("name") final String name,
            @Size(min = 3, max = 500, message = "{{Size.newRecipe.recipe_title}}")
            @NotEmpty(message = "{{NotEmpty.newRecipe.recipe_desc}}")
            @FormDataParam("email") final String email,
            @PathParam("id") long id) {
        LOGGER.info("In /recipe/{}/shared", id);
        if (recipeService.findById(id).isPresent()) {
            final Recipe recipe = recipeService.findById(id).get();
            mailService.sendShareRecipeEmail(name, email, recipe.getRecipeTitle(), recipe.getIdRecipe());
        }

        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/highlighted")
    @Produces(MediaType.APPLICATION_JSON)
    public Response highlightRecipe(@PathParam("id") long id) {
        LOGGER.info("In /recipe/{}/highlightRecipe", id);
        final User user = userService.findByEmail(securityContext.getUserPrincipal().getName()).orElseThrow(UserNotFoundException::new);
        if(userService.isAdmin(user)) {
            if (recipeService.findById(id).isPresent()) {
                recipeService.highlightRecipe(recipeService.findById(id).get());
            }
        }
        return Response.noContent().build();
    }

}
/*

@RequestMapping("/recipe/{idRecipe}")
    public ModelAndView recipeId(@PathVariable("idRecipe") final Long idRecipe,
                                 @ModelAttribute("shareRecipeForm") final ShareForm form,
                                 @ModelAttribute("commentForm") final CommentForm commentForm,
                                 @ModelAttribute("ratingForm") final RatingForm ratingForm,
                                 @ModelAttribute("paginationForm") final PaginationForm paginationForm,
                                 HttpServletRequest request,
                                 Principal principal, final Model model) {
        LOGGER.info("In /recipe/{}", idRecipe);
        final Recipe recipe;
        if(recipeService.findById(idRecipe).isPresent()){
            recipe = recipeService.findById(idRecipe).get();
            LOGGER.debug("Recipe is {}", recipe);
        }else{
            throw new BadRequestException();
        }
        final User author;
        if(userService.findById(recipe.getUser().getIdUser()).isPresent()){
            author = userService.findById(recipe.getUser().getIdUser()).get();
        }else{
            throw new ResourceNotFoundException();
        }
        final List<Ingredient> ingredients = ingredientService.findByRecipeId(recipe);
        PaginatedResult<Comment> comments = commentService.findByRecipeId(recipe,paginationForm.getPage());
        if(paginationForm.getPage() >= comments.getTotalPages()){
            comments = commentService.findByRecipeId(recipe,comments.getTotalPages()-1);
        }
        final ModelAndView mav = new ModelAndView("/recipePage");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            if (flashMap.get("shareError") != null) {
                mav.addObject("shareError", true);
                model.asMap().put("org.springframework.validation.BindingResult.shareRecipeForm", flashMap.get("shareRecipeFormError"));
            }
        }
        if (principal != null && userService.findByUsername(principal.getName()).isPresent()) {
            final User userLogged = userService.findByUsername(principal.getName()).get();
            mav.addObject("userLogged", userLogged);
            mav.addObject("userLoggedId",userLogged.getIdUser());

        } else {
            mav.addObject("userLoggedId", -1);
        }


        List<Recipe> suggested = recipeService.getSimilarRecipes(recipe);


        long totalComments = commentService.findByRecipeIdCount(recipe);
        long totalReviews = recipeService.getLikes(recipe) + recipeService.getDislikes(recipe);
        mav.addObject("totalComments", totalComments);
        mav.addObject("userService",userService);
        mav.addObject("author", author);
        mav.addObject("recipe", recipe);
        mav.addObject("recipeCategoryService", recipeCategoryService);
        mav.addObject("ingredients", ingredients);
        mav.addObject("imageService", imageService);
        mav.addObject("commentService", commentService);
        mav.addObject("comments", comments);
        mav.addObject("suggested", suggested);
        mav.addObject("recipeService", recipeService);
        mav.addObject("totalReviews",totalReviews);
        mav.addObject("actualpage",paginationForm.getPage());
        mav.addObject("lastpage",comments.getTotalPages()-1);
        return mav;
    }

    @RequestMapping(value = "/recipe/{idRecipe}/share", method = {RequestMethod.POST})
    public ModelAndView share(@Valid @ModelAttribute("shareRecipeForm") final ShareForm form, final BindingResult error, @PathVariable("idRecipe") final Long idRecipe, RedirectAttributes redirect) {
        LOGGER.info("In /recipe/{}/share", idRecipe);
        if (error.hasErrors()) {
            LOGGER.warn("Error in sharing recipe with id: {}", idRecipe);
            redirect.addFlashAttribute("shareRecipeFormError", error);
            redirect.addFlashAttribute("shareError", true);
            redirect.addFlashAttribute("shareRecipeForm", form);
            return new ModelAndView("redirect:/recipe/{idRecipe}");
        }
        if(recipeService.findById(idRecipe).isPresent()){
            final Recipe recipe = recipeService.findById(idRecipe).get();
            mailService.sendShareRecipeEmail(form.getFrom(), form.getEmail(), recipe.getRecipeTitle(), recipe.getIdRecipe());
            return new ModelAndView("redirect:/recipe/{idRecipe}");
        }else {
            throw new BadRequestException();
        }
    }

    @RequestMapping(value = "/recipe/{idRecipe}/rate", method = {RequestMethod.POST}, params = {"like"})
    public ModelAndView like(@RequestParam boolean like, @PathVariable("idRecipe") final Long idRecipe, Principal principal, final Integer error) {
        LOGGER.info("In /recipe/{}/rate for like", idRecipe);
        if(userService.findByUsername(principal.getName()).isPresent()){
            recipeService.likeRecipe(recipeService.findById(idRecipe).get(), userService.findByUsername(principal.getName()).get());
            return new ModelAndView("redirect:/recipe/{idRecipe}");
        }else{
            throw new BadRequestException();
        }
    }

    @RequestMapping(value = "/recipe/{idRecipe}/rate", method = {RequestMethod.POST}, params = {"dislike"})
    public ModelAndView dislike(@RequestParam boolean dislike, @PathVariable("idRecipe") final Long idRecipe, Principal principal, final Integer error) {
        LOGGER.info("In /recipe/{} for dislike", idRecipe);
        if(userService.findByUsername(principal.getName()).isPresent()){
            recipeService.dislikeRecipe(recipeService.findById(idRecipe).get(), userService.findByUsername(principal.getName()).get());
            return new ModelAndView("redirect:/recipe/{idRecipe}");
        }else{
            throw new BadRequestException();
        }
    }

    @RequestMapping(path = "/recipe/{idRecipe}/{page}", method = {RequestMethod.POST})
    public ModelAndView recipeComment(@Valid @ModelAttribute("commentForm") final CommentForm commentForm, @PathVariable("idRecipe") final Long idRecipe, @PathVariable("page") int page, final Integer error, Principal principal) {
        LOGGER.info("In /recipe/{} POST", idRecipe);
        if(recipeService.findById(idRecipe).isPresent() && userService.findByUsername(principal.getName()).isPresent()){
            final Recipe recipe = recipeService.findById(idRecipe).get();
            final User user = userService.findByUsername(principal.getName()).get();
            final Comment comment = commentService.create(recipe, user, commentForm.getDescription());
            String newPath = "redirect:/recipe/{idRecipe}?page={page}";
            return new ModelAndView(newPath);
        }else{
            throw new BadRequestException();
        }
    }

    @RequestMapping(value = "recipe/{idRecipe}/favorite")
    public ModelAndView favorite(@PathVariable("idRecipe") final Long idRecipe, Principal principal, final Integer error){
        LOGGER.info("In /recipe/{}/favorite", idRecipe);
        if(userService.findByUsername(principal.getName()).isPresent()){
            recipeService.favoriteRecipe(recipeService.findById(idRecipe).get(), userService.findByUsername(principal.getName()).get());
            return new ModelAndView("redirect:/recipe/{idRecipe}");
        }else{
            throw new BadRequestException();
        }
    }

    @RequestMapping(value = "recipe/{idRecipe}/highlightRecipe", method = {RequestMethod.POST})
    public ModelAndView highlightRecipe(@PathVariable("idRecipe") final Long idRecipe, Principal principal, final Integer error) {
        LOGGER.info("In /recipe/{}/highlightRecipe", idRecipe);
        if(userService.findByUsername(principal.getName()).isPresent()) {
            if (userService.isAdmin(userService.findByUsername(principal.getName()).get())) {
                if(recipeService.findById(idRecipe).isPresent()){
                    recipeService.highlightRecipe(recipeService.findById(idRecipe).get());
                }
            }
            return new ModelAndView("redirect:/recipe/{idRecipe}");
        }
        else {
            throw new BadRequestException();
        }
    }

    @RequestMapping(value = "/recipe/{idRecipe}/delete", method = {RequestMethod.POST})
    public ModelAndView deleteRecipe(@PathVariable("idRecipe") final Long idRecipe, Principal principal){
        LOGGER.info("In /recipe/{}/delete", idRecipe);
        if(userService.findByUsername(principal.getName()).isPresent()){
            recipeService.delete(recipeService.findById(idRecipe).get(), userService.findByUsername(principal.getName()).get());
            return new ModelAndView("redirect:/");
        }else{
            throw new BadRequestException();
        }
    }

    @RequestMapping(value = "/recipe/{idRecipe}/deleteCom/{idComment}/{page}", method = {RequestMethod.POST})
    public ModelAndView deleteComment(@PathVariable("idRecipe") final Long idRecipe, @PathVariable("idComment") final Long idComment, @PathVariable("page") final int page){
        LOGGER.info("In /recipe/{}/deleteCom/{}", idRecipe, idComment);
        commentService.delete(idComment);
        String newPath = "redirect:/recipe/{idRecipe}?page={page}";
        return new ModelAndView(newPath);
    }
 */