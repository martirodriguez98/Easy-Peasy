package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "favourites")
public class Favourites {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favourites_id_seq")
    @SequenceGenerator(sequenceName = "favourites_id_seq", name="favourites_id_seq", allocationSize = 1)
    @Column(name="id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recipe")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user",nullable = false)
    private User user;

    //    default
    protected Favourites(){
        //Just for Hibernate
    }

    public Favourites(Recipe recipe, User user) {
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
