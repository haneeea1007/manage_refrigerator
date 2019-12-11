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

public class AddSeasoning extends Fragment {
    private View view;
    private GridView gridView;
    private ArrayList<AddFoodGridViewData> seasoningList = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;
    static AddSeasoning addSeasoning;

    public static AddSeasoning newInstance() {
        if(addSeasoning ==null) {
            addSeasoning = new AddSeasoning(); //싱글톤과 같은 기능
        }
        return addSeasoning;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_food, container, false);
        gridView = view.findViewById(R.id.gridView);


        gridViewAdapter = new GridViewAdapter(getContext(), R.layout.add_food_item, seasoningList);
        gridView.setAdapter(gridViewAdapter);

        listInsertFoodData();

        return view;
    }

    private void listInsertFoodData() {
        Integer[] seasoningID = {R.drawable.seasoning_flour, R.drawable.seasoning_salt, R.drawable.seasoning_pepper,R.drawable.seasoning_sugar};

        String[] seasoningName = {"밀가루", "소금", "후추", "설탕"};
        for (int i = 0; i < 3; i++) {
            seasoningList.add(new AddFoodGridViewData(seasoningID[i], seasoningName[i]));
        }


    }


}