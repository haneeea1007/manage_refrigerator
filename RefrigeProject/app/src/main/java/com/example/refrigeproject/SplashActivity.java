package com.example.refrigeproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), KakaoLoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

//        Intent intent = new Intent(this, KakaoLoginActivity.class);
//        startActivity(intent);

//        finish();
    }
}
