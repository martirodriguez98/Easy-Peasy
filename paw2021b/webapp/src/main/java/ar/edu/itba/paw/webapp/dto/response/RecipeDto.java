package ar.edu.itba.paw.webapp.dto.response;

import ar.edu.itba.paw.models.*;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeDto {

        private Long id;
        private String recipeTitle;
        private String recipeDesc;
        private String recipeSteps;
        private int recipeDifficulty;
        private boolean isHighlighted;
        private String recipeTime;
        private LocalDate recipeDateCreated;
        private long likesCount;
        private UserDto author;
        private long commentsCount;

        private String url;
        private String commentsUrl;
        private String ingredientsUrl;
        private String categoriesUrl;
        private String likesUrl;
        private String dislikesUrl;
        private Set<String> imageUrls;


        public static UriBuilder getRecipeUriBuilder(Recipe recipe, UriInfo uri){
                return uri.getBaseUriBuilder().path("recipes").path(String.valueOf(recipe.getIdRecipe()));
        }

        public static Collection<RecipeDto> mapRecipeToDto(Collection<Recipe> recipes, UriInfo uri){
                return recipes.stream().map(r -> new RecipeDto(uri, r)).collect(Collectors.toList());
        }

        public RecipeDto(){

        }

        public RecipeDto(UriInfo uri, Recipe recipe) {
                UriBuilder uriBuilder = getRecipeUriBuilder(recipe, uri);
                this.id = recipe.getIdRecipe();
                this.recipeTitle = recipe.getRecipeTitle();
                this.recipeDesc = recipe.getRecipeDesc();
                this.recipeSteps = recipe.getRecipeSteps();
                this.recipeDifficulty = recipe.getRecipeDifficulty();
                this.isHighlighted = recipe.isHighlighted();
                this.recipeTime = recipe.getRecipeTime();
                this.author = new UserDto(uri, recipe.getUser());
                this.recipeDateCreated = recipe.getRecipeDateCreated();
                this.likesCount = recipe.getLikesCount();
                this.url = uriBuilder.build().toString();
                this.commentsUrl = uriBuilder.clone().path("comments").build().toString();
                this.categoriesUrl = uriBuilder.clone().path("categories").build().toString();
                this.ingredientsUrl = uriBuilder.clone().path("ingredients").build().toString();
                this.likesUrl = uriBuilder.clone().path("likes").build().toString();
                this.dislikesUrl = uriBuilder.clone().path("dislikes").build().toString();
                this.imageUrls = recipe.getImages().stream().map(i -> getRecipeUriBuilder(recipe, uri).clone().path("/images").path(String.valueOf(i.getImageId())).build().toString()).collect(Collectors.toSet());
                this.commentsCount = recipe.getComments().size();
        }

        public long getCommentsCount() {
                return commentsCount;
        }

        public void setCommentsCount(long commentsCount) {
                this.commentsCount = commentsCount;
        }

        public Long getIdRecipe() {
                return id;
        }

        public void setIdRecipe(Long id) {
                this.id = id;
        }

        public String getLikesUrl() {
                return likesUrl;
        }

        public void setLikesUrl(String likesUrl) {
                this.likesUrl = likesUrl;
        }

        public String getDislikesUrl() {
                return dislikesUrl;
        }

        public UserDto getAuthor() {
                return author;
        }

        public void setAuthor(UserDto author) {
                this.author = author;
        }

        public void setDislikesUrl(String dislikesUrl) {
                this.dislikesUrl = dislikesUrl;
        }

        public String getRecipeTitle() {
                return recipeTitle;
        }

        public void setRecipeTitle(String recipeTitle) {
                this.recipeTitle = recipeTitle;
        }

        public String getRecipeDesc() {
                return recipeDesc;
        }

        public String getUrl() {
                return url;
        }

        public void setUrl(String url) {
                this.url = url;
        }

        public String getCommentsUrl() {
                return commentsUrl;
        }

        public void setCommentsUrl(String commentsUrl) {
                this.commentsUrl = commentsUrl;
        }

        public void setRecipeDesc(String recipeDesc) {
                this.recipeDesc = recipeDesc;
        }

        public String getRecipeSteps() {
                return recipeSteps;
        }

        public void setRecipeSteps(String recipeSteps) {
                this.recipeSteps = recipeSteps;
        }

        public int getRecipeDifficulty() {
                return recipeDifficulty;
        }

        public void setRecipeDifficulty(int recipeDifficulty) {
                this.recipeDifficulty = recipeDifficulty;
        }

        public boolean isHighlighted() {
                return isHighlighted;
        }

        public void setHighlighted(boolean highlighted) {
                isHighlighted = highlighted;
        }

        public String getRecipeTime() {
                return recipeTime;
        }

        public void setRecipeTime(String recipeTime) {
                this.recipeTime = recipeTime;
        }

        public LocalDate getRecipeDateCreated() {
                return recipeDateCreated;
        }

        public void setRecipeDateCreated(LocalDate recipeDateCreated) {
                this.recipeDateCreated = recipeDateCreated;
        }

        public Set<String> getImageUrls() {
                return imageUrls;
        }

        public void setImageUrls(Set<String> imageUrls) {
                this.imageUrls = imageUrls;
        }

        public String getIngredientsUrl() {
                return ingredientsUrl;
        }

        public void setIngredientsUrl(String ingredientsUrl) {
                this.ingredientsUrl = ingredientsUrl;
        }

        public String getCategoriesUrl() {
                return categoriesUrl;
        }

        public void setCategoriesUrl(String categoriesUrl) {
                this.categoriesUrl = categoriesUrl;
        }

        public long getLikesCount() {
                return likesCount;
        }

        public void setLikesCount(long likesCount) {
                this.likesCount = likesCount;
        }


}

