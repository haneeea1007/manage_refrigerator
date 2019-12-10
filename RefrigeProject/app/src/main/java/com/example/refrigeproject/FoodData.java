package com.example.refrigeproject;

import com.saber.stickyheader.stickyData.StickyMainData;

public class FoodData implements StickyMainData {
    String category;
    String group;
    String name;
    String memo;
    String datePurchased;
    String expirationDate;

    public FoodData(String category, String group, String name, String memo, String datePurchased, String expirationDate) {
        this.category = category;
        this.group = group;
        this.name = name;
        this.memo = memo;
        this.datePurchased = datePurchased;
        this.expirationDate = expirationDate;
    }

    public FoodData(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public String getMemo() {
        return memo;
    }

    public String getDatePurchased() {
        return datePurchased;
    }

    public String getExpirationDate() {
        return expirationDate;
    }
}
