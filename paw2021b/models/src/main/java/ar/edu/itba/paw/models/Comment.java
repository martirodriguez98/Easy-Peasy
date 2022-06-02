package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_id_comment_seq")
    @SequenceGenerator(sequenceName = "comment_id_comment_seq", name="comment_id_comment_seq", allocationSize = 1)
    @Column(name = "id_comment")
    private long idComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recipe",nullable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user",nullable = false)
    private User user;

    @Column(name = "comment_desc", nullable = false)
    private String commentDesc;

    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Set<Reports> reports;



    /* default */
    protected Comment() {
        // Just for Hibernate
    }

    public Comment(Recipe recipe, User user, String commentDesc, LocalDate dateCreated) {
        this.recipe = recipe;
        this.user = user;
        this.commentDesc = commentDesc;
        this.dateCreated = dateCreated;
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

    public long getIdComment() {
        return idComment;
    }

    public void setIdComment(long idComment) {
        this.idComment = idComment;
    }

    public String getCommentDesc() {
        return commentDesc;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setCommentDesc(String commentDesc) {
        this.commentDesc = commentDesc;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return idComment == comment.idComment && recipe.getIdRecipe() == comment.recipe.getIdRecipe() && user.getIdUser() == comment.user.getIdUser() && Objects.equals(commentDesc, comment.commentDesc);
    }

}
