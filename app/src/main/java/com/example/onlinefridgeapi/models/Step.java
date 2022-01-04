package com.example.onlinefridgeapi.models;

public class Step {
    private int id;
    private int recipeID;
    private int number;
    private int stepDescription;

    public Step(int id, int recipeID, int number, int stepDescription) {
        this.id = id;
        this.recipeID = recipeID;
        this.number = number;
        this.stepDescription = stepDescription;
    }

    public Step(int recipeID, int number, int stepDescription) {
        this.recipeID = recipeID;
        this.number = number;
        this.stepDescription = stepDescription;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(int stepDescription) {
        this.stepDescription = stepDescription;
    }
}