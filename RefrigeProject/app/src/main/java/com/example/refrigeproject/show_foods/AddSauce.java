package com.example.refrigeproject.show_foods;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.refrigeproject.R;

import java.util.ArrayList;

public class AddSauce extends Fragment {
    private View view;
    private GridView gridView;
    public static ArrayList<AddFoodGridViewData> sauceList = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;
    public  static Integer[] sauceID = new Integer[]{R.drawable.sauce_dressing, R.drawable.sauce_mix, R.drawable.sauce_soybean,R.drawable.sauce_chilli,
            R.drawable.sauce_soysauce,R.drawable.sauce_vinegar,R.drawable.sauce_porkcutlet,R.drawable.sauce_honey, R.drawable.sauce_jam,
            R.drawable.sauce_ketchup,R.drawable.sauce_mayonnaise, R.drawable.sauce_mustard, R.drawable.sauce_jar};
    public  static String[] sauceName = new String[]{"드레싱", "쌈장", "된장", "고추장", "간장", "식초", "돈까스소스", "꿀", "딸기잼", "케첩", "마요네즈", "머스타드", "소스_직접입력"};

    public static AddSauce newInstance() {
        AddSauce addSauce = new AddSauce(); //싱글톤과 같은 기능
        return addSauce;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_food, container, false);
        gridView = view.findViewById(R.id.gridView);

        gridViewAdapter = new GridViewAdapter(getContext(), R.layout.add_food_item, sauceList);
        gridView.setAdapter(gridViewAdapter);

        listInsertFoodData();

        return view;
    }

    private void listInsertFoodData() {
        sauceList.clear();

        for (int i = 0; i < 13; i++) {
            sauceList.add(new AddFoodGridViewData(getResources().getString(R.string.sauce), sauceName[i], sauceID[i]));
        }
    }
}