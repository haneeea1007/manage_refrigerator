package com.example.refrigeproject.database;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteManage extends StringRequest {

    final static private String URL = "http://jms1132.dothome.co.kr/deleteManage.php";
    private Map<String, String> map;
    private String id;
    private String code;

    public DeleteManage(String id, String code, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); // getParams()를 부름
        map = new HashMap<>();
        this.id = id;
        this.code = code;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("code", this.code);
        map.put("id", this.id);

        return map;
    }
}