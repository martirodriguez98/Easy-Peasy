package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "recipe_categories")
public class RecipeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_categories_id_seq")
    @SequenceGenerator(sequenceName = "recipe_categories_id_seq", name="recipe_categories_id_seq", allocationSize = 1)
    @Column(name="id")
    private long id;

    @Column(name = "category")
    private int category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recipe")
    private Recipe recipe;

    /* default*/
    protected RecipeCategory() {
        //Just for Hibernate
    }

    public RecipeCategory(int category, Recipe recipe) {
        this.category = category;
        this.recipe=recipe;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeCategory that = (RecipeCategory) o;
        return category == that.category && recipe.getIdRecipe() == that.recipe.getIdRecipe();
    }

}
