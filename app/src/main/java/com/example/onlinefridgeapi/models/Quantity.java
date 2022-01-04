package com.example.onlinefridgeapi.models;

public class Quantity {
    private int id;
    private int recipeID;
    private int ingredientID;
    private float quantity;

    public Quantity(int id, int recipeID, int ingredientID, float quantity) {
        this.id = id;
        this.recipeID = recipeID;
        this.ingredientID = ingredientID;
        this.quantity = quantity;
    }

    public Quantity(int recipeID, int ingredientID, float quantity) {
        this.recipeID = recipeID;
        this.ingredientID = ingredientID;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public int getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}