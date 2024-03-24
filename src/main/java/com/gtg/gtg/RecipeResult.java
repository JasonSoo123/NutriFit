package com.gtg.gtg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeResult {
    private List<Hit> hits;

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Hit {
        private Recipe recipe;

        public Recipe getRecipe() {
            return recipe;
        }

        public void setRecipe(Recipe recipe) {
            this.recipe = recipe;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Recipe {
        private String uri;
        private String label;
        private String image;
        private String source;
        private String url;
        private String shareAs;
        private Double yield;
        private List<String> dietLabels;
        private List<String> healthLabels;
        private List<String> cautions;
        private List<String> ingredientLines;
        private List<Ingredient> ingredients;
        private Double calories;
        private List<String> cuisineType;
        private List<String> mealType;
        private List<String> dishType;
        private List<String> instructions; // Assuming instructions are a list of strings

        // Getters and Setters for Recipe
        public String getUri() { return uri; }
        public void setUri(String uri) { this.uri = uri; }
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public String getImage() { return image; }
        public void setImage(String image) { this.image = image; }
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public String getShareAs() { return shareAs; }
        public void setShareAs(String shareAs) { this.shareAs = shareAs; }
        public Double getYield() { return yield; }
        public void setYield(Double yield) { this.yield = yield; }
        public List<String> getDietLabels() { return dietLabels; }
        public void setDietLabels(List<String> dietLabels) { this.dietLabels = dietLabels; }
        public List<String> getHealthLabels() { return healthLabels; }
        public void setHealthLabels(List<String> healthLabels) { this.healthLabels = healthLabels; }
        public List<String> getCautions() { return cautions; }
        public void setCautions(List<String> cautions) { this.cautions = cautions; }
        public List<String> getIngredientLines() { return ingredientLines; }
        public void setIngredientLines(List<String> ingredientLines) { this.ingredientLines = ingredientLines; }
        public List<Ingredient> getIngredients() { return ingredients; }
        public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
        public Double getCalories() { return calories; }
        public void setCalories(Double calories) { this.calories = calories; }
        public List<String> getCuisineType() { return cuisineType; }
        public void setCuisineType(List<String> cuisineType) { this.cuisineType = cuisineType; }
        public List<String> getMealType() { return mealType; }
        public void setMealType(List<String> mealType) { this.mealType = mealType; }
        public List<String> getDishType() { return dishType; }
        public void setDishType(List<String> dishType) { this.dishType = dishType; }
        public List<String> getInstructions() { return instructions; }
        public void setInstructions(List<String> instructions) { this.instructions = instructions; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ingredient {
        private String text;
        private Double quantity;
        private String measure;
        private String food;
        private Double weight;
        private String foodId;

        // Getters and Setters for Ingredient
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public Double getQuantity() { return quantity; }
        public void setQuantity(Double quantity) { this.quantity = quantity; }
        public String getMeasure() { return measure; }
        public void setMeasure(String measure) { this.measure = measure; }
        public String getFood() { return food; }
        public void setFood(String food) { this.food = food; }
        public Double getWeight() { return weight; }
    }

}