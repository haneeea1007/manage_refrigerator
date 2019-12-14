package com.example.refrigeproject.database;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ManageRequest extends StringRequest {

    final static private String URL = "http://jms1132.dothome.co.kr/manage.php";
    private Map<String, String> map;
    private String id;
    private String code;

    public ManageRequest(String id, String code, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); // getParams()를 부름
        map = new HashMap<>();
        this.id = id;
        this.code = code;
    }

    // 반드시 Map으로 리턴!
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("id", this.id);
        map.put("code", this.code);

        return map;
    }
}