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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.refrigeproject.DBHelper;
import com.example.refrigeproject.R;
import com.example.refrigeproject.database.RefrigeratorRequest;
import com.example.refrigeproject.show_foods.RefrigeratorData;
import com.example.refrigeproject.show_foods.ShowFoodsFragment;
import com.r0adkll.slidr.Slidr;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class AddFridgeActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "AddFridgeActivity";

    // Widget
    private RadioGroup radioGroup;
    private RadioButton rbRef1, rbRef2, rbRef3;
    private Button btnRefAdd;
    private EditText edtTxt;

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
                    Log.d(TAG, "랜덤코드" + randomCode);

                    // 냉장고 이름 가져오기
                    refName = edtTxt.getText().toString().trim();
                    Log.d(TAG, "냉장고 이름" + refName);

                    String type = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                    Log.d(TAG , type);


                    // DB에 저장
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if(success){
                                    Toast.makeText(getApplicationContext(), refName + " 추가되었습니다.", Toast.LENGTH_LONG).show();
                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(), refName + " 추가 실패", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    RefrigeratorRequest registerRequest = new RefrigeratorRequest(randomCode, refName, type, responseListener); // 자료 다 들어있음. 상대방주소,데이터,데이터랩방식 등
                    RequestQueue requestQueue = Volley.newRequestQueue(AddFridgeActivity.this);
                    requestQueue.add(registerRequest);

//                    sqLiteDatabase = fridgeDBHelper.getWritableDatabase();
//
//                    String str = "INSERT INTO refrigeratorTBL values('" +
//                            randomCode + "', '" +
//                            refName + "', " +
//                            imgSource + ");";
//
//                    sqLiteDatabase.execSQL(str);
//                    sqLiteDatabase.close();

                    // ArrayList에 저장
                    ShowFoodsFragment.refrigeratorList.add(new RefrigeratorData(randomCode, refName, imgSource));

                }
            }
        });
    }
}
