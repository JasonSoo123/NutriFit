package com.gtg.gtg.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class SavedRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "recipe_uri", nullable = false)
    private String recipeUri;

    @Column(name = "recipe_image", nullable = false)
    private String recipeImage;

    @Column(name = "recipe_name", nullable = false)
    private String recipeName;

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    // Constructors
    public SavedRecipe() {}

    public SavedRecipe(Long userId, String recipeUri) {
        this.userId = userId;
        this.recipeUri = recipeUri;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(long i) {
        this.userId = (long) i;
    }

    public String getRecipeUri() {
        return recipeUri;
    }

    public void setRecipeUri(String recipeUri) {
        this.recipeUri = recipeUri;
    }
    
}
