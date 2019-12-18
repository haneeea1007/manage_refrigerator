package com.example.refrigeproject.show_foods;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.refrigeproject.R;
import com.example.refrigeproject.setting.ManageFridgeActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class AddFoodActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    private InputMethodManager imm;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageButton ibtBack, ibtSearchToAddFood;
    private EditText edtSearchFood;
    public static String searchFood;
    private boolean find = false;

    private AddFoodDBHelper addFoodDBHelper;
    private SQLiteDatabase sqLiteDatabase1;

    private ArrayList<AddFoodGridViewData> totalList = new ArrayList<AddFoodGridViewData>();
    private ArrayList<AddFoodGridViewData> searchList = new ArrayList<AddFoodGridViewData>();


//    public static int category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        addFoodDBHelper = new AddFoodDBHelper(getApplicationContext());

        ibtBack = findViewById(R.id.ibtBack);
        ibtSearchToAddFood = findViewById(R.id.ibtSearchToAddFood);
        edtSearchFood = findViewById(R.id.edtSearchFood);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                category = tabLayout.getSelectedTabPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fragmentPagerAdapter = new AddFoodViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        insertFoodData();

        ibtBack.setOnClickListener(this);
        ibtSearchToAddFood.setOnClickListener(this);

        edtSearchFood.setOnClickListener(this);
        edtSearchFood.setOnKeyListener(this);
    }

    private void insertFoodData() {
        // 테이블이 없을 때만 실행
        if(selectAllFoods() == null){
            sqLiteDatabase1 = addFoodDBHelper.getWritableDatabase();

            for (int i = 0; i < AddVegetable.vegeName.length; i++) {
                String str = "INSERT INTO foodTBL values('" + getResources().getString(R.string.vegetable)
                        + "' , '" + AddVegetable.vegeName[i] + "', '" + AddVegetable.vegeID[i] +"');";
                sqLiteDatabase1.execSQL(str);
            }

            for (int i = 0; i < AddFruits.fruitName.length; i++) {
                String str = "INSERT INTO foodTBL values('" + getResources().getString(R.string.fruit)
                        + "' , '" + AddFruits.fruitName[i] + "', '" + AddFruits.fruitID[i] +"');";
                Log.d("insertFoodData", "과일" + AddFruits.fruitName[i] + AddFruits.fruitID[i]);
                sqLiteDatabase1.execSQL(str);
            }

            for (int i = 0; i < AddMeat.meatName.length; i++) {
                String str = "INSERT INTO foodTBL values('"+ getResources().getString(R.string.meat)
                        + "' , '" + AddMeat.meatName[i] + "', '" + AddMeat.meatID[i] +"');";
                Log.d("insertFoodData", "고기" + AddMeat.meatName[i] + AddMeat.meatID[i]);
                sqLiteDatabase1.execSQL(str);
            }

            for (int i = 0; i < AddSeafood.seafoodName.length; i++) {
                String str = "INSERT INTO foodTBL values('"+ getResources().getString(R.string.seafood)
                        + "' , '" + AddSeafood.seafoodName[i] + "', '" + AddSeafood.seafoodID[i] +"');";
                Log.d("insertFoodData", "해산물" + AddSeafood.seafoodName[i] + AddSeafood.seafoodID[i]);
                sqLiteDatabase1.execSQL(str);
            }

            for (int i = 0; i < AddDairyProduct.dairyName.length; i++) {
                String str = "INSERT INTO foodTBL values('" + getResources().getString(R.string.dairy_product)
                        + "' , '" + AddDairyProduct.dairyName[i] + "', '" + AddDairyProduct.dairyID[i] +"');";
                sqLiteDatabase1.execSQL(str);
            }

            for (int i = 0; i < AddSideDishes.sideName.length ; i++) {
                String str = "INSERT INTO foodTBL values('" + getResources().getString(R.string.side_dish)
                        + "' , '" + AddSideDishes.sideName[i] + "', '" + AddSideDishes.sideID[i] +"');";
                sqLiteDatabase1.execSQL(str);
            }

            for (int i = 0; i < AddInstant.instantList.size(); i++) {
                String str = "INSERT INTO foodTBL values('" + getResources().getString(R.string.instant)
                        + "' , '" + AddInstant.instantName[i] + "', '" + AddInstant.instantID[i] +"');";
                sqLiteDatabase1.execSQL(str);
            }

            for (int i = 0; i < AddDrinks.drinkName.length; i++) {
                String str = "INSERT INTO foodTBL values('" + getResources().getString(R.string.beverage)
                        + "' , '" + AddDrinks.drinkName[i] + "', '" + AddDrinks.drinkID[i] +"');";
                sqLiteDatabase1.execSQL(str);
            }

            for (int i = 0; i < AddSauce.sauceName.length; i++) {
                String str = "INSERT INTO foodTBL values('" + getResources().getString(R.string.sauce)
                        + "' , '" + AddSauce.sauceName[i] + "', '" + AddSauce.sauceID[i] +"');";
                sqLiteDatabase1.execSQL(str);
            }

            for (int i = 0; i < AddSeasoning.seasoningName.length; i++) {
                String str = "INSERT INTO foodTBL values('" + getResources().getString(R.string.seasoning)
                        + "' , '" + AddSeasoning.seasoningName[i] + "', '" + AddSeasoning.seasoningID[i] +"');";
                sqLiteDatabase1.execSQL(str);
            }

            sqLiteDatabase1.close();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtBack:
                finish();
                break;

            case R.id.ibtSearchToAddFood:
                totalList = selectAllFoods();
                filter(edtSearchFood.getText().toString());
                Intent intent = new Intent(AddFoodActivity.this, SearchFoodActivity.class);
                intent.putExtra("searchList", searchList);
                startActivity(intent);



            case R.id.edtSearchFood:
                edtSearchFood.setText(null);
                break;
        }
    }

    private void filter(String keyword) {
        searchList.clear();

        if (keyword.length() == 0) {
            ManageFridgeActivity.simpleCookieBar("검색어를 입력해주세요", AddFoodActivity.this);
            return;
        } else {
            for (AddFoodGridViewData food : totalList) {
                String name = food.getSection();
                if (name.toLowerCase().contains(keyword)) {
                    searchList.add(food);
                }
            }
        }
    }

    private ArrayList<AddFoodGridViewData> selectAllFoods() {
        ArrayList<AddFoodGridViewData> tempList = new ArrayList<AddFoodGridViewData>();

        sqLiteDatabase1 = addFoodDBHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqLiteDatabase1.rawQuery("SELECT * FROM foodTBL;", null);
        while(cursor.moveToNext()){
            AddFoodGridViewData food = new AddFoodGridViewData(cursor.getString(0), cursor.getString(1), cursor.getInt(2) );
            tempList.add(food);
        }

        cursor.close();
        sqLiteDatabase1.close();

        return tempList;
    }


    private void hideKeyboard() {
        imm.hideSoftInputFromWindow(edtSearchFood.getWindowToken(), 0);

    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            ibtSearchToAddFood.callOnClick();
        }
        return true;
    }

    private class AddFoodViewPagerAdapter extends FragmentPagerAdapter {
        public AddFoodViewPagerAdapter(FragmentManager FragmentManager) {
            super(FragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AddVegetable.newInstance();
                case 1:
                    return AddFruits.newInstance();
                case 2:
                    return AddMeat.newInstance();
                case 3:
                    return AddSeafood.newInstance();
                case 4:
                    return AddDairyProduct.newInstance();
                case 5:
                    return AddSideDishes.newInstance();
                case 6:
                    return AddInstant.newInstance();
                case 7:
                    return AddDrinks.newInstance();
                case 8:
                    return AddSauce.newInstance();
                case 9:
                    return AddSeasoning.newInstance();
                default:
                    return null;
            }
        }


        @Override
        public int getCount() {
            return 10;
        }

        @Nullable
        @Override //상단의 탭 레이아웃 제목에 대한 텍스트를 선언하는 역할
        public CharSequence getPageTitle(int position) {
            switch (position) {
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


                default:
                    return null;
            }
        }
    }
}

