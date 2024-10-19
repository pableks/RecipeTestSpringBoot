package com.duoc.seguridad_calidad.service;

import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.util.FileStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private static final String RECIPES_DIRECTORY = "recipes";

    @Autowired
    private FileStorageUtil fileStorageUtil;

    public List<Recipe> getAllRecipes() throws IOException {
        return fileStorageUtil.readAllFromDirectory(RECIPES_DIRECTORY, Recipe.class);
    }

    public Recipe getRecipeById(String id) throws IOException {
        List<Recipe> recipes = getAllRecipes();
        return recipes.stream()
                .filter(recipe -> recipe.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Recipe> getRecipesByUser(String username) throws IOException {
        List<Recipe> allRecipes = getAllRecipes();
        return allRecipes.stream()
                .filter(recipe -> recipe.getCreatedBy().equals(username))
                .collect(Collectors.toList());
    }

    public void saveRecipe(Recipe recipe) throws IOException {
        if (recipe.getId() == null) {
            recipe.setId(UUID.randomUUID().toString());
        }
        fileStorageUtil.saveToFile(recipe, RECIPES_DIRECTORY + "/" + recipe.getId() + ".json");
    }

    public void deleteRecipe(String id) throws IOException {
        fileStorageUtil.deleteFile(RECIPES_DIRECTORY + "/" + id + ".json");
    }
}