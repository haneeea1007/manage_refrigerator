package com.example.refrigeproject.setting;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.refrigeproject.DBHelper;
import com.example.refrigeproject.R;
import com.example.refrigeproject.show_foods.RefrigeratorData;
import com.example.refrigeproject.show_foods.ShowFoodsFragment;
import com.r0adkll.slidr.Slidr;

import java.util.Random;

public class AddFridgeActivity extends AppCompatActivity implements View.OnClickListener {

    // Widget
    RadioGroup radioGroup;
    RadioButton rbRef1, rbRef2, rbRef3;
    Button btnRefAdd;
    EditText edtTxt;

    // DB
    private DBHelper fridgeDBHelper;
    SQLiteDatabase sqLiteDatabase;
    String refName;
    int imgSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_fridge);
        Slidr.attach(this);

        fridgeDBHelper = new DBHelper(this);

        radioGroup = findViewById(R.id.radioGroup);
        rbRef1 = findViewById(R.id.rbRef1);
        rbRef2 = findViewById(R.id.rbRef2);
        rbRef3 = findViewById(R.id.rbRef3);
        btnRefAdd = findViewById(R.id.btnRefAdd);
        edtTxt = findViewById(R.id.edtTxt);

        btnRefAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        btnRefAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("BUTTON", "click");

                // 이름 입력 안했을 때 저장X (정상작동)
                if ((edtTxt.getText().toString()).equals("")) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해 주세요.", Toast.LENGTH_LONG).show();

                } else if (radioGroup.getCheckedRadioButtonId() != R.id.rbRef1 &&
                        radioGroup.getCheckedRadioButtonId() != R.id.rbRef2 &&
                        radioGroup.getCheckedRadioButtonId() != R.id.rbRef3) {
                    Toast.makeText(getApplicationContext(), "냉장고 유형을 선택해 주세요.", Toast.LENGTH_LONG).show();

                } else {

                    // 랜덤코드 만들기 (정상작동)
                    Random random = new Random();
                    StringBuffer bufCode = new StringBuffer();
                    String randomCode;

                    for (int i = 0; i < 20; i++) {
                        if (random.nextBoolean()) {
                            bufCode.append((char) ((int) (random.nextInt(26)) + 97));
                        } else {
                            bufCode.append((random.nextInt(10)));
                        }
                    }

                    randomCode = bufCode.toString().trim();
                    Log.d("CODE", randomCode);

                    // 냉장고 이름 가져오기
                    refName = edtTxt.getText().toString().trim();
                    Log.d("NAME", refName);

                    switch (radioGroup.getCheckedRadioButtonId()){
                        case R.id.rbRef1:
                            imgSource = R.drawable.fridge1;
                            break;
                        case R.id.rbRef2:
                            imgSource = R.drawable.fridge2;
                            break;
                        case R.id.rbRef3:
                            imgSource = R.drawable.fridge3;
                            break;
                    }

                    Log.d("IMAGE", String.valueOf(R.drawable.fridge1));
                    Log.d("IMAGE", imgSource+"");


                    // DB에 저장
                    sqLiteDatabase = fridgeDBHelper.getWritableDatabase();

                    String str = "INSERT INTO refrigeratorTBL values('" +
                            randomCode + "', '" +
                            refName + "', " +
                            imgSource + ");";

                    sqLiteDatabase.execSQL(str);
                    sqLiteDatabase.close();

                    // ArrayList에 저장
                    ShowFoodsFragment.refrigeratorList.add(new RefrigeratorData(randomCode, refName, imgSource));

                    Toast.makeText(getApplicationContext(), refName + " 추가되었습니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}
