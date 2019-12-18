package com.example.refrigeproject.show_foods;

public class AddFoodGridViewData {
    private String category;
    private String section;
    private Integer imageID;

    public AddFoodGridViewData(String category, String section, Integer imageID) {
        this.category = category;
        this.section = section;
        this.imageID = imageID;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
