package ar.edu.itba.paw.webapp.dto.response;

import javax.ws.rs.core.UriInfo;

public class LandingDto {
    private String users_url;
    private String recipes_url;

    public LandingDto() {
    }

    public LandingDto(UriInfo uriInfo) {
        users_url = uriInfo.getBaseUriBuilder().path("users").build().toString();
        recipes_url = uriInfo.getBaseUriBuilder().path("recipes").build().toString();
    }

    public String getUsers_url() {
        return users_url;
    }

    public void setUsers_url(String users_url) {
        this.users_url = users_url;
    }

    public String getRecipes_url() {
        return recipes_url;
    }

    public void setRecipes_url(String recipes_url) {
        this.recipes_url = recipes_url;
    }
}
