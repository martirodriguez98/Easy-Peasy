package ar.edu.itba.paw.webapp.dto.response;

import ar.edu.itba.paw.models.RecipeCategory;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.stream.Collectors;

@XmlType(name= "")
public class CategoryDto {

    private long id;
    private int category;

    private String url;
    private String recipeUrl;

    public static Collection<CategoryDto> mapCategoryToDto(Collection<RecipeCategory> categories, UriInfo uriInfo){
        return categories.stream().map(c->new CategoryDto(uriInfo,c)).collect(Collectors.toList());
    }

    public static UriBuilder getCategoryUriBuilder(RecipeCategory category, UriInfo uriInfo){
        return uriInfo.getBaseUriBuilder().path("categories").path(String.valueOf(category.getId()));
    }

    public CategoryDto(){

    }

    public CategoryDto(UriInfo uri, RecipeCategory category){
        this.id = category.getId();
        this.category = category.getCategory();
        this.url = getCategoryUriBuilder(category, uri).build().toString();
        this.recipeUrl = uri.getBaseUriBuilder().path("recipes").path(String.valueOf(category.getRecipe().getIdRecipe())).build().toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
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
