package ar.edu.itba.paw.webapp.dto.response;

import ar.edu.itba.paw.models.DislikedRecipes;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.stream.Collectors;

public class DislikesDto {
    private long id;
    private String url;
    private String recipeUrl;
    private String userUrl;

    public static Collection<DislikesDto> mapDislikeToDto(Collection<DislikedRecipes> dislikes, UriInfo uriInfo){
        return dislikes.stream().map(d->new DislikesDto(uriInfo,d)).collect(Collectors.toList());
    }
    public static UriBuilder getLikesUriBuilder(DislikedRecipes like, UriInfo uriInfo){
        return uriInfo.getBaseUriBuilder().path("dislikes").path(String.valueOf(like.getId()));
    }

    public DislikesDto(){

    }

    public DislikesDto(UriInfo uriInfo, DislikedRecipes dislike){
        this.id = dislike.getId();
        this.url = getLikesUriBuilder(dislike, uriInfo).build().toString();
        this.recipeUrl = uriInfo.getBaseUriBuilder().path("recipes").path(String.valueOf(dislike.getRecipe().getIdRecipe())).build().toString();
        this.userUrl = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(dislike.getUser().getIdUser())).build().toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }
}
