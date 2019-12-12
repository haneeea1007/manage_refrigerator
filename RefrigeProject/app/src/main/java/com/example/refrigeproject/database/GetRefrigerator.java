package com.example.refrigeproject.database;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetRefrigerator extends StringRequest {

    final static private String URL = "http://soproject.dothome.co.kr/Food.php";
    private Map<String, String> map;
    private String code;
    private String name;
    private String type;

    public GetRefrigerator(String code, String name, String type, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); // getParams()를 부름
        map = new HashMap<>();
        this.code = code;
        this.name = name;
        this.type = type;
    }

    // 반드시 Map으로 리턴!
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("code", this.code);
        map.put("name", this.name);
        map.put("type", this.type);

        return map;
    }
}