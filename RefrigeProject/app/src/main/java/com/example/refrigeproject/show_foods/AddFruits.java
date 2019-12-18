package com.example.refrigeproject.show_foods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.refrigeproject.R;

import java.util.ArrayList;

public class AddFruits extends Fragment {
    private View view;
    private GridView gridView;
    public static ArrayList<AddFoodGridViewData> fruitList = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;
    public static Integer[] fruitID = new Integer[]{R.drawable.fruit_apple, R.drawable.fruit_banana, R.drawable.fruit_blueberries, R.drawable.fruit_cherries,
            R.drawable.fruit_grapes, R.drawable.fruit_kiwi, R.drawable.fruit_lemon, R.drawable.fruit_melon, R.drawable.fruit_orange,
            R.drawable.fruit_peach, R.drawable.fruit_pear, R.drawable.fruit_pineapple, R.drawable.fruit_plum, R.drawable.fruit_tomato, R.drawable.fruit_watermelon};
    public static String[] fruitName =new String[]{"사과", "바나나", "블루베리", "체리", "포도", "키위", "레몬", "멜론", "오렌지", "복숭아", "배", "파인애플", "자두", "토마토", "수박"};


    public static AddFruits newInstance() {
        AddFruits addFruits = new AddFruits(); //싱글톤과 같은 기능
        return addFruits;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_food, container, false);
        gridView = view.findViewById(R.id.gridView);


        gridViewAdapter = new GridViewAdapter(getContext(), R.layout.add_food_item, fruitList);
        gridView.setAdapter(gridViewAdapter);

        listInsertFoodData();

        return view;
    }

    private void listInsertFoodData() {
        fruitList.clear();

        for (int i = 0; i < 15; i++) {
            fruitList.add(new AddFoodGridViewData(getResources().getString(R.string.fruit), fruitName[i], fruitID[i]));

        }

    }

}

