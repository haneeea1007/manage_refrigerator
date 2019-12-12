package com.example.refrigeproject.show_foods;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.refrigeproject.R;
import com.google.android.material.tabs.TabLayout;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AddFoodActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentPagerAdapter fragmentPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageButton ibtBack, ibtSearchToAddFood;
    private EditText edtSearchFood;
    public static String searchFood;

    public static HashMap<Integer, String[]> list = new HashMap<Integer, String[]>();
    public static String[] vegeName = {"오이", "브로콜리", "당근", "고추", "옥수수", "가지", "마늘", "무", "양파"};
    public static String[] seafoodName = {"멸치", "게", "연어", "조개", "새우", "오징어"};
    public static String[] sideName = {"카레", "피클", "찌개", "국", "반찬_직접입력"};
    public static String[] instantName = {"만두", "치킨", "튀김", "라면", "소세지", "스팸"};
    public static String[] meatName = {"닭고기", "돼지고기", "소고기"};
    public static String[] drinkName = {"주류_직접입력", "음료_직접입력", "과일주스", "탄산음료", "소주", "물", "와인"};
    public static String[] sauceName = {"꿀", "딸기잼", "케첩", "마요네즈", "머스타드", "소스_직접입력"};
    public static String[] fruitName = {"사과", "바나나", "블루베리", "체리", "포도", "키위", "레몬", "멜론", "오렌지", "복숭아", "배", "파인애플", "자두", "토마토", "수박"};
    public static String[] dairyName = {"버터", "생크림", "우유", "휘핑크림", "새우", "요거트"};
    public static String[] seasoningName = {"밀가루", "소금", "후추", "설탕"};


    public static int category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

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

        setList();

        ibtBack.setOnClickListener(this);
        ibtSearchToAddFood.setOnClickListener(this);
    }

    private void setList() {
        AddFoodActivity.list.put(0, vegeName);
        AddFoodActivity.list.put(1, fruitName);
        AddFoodActivity.list.put(2, meatName);
        AddFoodActivity.list.put(3, seafoodName);
        AddFoodActivity.list.put(4, dairyName);
        AddFoodActivity.list.put(5, sideName);
        AddFoodActivity.list.put(6, instantName);
        AddFoodActivity.list.put(7, drinkName);
        AddFoodActivity.list.put(8, sauceName);
        AddFoodActivity.list.put(9, seasoningName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtBack:
                finish();
                break;
            case R.id.ibtSearchToAddFood:
                searchFood = edtSearchFood.getText().toString().trim();
                Toast.makeText(getApplicationContext(), list.toString(), Toast.LENGTH_SHORT).show();
                Log.d("TESTEST", list.toString());

                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    if (Arrays.asList(list.get(i)).contains(searchFood)) {

                        TabLayout.Tab tab = tabLayout.getTabAt(i);
                        tab.select();
                        break;
                    }
                }


                break;
        }
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
