package com.example.refrigeproject.show_foods;

public class RefrigeratorData {
    String code;
    String name;
    int imgResource;

    public RefrigeratorData(String code, String name, int imgResource) {
        this.code = code;
        this.name = name;
        this.imgResource = imgResource;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getImgResource() {
        return imgResource;
    }


}
