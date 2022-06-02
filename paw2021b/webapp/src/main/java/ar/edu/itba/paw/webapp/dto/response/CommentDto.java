package ar.edu.itba.paw.webapp.dto.response;


import ar.edu.itba.paw.models.Comment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlType;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@XmlType(name= "")
public class CommentDto {

    public static Collection<CommentDto> mapCommentToDto(Collection<Comment> comments, UriInfo uriInfo){
        return comments.stream().map(c->new CommentDto(uriInfo,c)).collect(Collectors.toList());
    }
    public static UriBuilder getCommentUriBuilder(Comment comment, UriInfo uriInfo){
        return uriInfo.getBaseUriBuilder().path("comments").path(String.valueOf(comment.getIdComment()));
    }

    private Long id;
    private String commentDesc;
    private LocalDate dateCreated;
    private UserDto user;
    private String url;
    private String recipeUrl;

    public CommentDto() {
    }

    public CommentDto(UriInfo uri, Comment comment){
        this.user = new UserDto(uri, comment.getUser());
        this.id = comment.getIdComment();
        this.commentDesc = comment.getCommentDesc();
        this.dateCreated = comment.getDateCreated();
        this.url = getCommentUriBuilder(comment, uri).build().toString();
        this.recipeUrl = uri.getBaseUriBuilder().path("recipes").path(String.valueOf(comment.getRecipe().getIdRecipe())).build().toString();
    }

    public String getCommentDesc() {
        return commentDesc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
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

    public void setCommentDesc(String commentDesc) {
        this.commentDesc = commentDesc;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
}