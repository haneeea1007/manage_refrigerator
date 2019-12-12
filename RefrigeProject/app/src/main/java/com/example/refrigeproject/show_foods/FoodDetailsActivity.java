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
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.refrigeproject.DBHelper;
import com.example.refrigeproject.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FoodDetailsActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Context context;

    private ImageButton ibtBack;
    private TextView tvDone, tvCategory, tvGroup, tvTitle, tvPurchaseDate, tvExpirationDate, tvRefrige;
    private EditText edtName, edtMemo;
    private RadioGroup rdoGroup;
    private RadioButton rdoRef, rdoFreeze, rdoStored;
    private DatePicker datePicker;
    private ImageView foodImage;
    private String foodName;

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

    // 리퀘스트코드
    private static final int PICK_FROM_CAMERA = 1;

    // 어디서 불렀는지 리퀘스트 코드를 넣고 INSERT 가 필요한지 UPDATE 가 필요한지 구분하기
    // 추가할때는 1로 인텐트, 수정할때는 2로 인텐트!!!!!
    private static final int FROM_ADD_OR_EDIT = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        context = getApplicationContext();
        dBHelper = new DBHelper(context);
        calendar = Calendar.getInstance();
        calendarTemp = Calendar.getInstance();

        ibtBack = findViewById(R.id.ibtBack);
        tvDone = findViewById(R.id.tvDone);
        tvCategory = findViewById(R.id.tvCategory);
        tvGroup = findViewById(R.id.tvGroup);
        edtName = findViewById(R.id.edtName);
        datePicker = findViewById(R.id.datePicker);
        foodImage = findViewById(R.id.foodImage);

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
        rdoRef = findViewById(R.id.rdoRef);
        rdoFreeze = findViewById(R.id.rdoFreeze);
        rdoStored = findViewById(R.id.rdoStored);
        datePicker = findViewById(R.id.datePicker);
        foodImage = findViewById(R.id.foodImage);

        tvDone.setOnClickListener(this);
        ibtBack.setOnClickListener(this);
        tvPurchaseDate.setOnClickListener(this);
        tvExpirationDate.setOnClickListener(this);
        tvRefrige.setOnClickListener(this);
        foodImage.setOnClickListener(this);
        rdoGroup.setOnCheckedChangeListener(this);

        Intent intent = getIntent();
        foodName = intent.getStringExtra("foodName");
        edtName.setText(foodName);

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
    }

    private void setData() {
        Intent intent = getIntent();
        String from = intent.getStringExtra("from");
        Toast.makeText(getApplicationContext(), from, Toast.LENGTH_SHORT).show();
        switch (from) {
            case "GridViewAdapter":
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

                // 알람연결
                // DB 내용 수정
                NotificationSetting();

                Toast.makeText(context, foodName + " 추가되었습니다.", Toast.LENGTH_SHORT).show();

                // 소비만료 날짜 확인
//                Toast.makeText(context, calendar.get(Calendar.YEAR) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
                finish();

                break;

            case R.id.ibtBack:
                // 뒤로가기
                finish();
                break;

            case R.id.tvPurchaseDate:
                // 구입일자
                // DB 내용 수정

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
                // DB 내용 수정

                DatePickerDialog dialog2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        // 알림 날짜 설정, 0시 0분
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DATE, dayOfMonth);
                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0);

                        // 현재 날짜, 0시 0분
                        Calendar today = Calendar.getInstance();
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

                // 선택한 값 받아서 디비에 넣기

                break;

            case R.id.foodImage:
                // 이미지 넣기
                // DB 내용 수정

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

    //==============================================================================================//

    // 인텐트로 category, section, code 받기

    // 알람 연결
    private void NotificationSetting() {

        // 소비만료 일자가 구입일자보다 이전일 때
        if (millis <= millisTemp) {
            Toast.makeText(context, "소비만료 일자가 구입일자보다 이전일 수 없습니다." + '\n' + "다시 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 추가할 때 DB문 (INSERT)
        if (FROM_ADD_OR_EDIT == 1) {

//        sqLiteDatabase = dBHelper.getWritableDatabase();
//
//        String str = "INSERT INTO foodTBL values('" +
//                null + "', '" +
//                category + "', '" +
//                section + "', '" +
//                foodName + "', '" +
//                tempFile.getPath() + "', '" +
//                edtMemo.getText().toString() + "', '" +
//                tvPurchaseDate.getText().toString() + "', '" +
//                tvExpirationDate.getText().toString() + "', '" +
//                code + "', '" +
//                rdoClick + "');";
//
//        sqLiteDatabase.execSQL(str);
//        sqLiteDatabase.close();

            // 수정할 때 DB문 (UPDATE)
        } else if (FROM_ADD_OR_EDIT == 2) {

//            sqLiteDatabase = dBHelper.getWritableDatabase();
//
//
//
//
//            sqLiteDatabase.execSQL(str);
//            sqLiteDatabase.close();

        }

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
        intent.putExtra("foodName", foodName);
        intent.putExtra("content", notiContent);
        intent.putExtra("id", alarmID);
        intent.putExtra("switch", switchSetting);
        PendingIntent pender = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pender);
    }

    //==============================================================================================//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

            Bitmap bitmapTemp = rotate(originalBm, exifDegree);

            foodImage.setImageBitmap(bitmapTemp);
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

        if (rdoGroup.getCheckedRadioButtonId() == R.id.rdoRef) {
            rdoClick = "냉장";
        } else if (rdoGroup.getCheckedRadioButtonId() == R.id.rdoFreeze) {
            rdoClick = "냉동";
        } else if (rdoGroup.getCheckedRadioButtonId() == R.id.rdoStored) {
            rdoClick = "실온";
        }
    }
}
