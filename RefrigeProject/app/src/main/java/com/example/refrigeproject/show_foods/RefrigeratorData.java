package com.example.refrigeproject.show_foods;

public class RefrigeratorData {
    String code;
    String name;
    String type;

    public RefrigeratorData() {
    }

    public RefrigeratorData(String code, String name, String type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}
