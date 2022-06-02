package ar.edu.itba.paw.webapp.dto.response;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserRole;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

public class UserDto {
    private Long id;
    private String username;
    private String email;
    private LocalDate dateCreated;

    private String[] roles;
    private int recipesCount;
    private int highlightedCount;
    private String url;
    private String recipesUrl;
    private String avatarUrl;



    public static Collection<UserDto> mapUserToDto(Collection<User> users, UriInfo uri){
        return users.stream().map(us -> new UserDto(uri, us)).collect(Collectors.toList());
    }

    public static UriBuilder getUserUriBuilder(User user, UriInfo uri){
        return uri.getBaseUriBuilder().clone().path("users").path(String.valueOf(user.getIdUser()));
    }
    public UserDto(){
    }

    public UserDto(UriInfo uri, User user) {
        UriBuilder uriBuilder = getUserUriBuilder(user, uri);
        this.id = user.getIdUser();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.dateCreated = user.getDateCreated();
        this.url = uriBuilder.clone().build().toString();
        this.roles = new String[user.getRoles().size()];
        int i=0;
        for(UserRole r: user.getRoles()){
            this.roles[i] = (r.getRole().getRoleName());
            i++;
        }
        this.recipesCount = user.getRecipes().size();
        this.highlightedCount = user.getHighlightedRecipes().size();

        if(user.getAvatar() != null){
            this.avatarUrl = uriBuilder.clone().path("avatar").build().toString();
        }else{
            this.avatarUrl = "";
        }

        this.recipesUrl = uriBuilder.clone().path("recipes").build().toString();

    }

    public int getRecipesCount() {
        return recipesCount;
    }

    public void setRecipesCount(int recipesCount) {
        this.recipesCount = recipesCount;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRecipesUrl() {
        return recipesUrl;
    }

    public void setRecipesUrl(String recipesUrl) {
        this.recipesUrl = recipesUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getUrl() {
        return url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getHighlightedCount() {
        return highlightedCount;
    }

    public void setHighlightedCount(int highlightedCount) {
        this.highlightedCount = highlightedCount;
    }
}