package com.example.refrigeproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import androidx.appcompat.app.AppCompatActivity;

public class KakaoLoginActivity extends AppCompatActivity {

    private SessionCallback sessionCallback;
    private Button btnLogin;
    private LoginButton kakaoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login);

        // 키해시 구할 때 쓰는 함수
        //getHashKey();

        sessionCallback = new SessionCallback(); // SessionCallback 초기화
        Session.getCurrentSession().addCallback(sessionCallback); // 현재 세션에 콜백 붙임.
        Session.getCurrentSession().checkAndImplicitOpen(); // 자동 로그인

        btnLogin = findViewById(R.id.btnLogin);
        kakaoLogin = findViewById(R.id.kakaoLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kakaoLogin.performClick();
            }
        });

        /*btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionCallback = new SessionCallback(); // SessionCallback 초기화
                Session.getCurrentSession().addCallback(sessionCallback); // 현재 세션에 콜백 붙임.
                Session.getCurrentSession().checkAndImplicitOpen(); // 자동 로그인
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 현재 액티비티 제거시 콜백도 같이 제거
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private class SessionCallback implements ISessionCallback {

        // 로그인 세션이 열렸을때.
        @Override
        public void onSessionOpened() {

            // 유저 정보를 받아오는 함수 이름 me
            UserManagement.getInstance().me(new MeV2ResponseCallback() {

                // 로그인에 실패했을 때
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                // 로그인 도중 세션이 비정상적인 이유로 닫혔을 때.
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                // 로그인에 성공했을 때. MeV2Response 객체 넘어오는데, 로그인한 유저의 정보를 담고 있는 중요객체.
                @Override
                public void onSuccess(MeV2Response result) {

                    // 인텐트를 통해 메인 액티비티에 유저정보 전달
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("name", result.getNickname()); // 유저 닉네임
                    intent.putExtra("profile", result.getProfileImagePath()); // 카카오톡 프로필 이미지
                    intent.putExtra("id", result.getId()); // 유저 고유 아이디

                    startActivity(intent);
                    finish();
                }
            });
        }

        // 로그인 세션이 정상적으로 열리지 않았을 때.
        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /*private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }*/
}