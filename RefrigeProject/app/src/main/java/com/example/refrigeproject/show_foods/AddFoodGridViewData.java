package com.example.refrigeproject.show_foods;

import android.os.Parcel;
import android.os.Parcelable;

public class AddFoodGridViewData implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.category);
        dest.writeString(this.section);
        dest.writeInt(this.imageID);
    }

    public AddFoodGridViewData(Parcel in){
        this.category = in.readString();
        this.section = in.readString();
        this.imageID = in.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        @Override
        public AddFoodGridViewData createFromParcel(Parcel source) {
            return new AddFoodGridViewData(source);
        }

        @Override
        public AddFoodGridViewData[] newArray(int size) {
            return new AddFoodGridViewData[size];
        }
    };
}
