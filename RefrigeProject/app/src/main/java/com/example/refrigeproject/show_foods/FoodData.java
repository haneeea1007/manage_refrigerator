package com.example.refrigeproject.show_foods;

public class FoodData implements Section {

    int postion;

    int id;
    String category;
    String section;
    String name;
    String memo;
    String purchaseDate;
    String expirationDate;
    String imagePath;
    String code;
    String place;

    public FoodData() {
    }

    // to test
    public FoodData(int postiion, String name) {
        this.postion = postiion;
        this.name = name;
    }

    public FoodData(int postion, int id, String category, String section, String name, String memo, String purchaseDate, String expirationDate, String imagePath, String code, String place) {
        this.postion = postion;
        this.id = id;
        this.category = category;
        this.section = section;
        this.name = name;
        this.memo = memo;
        this.purchaseDate = purchaseDate;
        this.expirationDate = expirationDate;
        this.imagePath = imagePath;
        this.code = code;
        this.place = place;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getSection() {
        return section;
    }

    public String getName() {
        return name;
    }

    public String getMemo() {
        return memo;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getCode() {
        return code;
    }

    public String getPlace() {
        return place;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public int type() {
        return ITEM;
    }

    @Override
    public int sectionPosition() {
        return postion;
    }
}
