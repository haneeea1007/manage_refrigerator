package com.example.refrigeproject.show_foods;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.refrigeproject.DBHelper;
import com.example.refrigeproject.MainActivity;
import com.example.refrigeproject.R;
import com.example.refrigeproject.setting.ManageFridgeActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.r0adkll.slidr.Slidr;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FoodDetailsActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private final static String TAG = "FoodDetailsActivity";
    private Context context;

    private ImageButton ibtBack;
    private TextView tvDone, tvCategory, tvGroup, tvTitle, tvPurchaseDate, tvExpirationDate, tvRefrige, tvRecommend;
    private EditText edtName, edtMemo;
    private RadioGroup rdoGroup;
    private RadioButton rdoFridge, rdoFreezer, rdoPantry;
    private DatePicker datePicker;
    private ImageView foodImage;
    private String foodName;
    private LinearLayout llRefrige;

    private File tempFile;
    private Calendar calendar;
    private Calendar calendarTemp;
    private DBHelper dBHelper;
    private SQLiteDatabase sqLiteDatabase;
    private AlarmManager alarmManager;

    private int dateSetting;
    private int alarmID;
    private boolean switchSetting;
    private String notiContent;
    private String rdoClick;

//    private int getYearPur;
//    private int getMonthPur;
//    private int getDayPur;
    private int getYearEx;
    private int getMonthEx;
    private int getDayEx;

    private Long millis;
    private Long millisTemp;
    Calendar today;

    // 리퀘스트코드
    private static final int PICK_FROM_CAMERA = 1;

    // 어디서 불렀는지 리퀘스트 코드를 넣고 INSERT 가 필요한지 UPDATE 가 필요한지 구분하기
    // 추가할때는 1로 인텐트, 수정할때는 2로 인텐트!!!!!
    private static final int FROM_ADD_OR_EDIT = 0;
    Intent intent;
    String action; // 이 액티비티를 부르면서 설정한 액션을 받는 변수 (수정 or 추가)
    FoodData food; // 인텐트로 받은 FoodData

    //DB
    private Bitmap bitmapTemp;
    private String encodedString;

    //Crawling
    private String htmlPageUrl;
    private String htmlContentInStringFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        Slidr.attach(this);

        context = getApplicationContext();
        dBHelper = new DBHelper(context);
        calendar = Calendar.getInstance();
        calendarTemp = Calendar.getInstance();

        ibtBack = findViewById(R.id.ibtBack);
        tvDone = findViewById(R.id.tvDone);
        tvCategory = findViewById(R.id.tvCategory);
        tvGroup = findViewById(R.id.tvGroup);
        tvTitle = findViewById(R.id.tvTitle);
        tvPurchaseDate = findViewById(R.id.tvPurchaseDate);
        tvExpirationDate = findViewById(R.id.tvExpirationDate);
        tvRefrige = findViewById(R.id.tvRefrige);
        edtName = findViewById(R.id.edtName);
        edtMemo = findViewById(R.id.edtMemo);
        rdoGroup = findViewById(R.id.rdoGroup);
        rdoFridge = findViewById(R.id.rdoFridge);
        rdoFreezer = findViewById(R.id.rdoFreezer);
        rdoPantry = findViewById(R.id.rdoPantry);
        datePicker = findViewById(R.id.datePicker);
        foodImage = findViewById(R.id.foodImage);
        llRefrige = findViewById(R.id.llRefrige);
        tvRecommend = findViewById(R.id.tvRecommend);

        tvDone.setOnClickListener(this);
        ibtBack.setOnClickListener(this);
        tvPurchaseDate.setOnClickListener(this);
        tvExpirationDate.setOnClickListener(this);
        tvRefrige.setOnClickListener(this);
        foodImage.setOnClickListener(this);
        rdoGroup.setOnCheckedChangeListener(this);

        intent = getIntent();
        action = intent.getAction(); // edit or add

        setData();

        // 권한 요청
        TedPermission.with(context)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
