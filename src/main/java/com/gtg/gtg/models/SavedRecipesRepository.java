package com.gtg.gtg.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedRecipesRepository extends JpaRepository<SavedRecipe, Long> {
    // Check if a recipe is already saved for a user
    boolean existsByUserIdAndRecipeUri(Long userId, String recipeUri);

    List<SavedRecipe> findByUserId(Long userId);

    // void deleteRecipeByRecipeAndUserId(Long userId, int recipeId);
}
