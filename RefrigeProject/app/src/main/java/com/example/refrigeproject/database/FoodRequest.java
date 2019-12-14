package com.example.refrigeproject.database;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FoodRequest extends StringRequest {

    final static private String URL = "http://jms1132.dothome.co.kr/food.php";
    private Map<String, String> map;
    private int id;
    private String category;
    private String section;
    private String name;
    private String imagePath;
    private String memo;
    private String purchaseDate;
    private String expirationDate;
    private String code;
    private String place;
    private int alarmID;

    public FoodRequest(int id, String category, String section, String name, String imagePath, String memo, String purchaseDate, String expirationDate, String code, String place, int alarmID,  Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); // getParams()를 부름
        map = new HashMap<>();
        this.id = id;
        this.category = category;
        this.section = section;
        this.name = name;
        this.imagePath = imagePath;
        this.memo = memo;
        this.imagePath = imagePath;
        this.purchaseDate = purchaseDate;
        this.expirationDate = expirationDate;
        this.code = code;
        this.place = place;
        this.alarmID = alarmID;
    }

    // 반드시 Map으로 리턴!
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("id", String.valueOf(this.id));
        map.put("category", this.category);
        map.put("section", this.section);
        map.put("name", this.name);
        map.put("imagePath", this.imagePath);
        map.put("memo", this.memo);
        map.put("purchaseDate", this.purchaseDate);
        map.put("expirationDate", this.expirationDate);
        map.put("code", this.code);
        map.put("place", this.place);
        map.put("alarmID", String.valueOf(this.alarmID));

        return map;
    }
}