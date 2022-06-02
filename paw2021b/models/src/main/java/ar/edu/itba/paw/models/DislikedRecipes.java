package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "disliked_recipes")
public class DislikedRecipes {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "disliked_recipes_id_seq")
    @SequenceGenerator(sequenceName = "disliked_recipes_id_seq", name="disliked_recipes_id_seq", allocationSize = 1)
    @Column(name="id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recipe")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;


    //default
    protected DislikedRecipes(){
        //Just for Hibernate
    }

    public DislikedRecipes(Recipe recipe, User user) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
