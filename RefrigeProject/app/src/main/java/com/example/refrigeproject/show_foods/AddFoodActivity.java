package com.example.refrigeproject.show_foods;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.refrigeproject.R;
import com.google.android.material.tabs.TabLayout;
import com.r0adkll.slidr.Slidr;

public class AddFoodActivity extends AppCompatActivity {

    private FragmentPagerAdapter fragmentPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        fragmentPagerAdapter = new AddFoodViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Slidr.attach(this);
    }

    private class AddFoodViewPagerAdapter extends FragmentPagerAdapter {
        public AddFoodViewPagerAdapter(FragmentManager FragmentManager) {
            super(FragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return AddFoodEachCategory.newInstance();
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
