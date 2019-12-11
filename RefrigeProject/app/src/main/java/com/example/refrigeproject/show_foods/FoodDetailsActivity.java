package com.example.refrigeproject.show_foods;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.refrigeproject.R;

public class FoodDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton ibtBack;
    private TextView tvDone, tvCategory, tvGroup;
    private EditText edtName;
    private DatePicker datePicker;
    private String foodName;
    private ImageView foodImage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        ibtBack = findViewById(R.id.ibtBack);
        tvDone = findViewById(R.id.tvDone);
        tvCategory = findViewById(R.id.tvCategory);
        tvGroup = findViewById(R.id.tvGroup);
        edtName = findViewById(R.id.edtName);
        datePicker = findViewById(R.id.datePicker);
        foodImage = findViewById(R.id.foodImage);

        tvDone.setOnClickListener(this);
        ibtBack.setOnClickListener(this);

        setData();
    }

    private void setData() {
        Intent intent = getIntent();
        String from = intent.getStringExtra("from");
        Toast.makeText(getApplicationContext(), from, Toast.LENGTH_SHORT).show();
        switch (from){
            case "GridViewAdapter" :
                int category = intent.getIntExtra("category", 0);
                tvCategory.setText(getCategory(category)); // 타이틀로 바꾸기

                String section = intent.getStringExtra("section");
                tvGroup.setText(section);
                edtName.setText(section);

                int image = intent.getIntExtra("image", 0);
                foodImage.setImageResource(image);
                break;
        }
    }

    private String getCategory(int category) {
        switch (category){
            case 0: return "야채";
            case 1: return "과일";
            case 2: return "육류";
            case 3: return "해산물";
            case 4: return "유제품";
            case 5: return "반찬";
            case 6: return "인스턴트";
            case 7: return "음료";
            case 8: return "양념";
            case 9: return "조미료";
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvDone:

                // 알람연결
                // DB 내용 수정
                finish();
                break;

            case R.id.ibtBack:
                finish();
                break;
        }
    }
}
