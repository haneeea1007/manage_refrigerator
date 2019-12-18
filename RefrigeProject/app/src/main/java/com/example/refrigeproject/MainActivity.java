package com.example.refrigeproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.refrigeproject.calendar.CalendarFragment;
import com.example.refrigeproject.checklist.CheckListFragment;
import com.example.refrigeproject.database.UserRequest;
import com.example.refrigeproject.search_recipe.SearchRecipeFragment;
import com.example.refrigeproject.setting.SettingFragment;
import com.example.refrigeproject.show_foods.ShowFoodsFragment;
import com.facebook.stetho.Stetho;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements ShowFoodsFragment.OnFragmentInteractionListener{
    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomMenu;
    private FrameLayout frameLayout;
    private Fragment calendar, searchRecipe, showFoods, checkList, setting;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Bundle bundle;
    private String strNickname, strProfile;
    public static String strId;
    private long backButtonTime = 0L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "메인 아이디 " + strId);

        Stetho.initializeWithDefaults(this);

        bottomMenu = findViewById(R.id.bottomMenu);
        frameLayout = findViewById(R.id.frameLayout);

        bottomMenu.setSelectedItemId(R.id.action_3);
        changeFragment(3);

        // bottomMenu를 변경을 때 Fragment
        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_1: changeFragment(1); break;
                    case R.id.action_2: changeFragment(2); break;
                    case R.id.action_3: changeFragment(3); break;
                    case R.id.action_4: changeFragment(4); break;
                    case R.id.action_5: changeFragment(5); break;
                }
                return true;
            }
        });

        calendar = new CalendarFragment();
        searchRecipe = new SearchRecipeFragment();
        showFoods = new ShowFoodsFragment();
        checkList = new CheckListFragment();
        setting = new SettingFragment();

        Intent intent = getIntent();
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");
        strId = String.valueOf(intent.getLongExtra("id", -1));
        // 사용자 정보 DB에 저장
        insertUserData();

        // 인텐트로 셋팅 프래그먼트에 전달
        Intent putIntent = new Intent(this, SettingFragment.class);
        putIntent.putExtra("name", strNickname); // 유저 닉네임
        putIntent.putExtra("profile", strProfile); // 카카오톡 프로필 이미지
        putIntent.putExtra("id", strProfile); // 유저 고유 아이디

    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        long getTime = currentTime - backButtonTime;

        if (getTime >= 0 && getTime <= 2000) {
            super.onBackPressed();
        } else {
            backButtonTime = currentTime;
        }
    }

    private void insertUserData() {
        // insert into userTBL
        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        Toast.makeText(getApplicationContext(), strNickname + "님 안녕하세요", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),  "로그인 실패", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        UserRequest userRequest = new UserRequest(strId, strProfile, strNickname, responseListener2); // 자료 다 들어있음. 상대방주소,데이터,데이터랩방식 등
        RequestQueue requestQueue2 = Volley.newRequestQueue(MainActivity.this);
        requestQueue2.add(userRequest);
    }

    private void changeFragment(int position) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (position){
            case 1:
                fragmentTransaction.replace(R.id.frameLayout, calendar); break;
            case 2:
                fragmentTransaction.replace(R.id.frameLayout, searchRecipe);
                searchRecipe.setArguments(bundle);
                break;
            case 3:
                fragmentTransaction.replace(R.id.frameLayout, new ShowFoodsFragment()); break;
            case 4:
                fragmentTransaction.replace(R.id.frameLayout, checkList); break;
            case 5:
                fragmentTransaction.replace(R.id.frameLayout, setting); break;
        }

        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Bundle bundle) {
        this.bundle = bundle;
        bottomMenu.setSelectedItemId(R.id.action_2);
    }
}
