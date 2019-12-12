package com.example.refrigeproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class KakaoLoginMainActivity extends Activity {

    String strNickname, strProfile, strId;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_main);

        TextView tvNickname = findViewById(R.id.txtNickName);
        TextView tvProfile = findViewById(R.id.txtImg);
        ImageView imgPro = findViewById(R.id.imgPro);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnRef = findViewById(R.id.btnRef);

        Intent intent = getIntent();
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");
        strId = String.valueOf(intent.getLongExtra("id", 0));

        tvNickname.setText(strNickname + "님 환영합니다!");
//        tvProfile.setText(strProfile);
        tvProfile.setText("아이디 : " + strId);

        Log.d("KakaoLoginMainActivity", strProfile);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogout();
                Toast.makeText(getApplicationContext(), "로그아웃 합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btnRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRef = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentRef);
            }
        });

        // 1. imgPro.setImageResource(Integer.parseInt(strProfile));
        /*
        2.
        try {
            URL url = new URL(strProfile);
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            imgPro.setImageBitmap(bm);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Thread thread = new Thread(){
            @Override
            public void run(){

                try{
                    URL url = new URL(strProfile);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        try {
            thread.join();
            imgPro.setImageBitmap(bitmap);
        }catch (InterruptedException e){

        }
    }

    private void onClickLogout() {

        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {

                Intent intent = new Intent(KakaoLoginMainActivity.this, KakaoLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}