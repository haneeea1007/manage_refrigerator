package com.example.refrigeproject.show_foods;

public class AddFoodGridViewData {
    private Integer imageID;
    private String foodName;

    public AddFoodGridViewData(Integer imageID, String foodName) {
        this.imageID = imageID;
        this.foodName = foodName;
    }

    public AddFoodGridViewData(Integer imageID) {
        this.imageID = imageID;
    }

    public AddFoodGridViewData(String foodName) {
        this.foodName = foodName;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
