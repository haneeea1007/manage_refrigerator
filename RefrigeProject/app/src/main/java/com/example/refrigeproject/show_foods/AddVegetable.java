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

public class AddVegetable extends Fragment {
    private View view;
    private GridView gridView;
    private ArrayList<AddFoodGridViewData> vegeList = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;

    public static AddVegetable newInstance() {
        AddVegetable addVegetable = new AddVegetable(); //싱글톤과 같은 기능
        return addVegetable;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_food, container, false);
        gridView = view.findViewById(R.id.gridView);


        gridViewAdapter = new GridViewAdapter(getContext(), R.layout.add_food_item, vegeList);
        gridView.setAdapter(gridViewAdapter);

        listInsertFoodData();

        return view;
    }

    private void listInsertFoodData() {
        vegeList.clear();
        Integer[] vegeID = {R.drawable.vege_cucumber, R.drawable.vege_broccoli, R.drawable.vege_carrot, R.drawable.vege_chili, R.drawable.vege_corn,
                R.drawable.vege_eggplant, R.drawable.vege_garlic, R.drawable.vege_radish, R.drawable.vege_onion};

        String[] vegeName = {"오이", "브로콜리", "당근", "고추", "옥수수", "가지", "마늘", "무", "양파"};

        for (int i = 0; i < 8; i++) {
            vegeList.add(new AddFoodGridViewData(vegeID[i], vegeName[i]));
        }

    }


}
