package com.example.refrigeproject.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.refrigeproject.MainActivity;
import com.example.refrigeproject.R;
import com.example.refrigeproject.database.DeleteFood;
import com.example.refrigeproject.database.DeleteManage;
import com.example.refrigeproject.database.ManageRequest;
import com.example.refrigeproject.show_foods.RefrigeratorData;
import com.example.refrigeproject.show_foods.ShowFoodsFragment;
import com.r0adkll.slidr.Slidr;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ManageFridgeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "ManageFridgeActivity";
    private ListView lvFridgeList;
    private ListViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_fridge);
        Slidr.attach(this);

        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        lvFridgeList = findViewById(R.id.lvFridgeList);

        adapter = new ListViewAdapter();
        lvFridgeList.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        adapter.notifyDataSetChanged();
    }

    private class ListViewAdapter extends BaseAdapter{
        LinearLayout llFridgeItem;
        ImageView ivFridge;
        TextView tvName, tvCode;

        @Override
        public int getCount() {
            return ShowFoodsFragment.refrigeratorList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if(position == ShowFoodsFragment.refrigeratorList.size() + 1) return "냉장고 추가하기";

            return ShowFoodsFragment.refrigeratorList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            if (convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fridge_list_item, null);
            }

            llFridgeItem = convertView.findViewById(R.id.llFridgeItem);
            ivFridge = convertView.findViewById(R.id.ivFridge);
            tvName = convertView.findViewById(R.id.tvName);
            tvCode = convertView.findViewById(R.id.tvCode);

            if(ShowFoodsFragment.refrigeratorList.size() == position){
                // 리스트뷰의 마지막 아이템일 경우
                // 냉장고 추가 기능
                Log.d("log", position+" / "+ ShowFoodsFragment.refrigeratorList.size());
                ivFridge.setImageResource(R.drawable.add_box);
                tvName.setText("냉장고 추가하기");
                tvCode.setVisibility(View.GONE);

                llFridgeItem.setOnLongClickListener(null);
                llFridgeItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final CookieBar.Builder build = CookieBar.build(ManageFridgeActivity.this);
                        build.setCustomView(R.layout.cookiebar_add_fridge);
                        build.setCustomViewInitializer(new CookieBar.CustomViewInitializer() {
                            @Override
                            public void initView(View view) {
                                TextView tvNew = view.findViewById(R.id.tvNew);
                                TextView tvExisting = view.findViewById(R.id.tvExisting);

                                // 새로운 냉장고 추가하기
                                tvNew.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(ManageFridgeActivity.this, AddFridgeActivity.class);
                                        startActivity(intent);
                                        try{
                                            CookieBar.dismiss(ManageFridgeActivity.this);
                                        }catch (NullPointerException e){
                                            Log.e(TAG, e.toString());
                                        }
                                    }
                                });

                                // 기존 냉장고 불러오기
                                tvExisting.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try{
                                            CookieBar.dismiss(ManageFridgeActivity.this);
                                        }catch (NullPointerException e){
                                            Log.e(TAG, e.toString());
                                        }

                                        View dialogView = View.inflate(v.getContext(), R.layout.add_existing_fridge, null);
                                        final EditText edtCode = dialogView.findViewById(R.id.edtCode);

                                        AlertDialog.Builder dialog = new AlertDialog.Builder(ManageFridgeActivity.this);
                                        dialog.setView(dialogView)
                                                .setNegativeButton("취소", null)
                                                .setNeutralButton("등록", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // insert into manageTBL
                                                        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                    boolean success = jsonObject.getBoolean("success");
                                                                    if(success){
                                                                        //해당 코드의 냉장고 정보 불러오기
                                                                        selectFridgeByCode(edtCode.getText().toString());
                                                                    }else{
                                                                        Toast.makeText(getApplicationContext(), " 추가 실패", Toast.LENGTH_LONG).show();
                                                                        finish();
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        };

                                                        ManageRequest manageRequest = new ManageRequest(MainActivity.strId, edtCode.getText().toString(), responseListener2); // 자료 다 들어있음. 상대방주소,데이터,데이터랩방식 등
                                                        RequestQueue requestQueue2 = Volley.newRequestQueue(ManageFridgeActivity.this);
                                                        requestQueue2.add(manageRequest);
                                                    }
                                                });

                                        dialog.show();
                                    }
                                });
                            }
                        });

                        build.setTitle("냉장고 추가하기");
                        build.setSwipeToDismiss(true);
                        build.setEnableAutoDismiss(true);
                        build.setDuration(5000);
                        build.setCookiePosition(CookieBar.BOTTOM);
                        build.show();

                    }
                });
                Log.d("log", tvName.getText().toString());
            } else {
                // 해당 냉장고 아이템 getView
                final RefrigeratorData ref = ShowFoodsFragment.refrigeratorList.get(position);

                switch (ref.getType()){
                    case "기본형":
                        ivFridge.setImageResource(R.drawable.fridge1);
                        break;

                    case "양문형":
                        ivFridge.setImageResource(R.drawable.fridge2);
                        break;

                    case "김치냉장고":
                        ivFridge.setImageResource(R.drawable.fridge3);
                        break;
                }

                tvName.setText(ref.getName());
                tvCode.setVisibility(View.VISIBLE);
                tvCode.setText(ref.getCode()); // ArrayList 타입 다시 정의하기!

                llFridgeItem.setOnClickListener(null);
                llFridgeItem.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        try{
                            CookieBar.dismiss(ManageFridgeActivity.this);
                        }catch (NullPointerException e){
                            Log.e(TAG, e.toString());
                        }
                        Log.d(TAG, ref.getName());

                        View dialogView = View.inflate(v.getContext(), R.layout.edit_fridge_info, null);
                        final EditText edtName = dialogView.findViewById(R.id.edtName);
                        TextView dTextCode = dialogView.findViewById(R.id.tvCode);

                        edtName.setText(ref.getName());
                        dTextCode.setText(ref.getCode());

                        AlertDialog.Builder dialog = new AlertDialog.Builder(ManageFridgeActivity.this);
                        dialog.setView(dialogView)
                                .setTitle("냉장고 정보")
                                .setPositiveButton("수정", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        // DB 수정
                                        RequestQueue requestQueue = Volley.newRequestQueue(ManageFridgeActivity.this);

                                        StringRequest jsonArrayRequest = new StringRequest(
                                                Request.Method.POST,
                                                "http://jms1132.dothome.co.kr/updateFridge.php",
                                                new Response.Listener<String>() {

                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response);
                                                            boolean success = jsonObject.getBoolean("success");
                                                            if(success){
                                                                // 수정 확인
                                                                simpleCookieBar(edtName.getText().toString().trim() + "(으)로 수정되었습니다.", ManageFridgeActivity.this);

                                                                // 데이터 변경 알림
                                                                adapter.notifyDataSetChanged();
                                                                ShowFoodsFragment.refrigeratorList.set(position,
                                                                        new RefrigeratorData(ref.getCode(), edtName.getText().toString(), ref.getType()));
                                                                dialog.dismiss();
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "수정 실패", Toast.LENGTH_LONG).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener(){
                                                    @Override
                                                    public void onErrorResponse(VolleyError error){
                                                        // Do something when error occurred
                                                        Log.d(TAG, error.toString());
                                                    }
                                                }
                                        ){
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                // 보내줄 인자
                                                params.put("name", edtName.getText().toString().trim());
                                                params.put("code", ref.getCode());
                                                return params;
                                            }
                                        };
                                        requestQueue.add(jsonArrayRequest);

                                    }
                                })
                                .setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 확인 받기
                                        AlertDialog.Builder delete = new AlertDialog.Builder(ManageFridgeActivity.this);
                                        delete.setCancelable(false)
                                                .setTitle(ref.getName() + "(을)를 정말로 삭제하시겠어요?")
                                                .setMessage("냉장고에 포함된 음식도 삭제됩니다")
                                                .setNeutralButton("삭제하기", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(final DialogInterface dialog, int which) {
                                                        // DB 삭제
                                                        RequestQueue requestQueue = Volley.newRequestQueue(ManageFridgeActivity.this);

                                                        // Initialize a new JsonArrayRequest instance
                                                        StringRequest jsonArrayRequest = new StringRequest(
                                                                Request.Method.POST,
                                                                "http://jms1132.dothome.co.kr/deleteFridge.php",
                                                                new Response.Listener<String>() {

                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        try {
                                                                            JSONObject jsonObject = new JSONObject(response);
                                                                            boolean success = jsonObject.getBoolean("success");
                                                                            if(success){
                                                                                // 삭제 확인
                                                                                deleteManage(ref); // manageTBL에서 삭제
                                                                                deleteFood(ref); // foodTBL에서 삭제
                                                                                simpleCookieBar(ref.getName() + "(이)가 삭제되었습니다.", ManageFridgeActivity.this);

                                                                                // 데이터 변경 알림
                                                                                ShowFoodsFragment.refrigeratorList.remove(ref);
                                                                                adapter.notifyDataSetChanged();
                                                                                if(ShowFoodsFragment.refrigeratorList.size() != 0){
                                                                                    ShowFoodsFragment.selectedFridge = ShowFoodsFragment.refrigeratorList.get(0);
                                                                                }

                                                                                dialog.dismiss();
                                                                            }else{
                                                                                Toast.makeText(getApplicationContext(), "삭제 실패", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                },
                                                                new Response.ErrorListener(){
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error){
                                                                        // Do something when error occurred
                                                                        Log.d(TAG, error.toString());
                                                                    }
                                                                }

                                                        ){
                                                            @Override
                                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                                Map<String, String> params = new HashMap<>();
                                                                // 보내줄 인자
                                                                params.put("code", ref.getCode());
                                                                return params;
                                                            }
                                                        };
                                                        requestQueue.add(jsonArrayRequest);

                                                    }
                                                })
                                                .setNegativeButton("취소", null)
                                                .show();
                                    }
                                })
                                .setNeutralButton("냉장고 목록에서 제거", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteManage(ref);
                                    }
                                })
                                .show();
                        return false;
                    }
                });

            }

            if(swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshing(false);
            }

            return convertView;
        }

        // 코드로 음식을 삭제하는 쿼리문 실행
        private void deleteFood(RefrigeratorData ref) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if(success){
                            Log.d(TAG, "연결 음식 삭제 완료");
                        }else{
                            Log.d(TAG, "연결 음식 삭제 실패");
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            };

            DeleteFood deleteFood = new DeleteFood(ref.getCode(), responseListener);
            RequestQueue requestQueue = Volley.newRequestQueue(ManageFridgeActivity.this);
            requestQueue.add(deleteFood);
        }

        // 코드로 냉장고 불러오는 쿼리문 실행
        private void selectFridgeByCode(final String code) {
            // Initialize a new RequestQueue instance
            RequestQueue requestQueue = Volley.newRequestQueue(ManageFridgeActivity.this);
            Log.d(TAG, MainActivity.strId);

            StringRequest jsonArrayRequest = new StringRequest(
                    Request.Method.POST,
                    "http://jms1132.dothome.co.kr/getFridgeByCode.php",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("food");

                                // 잘못된 코드일 경우
                                if(jsonArray.length() == 0) {
                                    Toast.makeText(getApplicationContext(), "해당 냉장고를 찾을 수 없습니다", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                for(int i = 0 ; i < jsonArray.length() ; i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    RefrigeratorData refrigerator = new RefrigeratorData();

                                    simpleCookieBar("냉장고를 불러왔습니다.", ManageFridgeActivity.this);
                                    refrigerator.setCode(object.getString("code"));
                                    refrigerator.setName(object.getString("name"));
                                    refrigerator.setType(object.getString("type"));

                                    ShowFoodsFragment.refrigeratorList.add(refrigerator);
                                    Log.d(TAG, refrigerator.getName() + "추가");

                                    // 데이터 변경 알림
                                    adapter.notifyDataSetChanged();
                                }

                            }catch (JSONException e){
                                Log.e(TAG, e.toString());
                                Toast.makeText(getApplicationContext(), "냉장고 불러오기 실패", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            // Do something when error occurred
                            Log.e(TAG, error.toString());
                            Toast.makeText(getApplicationContext(), "에러 발생" , Toast.LENGTH_LONG).show();
                        }

                    }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    // 보내줄 인자
                    params.put("code", code);
                    return params;
                }
            };

            // Add JsonArrayRequest to the RequestQueue
            requestQueue.add(jsonArrayRequest);
        }
    }

    // manageTBL에서 삭제하는 쿼리문 실행
    private void deleteManage(final RefrigeratorData refrigerator){
        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        Log.d(TAG, "manageTBL 삭제되었습니다.");
                        ShowFoodsFragment.refrigeratorList.remove(refrigerator);
                        adapter.notifyDataSetChanged();
                    }else{
                        // 잘못된 코드일 경우
                        Toast.makeText(getApplicationContext(), "삭제 실패", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        DeleteManage deleteManage = new DeleteManage(MainActivity.strId, refrigerator.getCode(), responseListener2);
        RequestQueue requestQueue2 = Volley.newRequestQueue(ManageFridgeActivity.this);
        requestQueue2.add(deleteManage);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    public static void simpleCookieBar(String message, Activity activity){
        try{
            CookieBar.build(activity)
                    .setTitle(message)
                    .setSwipeToDismiss(true)
                    .setEnableAutoDismiss(true)
                    .setDuration(2000)
                    .setCookiePosition(CookieBar.BOTTOM)
                    .show();
        }catch (NullPointerException e){
            Log.e(TAG, e.toString());
        }
    }
}
