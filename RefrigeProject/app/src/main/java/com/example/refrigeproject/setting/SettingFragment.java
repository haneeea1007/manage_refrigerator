package com.example.refrigeproject.setting;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import com.example.refrigeproject.R;
import com.example.refrigeproject.SplashActivity;
import com.example.refrigeproject.show_foods.AlarmReceiver;
import com.example.refrigeproject.show_foods.FoodDetailsActivity;
import com.example.refrigeproject.show_foods.ListViewAdapter;
import com.example.refrigeproject.show_foods.ShowFoodsFragment;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    View view;

    CircleImageView imgProfile;
    TextView tvUserName;
    LinearLayout llManage, llAlarm, llShare, llReport, llLogout;
    RadioGroup rdoGroup;
    RadioButton rdo1Day, rdo3Day, rdo7Day;
    Switch switchAlarm;
    String strNickname, strProfile, strId;
    Bitmap bitmap;
    private final String PREFERENCE = "com.example.refrigeproject";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, null, false);

        llManage = view.findViewById(R.id.llManage);
        llShare = view.findViewById(R.id.llShare);
        llAlarm = view.findViewById(R.id.llAlarm);
        llReport = view.findViewById(R.id.llReport);
        llLogout = view.findViewById(R.id.llLogout);
        tvUserName = view.findViewById(R.id.tvUserName);
        imgProfile = view.findViewById(R.id.imgProfile);

        rdoGroup = view.findViewById(R.id.rdoGroup);
        rdo1Day = view.findViewById(R.id.rdo1Day);
        rdo3Day = view.findViewById(R.id.rdo3Day);
        rdo7Day = view.findViewById(R.id.rdo7Day);
        switchAlarm = view.findViewById(R.id.switchAlarm);

        // 라디오버튼 체크값 기본 셋팅
        rdo1Day.setChecked(true);
        rdo1Day.setBackgroundResource(R.drawable.btnon);
        rdo3Day.setBackgroundResource(R.drawable.btnoff);
        rdo7Day.setBackgroundResource(R.drawable.btnoff);

        llManage.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llAlarm.setOnClickListener(this);
        llReport.setOnClickListener(this);

        rdoGroup.setOnCheckedChangeListener(this);

        // 쉐어드프레퍼런스 체크값 가져와서 셋팅하기
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFERENCE, MODE_PRIVATE);

        switchAlarm.setOnCheckedChangeListener(this);

        if (sharedPreferences.getBoolean("switchPref", false) == true) {
            switchAlarm.setChecked(true);
            switchAlarm.setText("on");
        } else if (sharedPreferences.getBoolean("switchPref", false) == false) {
            switchAlarm.setChecked(false);
            switchAlarm.setText("off");
        }

        if (sharedPreferences.getInt("radioPref", 0) == 1) {
            rdo1Day.setChecked(true);
            rdo1Day.setBackgroundResource(R.drawable.btnon);
            rdo3Day.setBackgroundResource(R.drawable.btnoff);
            rdo7Day.setBackgroundResource(R.drawable.btnoff);
        } else if (sharedPreferences.getInt("radioPref",0) == 3) {
            rdo3Day.setChecked(true);
            rdo1Day.setBackgroundResource(R.drawable.btnoff);
            rdo3Day.setBackgroundResource(R.drawable.btnon);
            rdo7Day.setBackgroundResource(R.drawable.btnoff);
        } else if (sharedPreferences.getInt("radioPref",0) == 7) {
            rdo7Day.setChecked(true);
            rdo1Day.setBackgroundResource(R.drawable.btnoff);
            rdo3Day.setBackgroundResource(R.drawable.btnoff);
            rdo7Day.setBackgroundResource(R.drawable.btnon);
        }

        // 인텐트로 유저 닉네임, 프로필사진, 고유아이디 받아오기
        Intent intent = this.getActivity().getIntent();
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");
        strId = String.valueOf(intent.getLongExtra("id", 0));

        tvUserName.setText(strNickname);
        Log.d("KakaoLoginMainActivity", strId);

        // 로그아웃 버튼 클릭 이벤트
        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogout();
                Toast.makeText(view.getContext(), "로그아웃이 완료되었습니다." + '\n' + "다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 프로필 이미지 셋팅
        Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    URL url = new URL(strProfile);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        try {
            thread.join();
            imgProfile.setImageBitmap(bitmap);
        } catch (InterruptedException e) {

        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llManage:
                // 냉장고 관리
                Intent intent = new Intent(getActivity(), ManageFridgeActivity.class);
                startActivity(intent);

                break;

            case R.id.llShare:
                // 냉장고 공유
                CookieBar.build(getActivity())
                        .setCustomView(R.layout.cookiebar_select_fridge)
                        .setCustomViewInitializer(new CookieBar.CustomViewInitializer() {
                            @Override
                            public void initView(View view) {

                                ListView listView = view.findViewById(R.id.listView);
                                ListViewAdapter listViewAdapter = new ListViewAdapter(getActivity(), null);
                                listView.setAdapter(listViewAdapter);
                            }
                        })
                        .setAction("Close", new OnActionClickListener() {
                            @Override
                            public void onClick() {
                                CookieBar.dismiss(getActivity());
                            }
                        })
                        .setSwipeToDismiss(true)
                        .setEnableAutoDismiss(true)
                        .setDuration(5000)
                        .setCookiePosition(CookieBar.BOTTOM)
                        .show();

                break;

            case R.id.llReport:
                // 문의하기
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] address = {"k012497@gmail.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                email.putExtra(Intent.EXTRA_SUBJECT, "어플 문의");
                email.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(email);
                break;

            default:
                break;
        }
    }

    // 라디오버튼 체크 감지
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        // 라디오버튼 체크값을 쉐어드프레퍼런스에 저장
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (radioGroup.getCheckedRadioButtonId() == R.id.rdo1Day) {
            editor.putInt("radioPref", 1);
            editor.commit();
            rdo1Day.setBackgroundResource(R.drawable.btnon);
            rdo3Day.setBackgroundResource(R.drawable.btnoff);
            rdo7Day.setBackgroundResource(R.drawable.btnoff);
            Toast.makeText(getContext(), "알림이 소비 만료일자 1일 전으로 설정되었습니다.", Toast.LENGTH_SHORT).show();

        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdo3Day) {
            editor.putInt("radioPref", 3);
            editor.commit();
            rdo1Day.setBackgroundResource(R.drawable.btnoff);
            rdo3Day.setBackgroundResource(R.drawable.btnon);
            rdo7Day.setBackgroundResource(R.drawable.btnoff);
            Toast.makeText(getContext(), "알림이 소비 만료일자 3일 전으로 설정되었습니다.", Toast.LENGTH_SHORT).show();

        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdo7Day) {
            editor.putInt("radioPref", 7);
            editor.commit();
            rdo1Day.setBackgroundResource(R.drawable.btnoff);
            rdo3Day.setBackgroundResource(R.drawable.btnoff);
            rdo7Day.setBackgroundResource(R.drawable.btnon);
            Toast.makeText(getContext(), "알림이 소비 만료일자 7일 전으로 설정되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 스위치 체크 감지
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        // 스위치 체크값을 쉐어드프레퍼런스에 저장
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (isChecked == true) {
            switchAlarm.setText("on");
            editor.putBoolean("switchPref", true);
            editor.commit();

        } else if (isChecked == false) {
            switchAlarm.setText("off");
            editor.putBoolean("switchPref", false);
            editor.commit();
        }
//        Toast.makeText(getContext(), "알람 설정 : " + isChecked, Toast.LENGTH_SHORT).show();
    }

    // 로그아웃 버튼 클릭 이벤트
    private void onClickLogout() {

        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("logout", true);
                editor.commit();

                // 알람 다 지우기
                deleteAlarm();

                // 선택된 냉장고 값 지우기
                ShowFoodsFragment.selectedFridge = null;

                Intent intent = new Intent(view.getContext(), SplashActivity.class);
                startActivity(intent);

//                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//                dialog.setTitle("로그아웃 확인")
//                        .setMessage("로그아웃 하시겠습니까?")
//                        .setNegativeButton("취소", null)
//                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(view.getContext(), SplashActivity.class);
//                                startActivity(intent);
//                                Toast.makeText(view.getContext(), "로그아웃이 완료되었습니다." + '\n' + "다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
//                            }
//                        }).show();
            }
        });
    }

    private void deleteAlarm() {
        // 알람도 같이 삭제
        for(int i = 0 ; i < ShowFoodsFragment.alarmList.size() ; i++){
            Log.d("delete alarm", ShowFoodsFragment.alarmList.get(i).getAlarmID()+" 알람");
            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            PendingIntent pender = PendingIntent.getBroadcast(getContext(), ShowFoodsFragment.alarmList.get(i).getAlarmID(), intent, 0);
            alarmManager.cancel(pender);
        }
    }
}

