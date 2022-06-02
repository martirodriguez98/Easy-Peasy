package ar.edu.itba.paw.webapp.dto.response;

import ar.edu.itba.paw.models.LikedRecipes;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.stream.Collectors;

public class LikesDto {
    private long id;
    private String url;
    private String recipeUrl;
    private String userUrl;

    public static Collection<LikesDto> mapLikeToDto(Collection<LikedRecipes> likes, UriInfo uriInfo){
        return likes.stream().map(l->new LikesDto(uriInfo,l)).collect(Collectors.toList());
    }
    public static UriBuilder getLikesUriBuilder(LikedRecipes like, UriInfo uriInfo){
        return uriInfo.getBaseUriBuilder().path("likes").path(String.valueOf(like.getId()));
    }

    public LikesDto(){

    }

    public LikesDto(UriInfo uriInfo, LikedRecipes like){
        this.id = like.getId();
        this.url = getLikesUriBuilder(like, uriInfo).build().toString();
        this.recipeUrl = uriInfo.getBaseUriBuilder().path("recipes").path(String.valueOf(like.getRecipe().getIdRecipe())).build().toString();
        this.userUrl = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(like.getUser().getIdUser())).build().toString();
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
