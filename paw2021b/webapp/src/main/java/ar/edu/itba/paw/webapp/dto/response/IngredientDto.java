package ar.edu.itba.paw.webapp.dto.response;

import ar.edu.itba.paw.models.Ingredient;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.stream.Collectors;

public class IngredientDto {

    private long id;
    private String ingredientName;
    private String ingredientQuantity;

    private String url;
    private String recipeUrl;

    public static Collection<IngredientDto> mapIngredientToDto(Collection<Ingredient> ingredients, UriInfo uriInfo){
        return ingredients.stream().map(i->new IngredientDto(uriInfo,i)).collect(Collectors.toList());
    }
    public static UriBuilder getIngredientUriBuilder(Ingredient ingredient, UriInfo uriInfo){
        return uriInfo.getBaseUriBuilder().path("ingredients").path(String.valueOf(ingredient.getIdIngredient()));
    }

    public IngredientDto(){

    }

    public IngredientDto(UriInfo uriInfo, Ingredient ingredient){
        this.id = ingredient.getIdIngredient();
        this.ingredientName = ingredient.getIngredientName();
        this.ingredientQuantity = ingredient.getIngredientQuantity();

        this.url = getIngredientUriBuilder(ingredient, uriInfo).build().toString();
        this.recipeUrl = uriInfo.getBaseUriBuilder().path("recipes").path(String.valueOf(ingredient.getRecipe().getIdRecipe())).build().toString();

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(String ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }
}
