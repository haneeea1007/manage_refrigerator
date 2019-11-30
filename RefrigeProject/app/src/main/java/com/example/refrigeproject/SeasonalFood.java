package com.example.refrigeproject;

class SeasonalFood {
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
    private String spcies; // 주요 품종
    private String effect; // 효능
    private String purchaseTips; // 구입 요령
    private String cookTips; // 조리법
    private String trimmingTips; // 손질요령
    private String detailsUrl; // 상세 페이지 URL
    private String imageUrl; // 이미지Url
    private String registDate; //등록일

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
        return spcies;
    }

    public void setSpcies(String spcies) {
        this.spcies = spcies;
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
}
