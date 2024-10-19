package com.duoc.seguridad_calidad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.service.RecipeService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public String getAllRecipes(Model model) {
        try {
            List<Recipe> recipes = recipeService.getAllRecipes();
            model.addAttribute("recipes", recipes);
            return "recipes";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading recipes: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/my-recipes")
    public String getUserRecipes(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            List<Recipe> userRecipes = recipeService.getRecipesByUser(username);
            model.addAttribute("recipes", userRecipes);
            return "user-recipes";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading user recipes: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/new")
    public String newRecipeForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "recipe-form";
    }

    @PostMapping
    public String createRecipe(@ModelAttribute Recipe recipe, Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            recipe.setCreatedBy(username);
            
            // Convert ingredients string to List<String>
            if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
                List<String> ingredientList = Arrays.asList(recipe.getIngredients().split("\\r?\\n"));
                recipe.setIngredientList(ingredientList);
            }
            
            recipeService.saveRecipe(recipe);
            return "redirect:/recipes";
        } catch (IOException e) {
            model.addAttribute("error", "Error creating recipe: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/{id}")
    public String getRecipe(@PathVariable String id, Model model) {
        try {
            Recipe recipe = recipeService.getRecipeById(id);
            model.addAttribute("recipe", recipe);
            return "recipe-details";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading recipe: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/{id}/edit")
    public String editRecipeForm(@PathVariable String id, Model model) {
        try {
            Recipe recipe = recipeService.getRecipeById(id);
            model.addAttribute("recipe", recipe);
            return "recipe-form";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading recipe for editing: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/{id}")
    public String updateRecipe(@PathVariable String id, @ModelAttribute Recipe recipe, Model model) {
        try {
            recipe.setId(id);
            recipeService.saveRecipe(recipe);
            return "redirect:/recipes/" + id;
        } catch (IOException e) {
            model.addAttribute("error", "Error updating recipe: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteRecipe(@PathVariable String id, Model model) {
        try {
            recipeService.deleteRecipe(id);
            return "redirect:/recipes";
        } catch (IOException e) {
            model.addAttribute("error", "Error deleting recipe: " + e.getMessage());
            return "error";
        }
    }
}