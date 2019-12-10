package com.example.refrigeproject.search_recipe;

public class RecipeIngredient {
//       {"IRDNT_CPCTY":"2T",
//            "IRDNT_NM":"고추장",
//            "IRDNT_SN":180381,
//            "RN":6072,
//            "IRDNT_TY_NM":"양념",
//            "RECIPE_ID":180363,
//            "IRDNT_TY_CODE":"3060003"}

    private String recipeID;
    private String capacity;
    private String name;
    private String serialNumber;
    private String typeCode;
    private String typeName;

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
