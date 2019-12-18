package com.example.refrigeproject.show_foods;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.refrigeproject.MainActivity;
import com.example.refrigeproject.R;
import com.example.refrigeproject.checklist.CheckListDBHelper;
import com.google.android.material.tabs.TabLayout;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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






    public static int category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);


        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        addFoodDBHelper = new AddFoodDBHelper(getApplicationContext());
        sqLiteDatabase1 = addFoodDBHelper.getWritableDatabase();



        ibtBack = findViewById(R.id.ibtBack);
        ibtSearchToAddFood = findViewById(R.id.ibtSearchToAddFood);
        edtSearchFood = findViewById(R.id.edtSearchFood);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                category = tabLayout.getSelectedTabPosition();
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


        for (int i = 0; i < AddVegetable.vegeList.size(); i++) {
            String str = "INSERT INTO AddFoodTBL values('야채' , '" + AddVegetable.vegeName[i] + "', '" + AddVegetable.vegeID[i] +"');";
            sqLiteDatabase1.execSQL(str);
        }

        for (int i = 0; i < AddFruits.fruitList.size(); i++) {
            String str = "INSERT INTO AddFoodTBL values('과일' , '" + AddFruits.fruitName[i] + "', '" + AddFruits.fruitID[i] +"');";
            sqLiteDatabase1.execSQL(str);
        }
        for (int i = 0; i < AddMeat.meatList.size(); i++) {
            String str = "INSERT INTO AddFoodTBL values('고기' , '" + AddMeat.meatName[i] + "', '" + AddMeat.meatID[i] +"');";
            sqLiteDatabase1.execSQL(str);
        }
        for (int i = 0; i < AddSeafood.seafoodList.size(); i++) {
            String str = "INSERT INTO AddFoodTBL values('해산물' , '" + AddSeafood.seafoodName[i] + "', '" + AddSeafood.seafoodID[i] +"');";
            sqLiteDatabase1.execSQL(str);
        }
        for (int i = 0; i < AddDairyProduct.dairyList.size(); i++) {
            String str = "INSERT INTO AddFoodTBL values('유제품' , '" + AddDairyProduct.dairyName[i] + "', '" + AddDairyProduct.dairyID[i] +"');";
            sqLiteDatabase1.execSQL(str);
        }

        for (int i = 0; i < AddSideDishes.sideList.size(); i++) {
            String str = "INSERT INTO AddFoodTBL values('반찬' , '" + AddSideDishes.sideName[i] + "', '" + AddSideDishes.sideID[i] +"');";
            sqLiteDatabase1.execSQL(str);
        }
        for (int i = 0; i < AddInstant.instantList.size(); i++) {
            String str = "INSERT INTO AddFoodTBL values('인스턴트' , '" + AddInstant.instantName[i] + "', '" + AddInstant.instantID[i] +"');";
            sqLiteDatabase1.execSQL(str);
        }
        for (int i = 0; i < AddDrinks.drinksList.size(); i++) {
            String str = "INSERT INTO AddFoodTBL values('음료' , '" + AddDrinks.drinkName[i] + "', '" + AddDrinks.drinkID[i] +"');";
            sqLiteDatabase1.execSQL(str);
        }
        for (int i = 0; i < AddSauce.sauceList.size(); i++) {
            String str = "INSERT INTO AddFoodTBL values('소스' , '" + AddSauce.sauceName[i] + "', '" + AddSauce.sauceID[i] +"');";
            sqLiteDatabase1.execSQL(str);
        }
        for (int i = 0; i < AddSeasoning.seasoningList.size(); i++) {
            String str = "INSERT INTO AddFoodTBL values('조미료' , '" + AddSeasoning.seasoningName[i] + "', '" + AddSeasoning.seasoningID[i] +"');";
            sqLiteDatabase1.execSQL(str);
        }
        sqLiteDatabase1.close();

        sqLiteDatabase1 = addFoodDBHelper.getReadableDatabase();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtBack:
                finish();
                break;

            case R.id.ibtSearchToAddFood:


            case R.id.edtSearchFood:
                edtSearchFood.setText(null);
                break;
        }
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