//                        Toast.makeText(context, "카메라 권한 요청이 허용되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(context, "카메라 권한 요청이 거절되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setRationaleMessage("카메라 권한 허용 요청")
                .setDeniedMessage("요청 거절시 카메라를 사용할 수 없습니다.")
                .setPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtName.getWindowToken(), 0);

    }

    private void setData() {
        switch(action){
            case "edit":
                food = intent.getParcelableExtra("food");
                tvTitle.setText(food.getName());
                if(food.getImagePath()!=null){
                    Glide.with(this).load(food.getImagePath()).into(foodImage);
                }
                tvCategory.setText(food.getCategory());
                tvGroup.setText(food.getSection());
                edtName.setText(food.getName());
                tvPurchaseDate.setText(food.getPurchaseDate());
                tvExpirationDate.setText(food.getExpirationDate());
                edtMemo.setText(food.getMemo());
                switch (food.getPlace()){
                    case "냉장": rdoFridge.setChecked(true); break;
                    case "냉동": rdoFreezer.setChecked(true); break;
                    case "실온": rdoPantry.setChecked(true); break;

                }
                llRefrige.setVisibility(View.GONE);

                break;
            case "add":
                tvTitle.setText("음식 추가하기");
                int category = intent.getIntExtra("category", 0);
                tvCategory.setText(getCategory(category)); // 타이틀로 바꾸기

                String section = intent.getStringExtra("section");
                tvGroup.setText(section);
                edtName.setText(section);
                tvRefrige.setText(ShowFoodsFragment.selectedFridge.getName());

                // 웹 크롤링
                crawlingDays();
//                tvRecommend.setText();

                int image = intent.getIntExtra("image", 0);
                foodImage.setImageResource(image);
                break;
        }
    }

    private void crawlingDays() {
        Log.d("CRAWLING TEST", "doInBackground " + tvCategory.getText().toString());
        htmlPageUrl = "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&query=" + tvGroup.getText().toString() +"+보관기간";
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Log.d("CRAWLING TEST", "doInBackground " + food);
                Document doc = Jsoup.connect(htmlPageUrl).get(); // 해당 페이지의 html 저

                //body > div:nth-child(4) > div.right_wrap > div.wrap_recipe > div > dl:nth-child(3) > dt
                Elements titles= doc.select(".result_area em.v");
                System.out.println("-------------------------------------------------------------");
                for(Element e: titles){
                    System.out.println("title: " + e.text());
                    htmlContentInStringFormat = "* 추천 보관일은 " + e.text().trim() + " 입니다.";
                    Log.d("CRAWLING TEST", e.text().trim());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("CRAWLING TEST", "onPostExecute");
            tvRecommend.setVisibility(View.VISIBLE);
            tvRecommend.setText(htmlContentInStringFormat);
        }
    }

    private String getCategory(int category) {
        switch (category) {
            case 0:
                return "야채";
            case 1:
                return "과일";
            case 2:
                return "육류";
            case 3:
                return "해산물";
            case 4:
                return "유제품";
            case 5:
                return "반찬";
            case 6:
                return "인스턴트";
            case 7:
                return "음료";
            case 8:
                return "양념";
            case 9:
                return "조미료";
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDone:
                // 입력 값이 없을 경우
                if(edtName.getText().toString().equals("") || tvPurchaseDate.getText().toString().equals("")
                    || rdoClick == null){
                    ManageFridgeActivity.simpleCookieBar("정보를 입력해주세요", FoodDetailsActivity.this);
                    return;
                }

                switch(action){
                    case "edit":
                        makeEncodedString(); // 이미지 처리
                        // DB 내용 수정
                        updateFood();
                        finish(); // resultcode보내기 & notify
                        break;
                    case "add":
                        makeEncodedString(); // 이미지 처리
                        uploadData();
                        Toast.makeText(context, edtName.getText().toString() + " 추가되었습니다.", Toast.LENGTH_SHORT).show();
                        // 소비만료 날짜 확인
//                Toast.makeText(context, calendar.get(Calendar.YEAR) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
                        finish();

                        break;
                }

            case R.id.ibtBack:
                // 뒤로가기
                finish();
                break;

            case R.id.tvPurchaseDate:
                // 구입일자

                DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        // 알림 날짜 설정
                        calendarTemp.set(Calendar.YEAR, year);
                        calendarTemp.set(Calendar.MONTH, month);
                        calendarTemp.set(Calendar.DATE, dayOfMonth);

                        // 현재보다 이후이면 등록 못하도록 함
                        if (calendarTemp.after(Calendar.getInstance())) {
                            Toast.makeText(context, "현재 날짜 이후로 설정할 수 없습니다.", Toast.LENGTH_LONG).show();
                            return;
                        }

//                        getYearPur = year;
//                        getMonthPur = month;
//                        getDayPur = dayOfMonth;

                        millisTemp = calendarTemp.getTimeInMillis();

                        // 날짜 표시
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        tvPurchaseDate.setText(String.valueOf(format.format(calendarTemp.getTime())));

                    }
                }, calendarTemp.get(Calendar.YEAR), calendarTemp.get(Calendar.MONTH), calendarTemp.get(Calendar.DAY_OF_MONTH));
                dialog.show();


                break;

            case R.id.tvExpirationDate:
                // 소비만료 일자

                DatePickerDialog dialog2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        // 알림 날짜 설정, 0시 0분
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DATE, dayOfMonth);
                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0);

                        // 현재 날짜, 0시 0분
                        today = Calendar.getInstance();
                        today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH) -1, 0, 0);

                        // 현재 날짜보다 이전이면 등록 못하도록 함
                        if (calendar.before(today)) {
//                        if (calendar.getTimeInMillis() < today.getTimeInMillis()) {
                            Toast.makeText(context, "현재 날짜 이전으로 설정할 수 없습니다.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        getYearEx = year;
                        getMonthEx = month;
                        getDayEx = dayOfMonth;

                        millis = calendar.getTimeInMillis();

                        // 날짜 표시
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        tvExpirationDate.setText(String.valueOf(format.format(calendar.getTime())));

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog2.show();

                break;

            case R.id.tvRefrige:
                // 냉장고 선택

                CookieBar.build(FoodDetailsActivity.this)
                        .setCustomView(R.layout.cookiebar_select_fridge)
                        .setCustomViewInitializer(new CookieBar.CustomViewInitializer() {
                            @Override
                            public void initView(View view) {

                                ListView listView = view.findViewById(R.id.listView);
                                ListViewAdapter listViewAdapter = new ListViewAdapter(FoodDetailsActivity.this, tvRefrige);
                                listView.setAdapter(listViewAdapter);
                            }
                        })
                        .setAction("Close", new OnActionClickListener() {
                            @Override
                            public void onClick() {
                                CookieBar.dismiss(FoodDetailsActivity.this);
                            }
                        })
                        .setSwipeToDismiss(true)
                        .setEnableAutoDismiss(true)
                        .setDuration(5000)
                        .setCookiePosition(CookieBar.BOTTOM)
                        .show();

                break;

            case R.id.foodImage:
                // 이미지 넣기
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                try {
                    tempFile = createImageFile();
                } catch (IOException e) {
                    Toast.makeText(context, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                if (tempFile != null) {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                        Uri photoUri = FileProvider.getUriForFile(context,
                                "com.example.refrigeproject.foodimage.provider", tempFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, PICK_FROM_CAMERA);

                    } else {

                        Uri photoUri = Uri.fromFile(tempFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, PICK_FROM_CAMERA);

                    }
                }
        }
    }

    private void makeEncodedString() {
        if(bitmapTemp == null){
            // 사진을 찍지 않고 아이콘을 이미지로 등록할 경우
            bitmapTemp = ((BitmapDrawable)foodImage.getDrawable()).getBitmap();
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmapTemp.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        encodedString = Base64.encodeToString(byte_arr, 0);
    }

    private void updateFood() {
        final int newRequestCode = makeAlarmId();
        // DB 수정
        RequestQueue rq = Volley.newRequestQueue(this);
        String url = "http://jms1132.dothome.co.kr/updateFood.php";
        Log.d("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.e("RESPONSE", response);
                    Toast.makeText(getBaseContext(),
                            "The image is upload", Toast.LENGTH_SHORT)
                            .show();

                } catch (Exception e) {
                    Log.d("JSON Exception", e.toString());
                    Toast.makeText(getBaseContext(),
                            "Error while loadin data!"+e.toString(),
                            Toast.LENGTH_LONG).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "Error [" + error + "]");
                Toast.makeText(getBaseContext(),
                        "Cannot connect to server"+error, Toast.LENGTH_LONG)
                        .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", String.valueOf(food.getId()));
                params.put("category", tvCategory.getText().toString());
                params.put("section", tvGroup.getText().toString());
                params.put("name", edtName.getText().toString());
                params.put("memo", edtMemo.getText().toString());
                params.put("image", encodedString);
                params.put("purchaseDate", tvPurchaseDate.getText().toString());
                params.put("expirationDate",tvExpirationDate.getText().toString());

                Log.d("DATA FOR UPDATE", tvCategory.getText().toString());
                Log.d("DATA FOR UPDATE", tvGroup.getText().toString());
                Log.d("DATA FOR UPDATE", edtName.getText().toString());
                Log.d("DATA FOR UPDATE", edtMemo.getText().toString());
                Log.d("DATA FOR UPDATE", tvPurchaseDate.getText().toString());
                Log.d("DATA FOR UPDATE", food.getPlace());

                // 보관유형을 바꿨을 경우에만 place 수정
                if(rdoClick == null){
                    params.put("place", food.getPlace());
                }else{
                    params.put("place", rdoClick);
                }

                // 유통기한을 바꿨을 경우에만 alarmID 수정
                if(!tvExpirationDate.getText().toString().equals(food.getExpirationDate())){
                    params.put("alarmID", String.valueOf(newRequestCode)); // 새로운 알람 아이디 부여
                    // 기존 알람 cancel 후 재생성하기 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    // 취소
                    // 재생성
                } else {
                    params.put("alarmID", String.valueOf(food.getAlarmID())); // 기존 알람 아이디 부여
                }

                return params;

            }

        };
        rq.add(stringRequest);
    }

    public void uploadData() {
        RequestQueue rq = Volley.newRequestQueue(this);
        String url = "http://jms1132.dothome.co.kr/food.php";
        Log.d("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.e("RESPONSE", response);
                    //JSONObject json = new JSONObject(response);

                    Toast.makeText(getBaseContext(),
                            "The image is upload", Toast.LENGTH_SHORT)
                            .show();

                } catch (Exception e) {
                    Log.d("JSON Exception", e.toString());
                    Toast.makeText(getBaseContext(),
                            "Error while loadin data!"+e.toString(),
                            Toast.LENGTH_LONG).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "Error [" + error + "]");
                Toast.makeText(getBaseContext(),
                        "Cannot connect to server"+error, Toast.LENGTH_LONG)
                        .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("category", tvCategory.getText().toString());
                params.put("section", tvGroup.getText().toString());
                params.put("name", edtName.getText().toString());
                params.put("memo", edtMemo.getText().toString());
                params.put("image", encodedString);
                params.put("purchaseDate", tvPurchaseDate.getText().toString());
                params.put("expirationDate",tvExpirationDate.getText().toString());
                params.put("code", ShowFoodsFragment.selectedFridge.getCode());  // 선택된 냉장고 코드 가져오기
                params.put("place", rdoClick);
                int requestCode = makeAlarmId();
                params.put("alarmID", String.valueOf(requestCode));// 등록 시간 - 사용자 아이디 = 알람 리퀘스트코드로 사용

                // 만들어진 알람 아이디로 알람 세팅
                notificationSetting(requestCode);

                return params;

            }

        };
        rq.add(stringRequest);
    }

    private int makeAlarmId() {
        Calendar calendar = Calendar.getInstance();
        String date;
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmSS", Locale.getDefault());
        date = format.format(calendar.getTime());
        int alarmId = Integer.parseInt(date) - Integer.parseInt(MainActivity.strId);
        Log.d("alarmId", alarmId+"");

        return alarmId;
    }

    //==============================================================================================//

    // 인텐트로 category, section, code 받기

    // 알람 연결
    private void notificationSetting(int requestCode) {

        //=========================================================================================//

        // 인텐트로 셋팅값 가져오기
        Intent settingIntent = getIntent();
        dateSetting = settingIntent.getIntExtra("dateSetting", dateSetting);
        switchSetting = settingIntent.getBooleanExtra("switchSetting", switchSetting);

        // 설정탭에서 인텐트를통해 여기로 체크값을 보냄 (1일전 = 1, 3일전 = 3, 7일전 = 7)
        // 여기서 인텐트를통해 리시버로 1일전, 3일전, 5일전의 날짜를 보냄
        // 리시버에서 스위치값을 감지해서 if else 문으로 서비스로 전달

        // 1일전에 체크되었을때 소비만료 일자에서 1일을 빼줌
        if (dateSetting == 1) {
            millis -= 86400000;

            // 3일전에 체크되었을때 소비만료 일자에서 3일을 빼줌
        } else if (dateSetting == 3) {
            millis -= 86400000 * 3;

            // 7일전에 체크되었을때 소비만료 일자에서 7일을 빼줌
        } else if (dateSetting == 7) {
            millis -= 86400000 * 7;
        }

        // 캘린더에 셋팅함 (시간은 오후 4시 0분 0초)
        calendar.set(Calendar.YEAR, getYearEx);
        calendar.set(Calendar.MONTH, getMonthEx);
        calendar.set(Calendar.DAY_OF_MONTH, getDayEx);
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // 구입일자가 소비만료일자보다 이상이면 안됨

        // 미완성!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //=======================================================================================//4

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("foodName", edtName.getText().toString());
        intent.putExtra("content", notiContent);
        intent.putExtra("id", requestCode);
        intent.putExtra("switch", switchSetting);
        PendingIntent pender = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pender);
    }

    //==============================================================================================//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_FROM_CAMERA) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

                ExifInterface exifInterface = null;
                // 속성을 체크해야된다.
                try {
                    exifInterface = new ExifInterface(tempFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int exifOrientation; // 방향 설정값 저장 변수
                int exifDegree; // Degree 설정값 저장 변수
                if (exifInterface != null) {
                    exifOrientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
                    exifDegree = exifOrientationToDegree(exifOrientation);
                } else {
                    exifDegree = 0;
                }

                bitmapTemp = rotate(originalBm, exifDegree);

                foodImage.setImageBitmap(bitmapTemp);
            }

        } else {
            Toast.makeText(context, "촬영이 취소되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    // 각도를 조절해서 다시 만든 비트맵
    private Bitmap rotate(Bitmap bitmap, int exifDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(exifDegree);
        Bitmap tempBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
        return tempBitmap;
    }

    private int exifOrientationToDegree(int exifOrientation) {

        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
        }
        return 0;
    }

    private File createImageFile() throws IOException {

        // 이미지 파일 이름 ( profileImage_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "FoodImage_" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/FoodImage/");

        // 프로필 이미지 바꿀때 안에 있는 파일 지워줌. 용량 차지하지 못하게 한다.
        if (storageDir.exists()) {
            File[] fileList = storageDir.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList.length == 0) {
                    break;
                }
                fileList[i].delete();
                Toast.makeText(context, "사진을 촬영합니다.", Toast.LENGTH_SHORT).show();
            }
            //storageDir.delete();
        } else if (!storageDir.exists()) {
            storageDir.mkdirs();
            Toast.makeText(context, "디렉토리 생성", Toast.LENGTH_SHORT).show();
        }

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }

    //=================================================================================================//

    // 라디오버튼 체크 인식
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (rdoGroup.getCheckedRadioButtonId() == R.id.rdoFridge) {
            rdoClick = "냉장";
        } else if (rdoGroup.getCheckedRadioButtonId() == R.id.rdoFreezer) {
            rdoClick = "냉동";
        } else if (rdoGroup.getCheckedRadioButtonId() == R.id.rdoPantry) {
            rdoClick = "실온";
        }
    }
}
