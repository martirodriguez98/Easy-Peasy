package ar.edu.itba.paw.webapp.dto.request;

import ar.edu.itba.paw.models.Recipe;
import ar.edu.itba.paw.models.User;

import java.time.LocalDate;

public class CommentCreateDto {
    private long id;
    private Recipe recipe;
    private User user;
    private String commentDesc;
    private LocalDate dateCreated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentDesc() {
        return commentDesc;
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
