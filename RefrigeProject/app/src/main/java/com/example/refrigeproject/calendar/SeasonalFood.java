package com.example.refrigeproject.calendar;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.refrigeproject.search_recipe.BasicRecipe;

public class SeasonalFood implements Parcelable {
//    {"IDNTFC_NO":"식품번호"},
//    {"PRDLST_NM":"품목명"},
//    {"M_DISTCTNS":"월별"},
//    {"M_DISTCTNS_ITM":"월별농식품"},
//    {"PRDLST_CL":"품목분류"},
//    {"MTC_NM":"주요 산지"},
//    {"PRDCTN__ERA":"생산시기"},
//    {"MAIN_SPCIES_NM":"주요 품종"},
//    {"EFFECT":"효능"},
//    {"PURCHASE_MTH":"구입요령"},
//    {"COOK_MTH":"조리법"},
//    {"TRT_MTH":"손질요령"},
//    {"URL":"상세페이지 URL"},
//    {"IMG_URL":"이미지 URL"},
//    {"REGIST_DE":"등록일"}

    private String foodID; // 식품번호
    private String foodName; // 품목명
    private String month; // 월별
    private String season; // 월별농식품
    private String classification; // 품목 분류
    private String productionRegion; // 주요 산지
    private String productionEra; // 생산 시기
    private String species; // 주요 품종
    private String effect; // 효능
    private String purchaseTips; // 구입 요령
    private String cookTips; // 조리법
    private String trimmingTips; // 손질요령
    private String detailsUrl; // 상세 페이지 URL
    private String imageUrl; // 이미지Url
    private String registDate; //등록일


    public SeasonalFood() {
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getProductionRegion() {
        return productionRegion;
    }

    public void setProductionRegion(String productionRegion) {
        this.productionRegion = productionRegion;
    }

    public String getProductionEra() {
        return productionEra;
    }

    public void setProductionEra(String productionEra) {
        this.productionEra = productionEra;
    }

    public String getSpcies() {
        return species;
    }

    public void setSpcies(String species) {
        this.species = species;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getPurchaseTips() {
        return purchaseTips;
    }

    public void setPurchaseTips(String purchaseTips) {
        this.purchaseTips = purchaseTips;
    }

    public String getCookTips() {
        return cookTips;
    }

    public void setCookTips(String cookTips) {
        this.cookTips = cookTips;
    }

    public String getTrimmingTips() {
        return trimmingTips;
    }

    public void setTrimmingTips(String trimmingTips) {
        this.trimmingTips = trimmingTips;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRegistDate() {
        return registDate;
    }

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public SeasonalFood(Parcel in) {
        this.foodID = in.readString();
        this.foodName = in.readString();
        this.month = in.readString();
        this.season = in.readString();
        this.classification = in.readString();
        this.productionRegion = in.readString();
        this.productionEra = in.readString();
        this.species = in.readString();
        this.effect = in.readString();
        this.purchaseTips = in.readString();
        this.cookTips = in.readString();
        this.trimmingTips = in.readString();
        this.detailsUrl = in.readString();
        this.imageUrl = in.readString();
        this.registDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.foodID);
        dest.writeString(this.foodName);
        dest.writeString(this.month);
        dest.writeString(this.season);
        dest.writeString(this.classification);
        dest.writeString(this.productionRegion);
        dest.writeString(this.productionEra);
        dest.writeString(this.species);
        dest.writeString(this.effect);
        dest.writeString(this.purchaseTips);
        dest.writeString(this.cookTips);
        dest.writeString(this.trimmingTips);
        dest.writeString(this.detailsUrl);
        dest.writeString(this.imageUrl);
        dest.writeString(this.registDate);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        @Override
        public SeasonalFood createFromParcel(Parcel source) {
            return new SeasonalFood(source);
        }

        @Override
        public SeasonalFood[] newArray(int size) {
            return new SeasonalFood[size];
        }
    };
}
