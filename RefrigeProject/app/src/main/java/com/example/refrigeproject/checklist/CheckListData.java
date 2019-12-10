package com.example.refrigeproject.checklist;

public class CheckListData {

    private String checkItem;
    private String checkChecked;

    public CheckListData(String checkItem, String checkChecked) {
        this.checkItem = checkItem;
        this.checkChecked = checkChecked;
    }

    public String getCheckItem() {
        return checkItem;
    }

    public void setCheckItem(String checkItem) {
        this.checkItem = checkItem;
    }

    public String getCheckChecked() {
        return checkChecked;
    }

    public void setCheckChecked(String checkChecked) {
        this.checkChecked = checkChecked;
    }
}

