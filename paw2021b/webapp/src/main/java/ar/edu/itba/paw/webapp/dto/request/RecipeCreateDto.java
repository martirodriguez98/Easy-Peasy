package ar.edu.itba.paw.webapp.dto.request;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class RecipeCreateDto {
    @NotNull
    @Size(min = 2, max = 100)
    private String recipeTitle;
    @NotEmpty
    private String recipeDesc;
    @NotEmpty
    private String recipeSteps;
    private String recipeImage;
    private int recipeDifficulty;
    @Size(max = 50)
    private String recipeTime;
    private List<MultipartFile> images;

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    private List<String> ingredientsList;

    private List<Integer> categoriesList;

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

    public String getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(String recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public int getRecipeDifficulty() {
        return recipeDifficulty;
    }

    public void setRecipeDifficulty(int recipeDifficulty) {
        this.recipeDifficulty = recipeDifficulty;
    }

    public String getRecipeTime() {
        return recipeTime;
    }

    public void setRecipeTime(String recipeTime) {
        this.recipeTime = recipeTime;
    }

    public List<String> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(List<String> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public List<Integer> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(List<Integer> categoriesList) {
        this.categoriesList = categoriesList;
    }
}
