package com.example.refrigeproject.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.refrigeproject.R;

public class SettingFragment extends Fragment implements View.OnClickListener {
    View view;

    LinearLayout llManage,llAlarm, llShare, llReport;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, null, false);

        llManage = view.findViewById(R.id.llManage);
        llShare = view.findViewById(R.id.llShare);
        llAlarm = view.findViewById(R.id.llAlarm);
        llReport = view.findViewById(R.id.llReport);

        llManage.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llAlarm.setOnClickListener(this);
        llReport.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llManage:
                Intent intent = new Intent(getActivity(), ManageFridgeActivity.class);
                startActivity(intent);

                break;

            case R.id.llShare:

                break;

            case R.id.llReport:
                Toast.makeText(getContext(), "문의 하지마", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;

                // 관리하기에 더 넣을 만한 것들 ?
        }
    }
}
