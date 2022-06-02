package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "liked_recipes")
public class LikedRecipes {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "liked_recipes_id_seq")
    @SequenceGenerator(sequenceName = "liked_recipes_id_seq", name="liked_recipes_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recipe")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user",nullable = false)
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    //    default
    protected LikedRecipes(){
        //Just for Hibernate
    }

    public LikedRecipes(Recipe recipe, User user) {
        this.recipe = recipe;
        this.user = user;
    }

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


}
