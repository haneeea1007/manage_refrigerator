package com.example.refrigeproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomMenu;
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomMenu = findViewById(R.id.bottomMenu);
        frameLayout = findViewById(R.id.frameLayout);

        bottomMenu.setSelectedItemId(R.id.action_3);
        changeFragment(3);


        // bottomMenu를 변경을 때 Fragment
        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.action_1: changeFragment(1); break;
                    case R.id.action_2: changeFragment(2); break;
                    case R.id.action_3: changeFragment(3); break;
                    case R.id.action_4: changeFragment(4); break;
                    case R.id.action_5: changeFragment(5); break;
                }
                return true;
            }
        });

    }

    private void changeFragment(int position) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (position){
            case 1: fragmentTransaction.replace(R.id.frameLayout, new CalendarFragment()); break;
            case 2: fragmentTransaction.replace(R.id.frameLayout, new SearchRecipeFragment()); break;
            case 3: fragmentTransaction.replace(R.id.frameLayout, new ShowItemsFragment()); break;
            case 4: fragmentTransaction.replace(R.id.frameLayout, new Fragment1Activity()); break;
            case 5: fragmentTransaction.replace(R.id.frameLayout, new Fragment5Activity()); break;
        }

        fragmentTransaction.commit();
    }
}
