package com.example.refrigeproject.database;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UserRequest extends StringRequest {

    final static private String URL = "http://jms1132.dothome.co.kr/user.php";
    private Map<String, String> map;
    private String id;
    private String profile;
    private String name;

    public UserRequest(String id, String profile, String name, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); // getParams()를 부름
        map = new HashMap<>();
        this.id = id;
        this.profile = profile;
        this.name = name;
    }

    // 반드시 Map으로 리턴!
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("id", this.id);
        map.put("profile", this.profile);
        map.put("name", this.name);

        return map;
    }
}