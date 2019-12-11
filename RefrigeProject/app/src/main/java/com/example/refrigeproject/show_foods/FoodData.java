package com.example.refrigeproject.show_foods;

public class FoodData implements Section {

    int postion;

    int id;
    String category;
    String section;
    String name;
    String memo;
    String PurchaseData;
    String expirationDate;
    String imagePath;
    String code;
    String place;

    // to test
    public FoodData(int postiion, String name) {
        this.postion = postiion;
        this.name = name;
    }

    public FoodData(int postion, int id, String category, String section, String name, String memo, String purchaseData, String expirationDate, String imagePath, String code, String place) {
        this.postion = postion;
        this.id = id;
        this.category = category;
        this.section = section;
        this.name = name;
        this.memo = memo;
        PurchaseData = purchaseData;
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

    public String getPurchaseData() {
        return PurchaseData;
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

    @Override
    public int type() {
        return ITEM;
    }

    @Override
    public int sectionPosition() {
        return postion;
    }
}
