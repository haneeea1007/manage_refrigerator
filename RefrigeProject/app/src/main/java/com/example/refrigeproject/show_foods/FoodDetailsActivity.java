package com.example.refrigeproject.show_foods;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.refrigeproject.R;

public class FoodDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton ibtBack;
    private TextView tvDone, tvCategory, tvGroup;
    private EditText edtName;
    private DatePicker datePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        ibtBack = findViewById(R.id.ibtBack);
        tvDone = findViewById(R.id.tvDone);
        tvGroup = findViewById(R.id.tvGroup);
        edtName = findViewById(R.id.edtName);
        datePicker = findViewById(R.id.datePicker);

        tvDone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvDone:
                // 알람연결
                // DB 내용 수정
                finish();
                break;
        }
    }
}
