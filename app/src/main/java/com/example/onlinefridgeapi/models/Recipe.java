package com.example.onlinefridgeapi.models;

public class Recipe {
    private int id;
    private String userID;
    private String name;
    private String description;
    private int cookTime;
    private int prepTime;
    private String foodCategory;

    public Recipe(int id, String userID, String name, String description, int cookTime, int prepTime, String foodCategory) {
        this.id = id;
        this.userID = userID;
        this.name = name;
        this.description = description;
        this.cookTime = cookTime;
        this.prepTime = prepTime;
        this.foodCategory = foodCategory;
    }

    public static String foodCategoryFromInt(int i) {
        switch (i) {
            case 0: return "Breakfast";
            case 1: return "Lunch";
            case 2: return "Dinner";
            case 3: return "Salad";
            case 4: return "Appetizer";
            case 5: return "Dessert";
            default: return null;
        }
    }

    public static int intFromFoodCategory(String s) {
        switch (s) {
            case "Breakfast": return 0;
            case "Lunch": return 1;
            case "Dinner": return 2;
            case "Salad": return 3;
            case "Appetizer": return 4;
            case "Dessert": return 5;
            default: return 6;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }
}