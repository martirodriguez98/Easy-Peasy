package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDate;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "recipe")
public class Recipe implements Comparable<Recipe> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_id_recipe_seq")
    @SequenceGenerator(sequenceName = "recipe_id_recipe_seq", name="recipe_id_recipe_seq", allocationSize = 1)
    @Column(name="id_recipe")
    private long idRecipe;


    @Column(name = "recipe_title", length = 100, nullable = false)
    private String recipeTitle;

    @Column(name = "recipe_desc", nullable = false)
    private String recipeDesc;

    @Column(name = "recipe_steps")
    private String recipeSteps;

    @Column(name = "recipe_difficulty")
    private int recipeDifficulty;

    @Column(name = "is_highlighted")
    private boolean isHighlighted;

    @Column(name = "recipe_time", length = 50)
    private String recipeTime;

    @Column(name = "recipe_date_created")
    private LocalDate recipeDateCreated;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_user",nullable = false)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Set<Ingredient> ingredients;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Image> images;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Set<RecipeCategory> categories;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Set<LikedRecipes> likes;

    private long likesCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Set<DislikedRecipes> dislikes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Set<Favourites> favourites;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Set<Comment> comments;


    /* default */
    protected Recipe() {
        // Just for Hibernate
    }

    public Recipe(String recipeTitle, String recipeDesc, String recipeSteps, User user, int recipeDifficulty, boolean isHighlighted, String recipeTime, LocalDate recipeDateCreated) {
        this.recipeTitle = recipeTitle;
        this.recipeDesc = recipeDesc;
        this.recipeSteps = recipeSteps;
        this.user = user;
        this.recipeDifficulty = recipeDifficulty;
        this.isHighlighted = isHighlighted;
        this.recipeTime = recipeTime;
        this.recipeDateCreated = recipeDateCreated;
        this.likesCount = 0;
    }

    public long getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(long idRecipe) {
        this.idRecipe = idRecipe;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipeDesc() {
        return recipeDesc;
    }

    public void setRecipeDesc(String recipeDesc) {
        this.recipeDesc = recipeDesc;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRecipeDifficulty() {
        return recipeDifficulty;
    }

    public void setRecipeDifficulty(int recipeDifficulty) {
        this.recipeDifficulty = recipeDifficulty;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setIsHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public String getRecipeTime() {
        return recipeTime;
    }

    public void setRecipeTime(String recipeTime) {
        this.recipeTime = recipeTime;
    }

    public LocalDate getRecipeDateCreated() {
        return recipeDateCreated;
    }

    public void setRecipeDateCreated(LocalDate recipeDateCreated) {
        this.recipeDateCreated = recipeDateCreated;
    }

    public String getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(String recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Set<RecipeCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<RecipeCategory> categories) {
        this.categories = categories;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Set<LikedRecipes> getLikes() {
        return likes;
    }

    public void setLikes(Set<LikedRecipes> likes) {
        this.likes = likes;
    }

    public Set<DislikedRecipes> getDislikes() {
        return dislikes;
    }

    public void setDislikes(Set<DislikedRecipes> dislikes) {
        this.dislikes = dislikes;
    }

    public Set<Favourites> getFavourites() {
        return favourites;
    }

    public void setFavourites(Set<Favourites> favourites) {
        this.favourites = favourites;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;
        Recipe recipe = (Recipe) o;
        return idRecipe == recipe.idRecipe;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRecipe);
    }

    @Override
    public int compareTo(Recipe o) {
        if (this.idRecipe == o.getIdRecipe()) {
            return 0;
        }
        return 1;
    }
}