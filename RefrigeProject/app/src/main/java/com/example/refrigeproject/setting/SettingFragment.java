package com.example.refrigeproject.setting;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.refrigeproject.R;
import com.example.refrigeproject.show_foods.FoodDetailsActivity;

public class SettingFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    View view;

    LinearLayout llManage, llAlarm, llShare, llReport;
    RadioGroup rdoGroup;
    Switch switchAlarm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, null, false);

        llManage = view.findViewById(R.id.llManage);
        llShare = view.findViewById(R.id.llShare);
        llAlarm = view.findViewById(R.id.llAlarm);
        llReport = view.findViewById(R.id.llReport);

        rdoGroup = view.findViewById(R.id.rdoGroup);
        switchAlarm = view.findViewById(R.id.switchAlarm);

        llManage.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llAlarm.setOnClickListener(this);
        llReport.setOnClickListener(this);

        rdoGroup.setOnCheckedChangeListener(this);
        switchAlarm.setOnCheckedChangeListener(this);

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
                Intent sharedMessage = new Intent(Intent.ACTION_SEND);
                sharedMessage.addCategory(Intent.CATEGORY_DEFAULT);
                sharedMessage.putExtra(Intent.EXTRA_SUBJECT, "언니 올때 메로나 냉동실^^");
                sharedMessage.putExtra(Intent.EXTRA_TEXT,   '\n' + "냉장고 열쇠: " + "");
                sharedMessage.setType("text/plain");
                startActivity(Intent.createChooser(sharedMessage, "냉장고 공유하기"));

                break;

            case R.id.llReport:
                // 문의하기
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] address = {"k012497@gmail.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                email.putExtra(Intent.EXTRA_SUBJECT,"어플 문의");
                email.putExtra(Intent.EXTRA_TEXT,"");
                startActivity(email);
                break;

            default:
                break;

            // 관리하기에 더 넣을 만한 것들 ?
        }
    }

    // 라디오버튼 체크 감지
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        Intent radioIntent = new Intent(getContext(), FoodDetailsActivity.class);

        if (radioGroup.getCheckedRadioButtonId() == R.id.rdo1Day) {
            radioIntent.putExtra("dateSetting", 1);

        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdo3Day) {
            radioIntent.putExtra("dateSetting", 3);

        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdo7Day) {
            radioIntent.putExtra("dateSetting", 7);

        }
    }

    // 스위치 체크 감지
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        Intent switchIntent = new Intent(getContext(), FoodDetailsActivity.class);
        switchIntent.putExtra("switchSetting", isChecked);
    }
}

