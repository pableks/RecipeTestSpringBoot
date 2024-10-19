package com.duoc.seguridad_calidad.model;

import java.util.List;

public class Recipe {
    private String id;
    private String name;
    private String createdBy;
    private int preparationTime;
    private String difficulty;
    private List<String> ingredients;
    private String preparation;

    // Constructors
    public Recipe() {}

    public Recipe(String id, String name, String createdBy, int preparationTime, String difficulty, List<String> ingredients, String preparation) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.preparationTime = preparationTime;
        this.difficulty = difficulty;
        this.ingredients = ingredients;
        this.preparation = preparation;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }
}