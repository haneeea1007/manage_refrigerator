package com.example.refrigeproject.search_recipe;

public class Recipe {
//    "STRE_STEP_IMAGE_URL":"http://file.okdab.com/UserFiles/searching/recipe/000200_p01.jpg",
//    "STEP_TIP":null,
//    "RN":1,
//    "RECIPE_ID":1,
//    "COOKING_DC":"양지머리로 육수를 낸 후 식혀 기름을 걷어낸 후, 불린 쌀을 넣어 고슬고슬하게 밥을 짓는다.",
//    "COOKING_NO":1

    private String imageUrl;
    private String tip;
    private String recipeID;
    private String description;
    private String order;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
