package com.example.refrigeproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.refrigeproject.calendar.CalendarFragment;
import com.example.refrigeproject.checklist.CheckListFragment;
import com.example.refrigeproject.search_recipe.SearchRecipeFragment;
import com.example.refrigeproject.setting.SettingFragment;
import com.example.refrigeproject.show_foods.ShowFoodsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements ShowFoodsFragment.OnFragmentInteractionListener{
    private BottomNavigationView bottomMenu;
    private FrameLayout frameLayout;
    private Fragment calendar, searchRecipe, showFoods, checkList, setting;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Bundle bundle;

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

        calendar = new CalendarFragment();
        searchRecipe = new SearchRecipeFragment();
        showFoods = new ShowFoodsFragment();
        checkList = new CheckListFragment();
        setting = new SettingFragment();

    }

    private void changeFragment(int position) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (position){
            case 1:
                fragmentTransaction.replace(R.id.frameLayout, calendar); break;
            case 2:
                fragmentTransaction.replace(R.id.frameLayout, searchRecipe);
                searchRecipe.setArguments(bundle);
                break;
            case 3:
                fragmentTransaction.replace(R.id.frameLayout, new ShowFoodsFragment()); break;
            case 4:
                fragmentTransaction.replace(R.id.frameLayout, checkList); break;
            case 5:
                fragmentTransaction.replace(R.id.frameLayout, setting); break;
        }

        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Bundle bundle) {
        this.bundle = bundle;
        bottomMenu.setSelectedItemId(R.id.action_2);
    }
}
