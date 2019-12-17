package com.example.refrigeproject.show_foods;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodData implements Section, Parcelable {

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
    int alarmID;

    public FoodData() {
    }

    // to test
    public FoodData(int postiion, String name) {
        this.postion = postiion;
        this.name = name;
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

    public int getPostion() {
        return postion;
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

    public int getAlarmID() {
        return alarmID;
    }

    public void setAlarmID(int alarmID) {
        this.alarmID = alarmID;
    }

    @Override
    public int type() {
        return ITEM;
    }

    @Override
    public int sectionPosition() {
        return postion;
    }

    /////////////////
    public FoodData(Parcel in) {
        this.postion = in.readInt();
        this.id = in.readInt();
        this.category = in.readString();
        this.section = in.readString();
        this.name = in.readString();
        this.memo = in.readString();
        this.purchaseDate = in.readString();
        this.expirationDate = in.readString();
        this.imagePath = in.readString();
        this.code = in.readString();
        this.place = in.readString();
        this.alarmID = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.postion);
        dest.writeInt(this.id);
        dest.writeString(this.category);
        dest.writeString(this.section);
        dest.writeString(this.name);
        dest.writeString(this.memo);
        dest.writeString(this.purchaseDate);
        dest.writeString(this.expirationDate);
        dest.writeString(this.imagePath);
        dest.writeString(this.code);
        dest.writeString(this.place);
        dest.writeInt(this.alarmID);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        @Override
        public FoodData createFromParcel(Parcel source) {
            return new FoodData(source);
        }

        @Override
        public FoodData[] newArray(int size) {
            return new FoodData[size];
        }
    };
}
