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
    public static ArrayList<AddFoodGridViewData> seasoningList = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;
   public static Integer[] seasoningID = new Integer[]{R.drawable.seasoning_flour, R.drawable.seasoning_salt, R.drawable.seasoning_pepper,R.drawable.seasoning_sugar,
            R.drawable.seasoning_buchim, R.drawable.seasoning_currypowder,R.drawable.seaoning_powder};
    public static String[] seasoningName = new String[]{"밀가루", "소금", "후추", "설탕", "부침가루", "카레가루", "조미료_직접입력"};

    public static AddSeasoning newInstance() {
        AddSeasoning addSeasoning = new AddSeasoning(); //싱글톤과 같은 기능
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
        seasoningList.clear();


        for (int i = 0; i < 7; i++) {
            seasoningList.add(new AddFoodGridViewData(getResources().getString(R.string.seasoning), seasoningName[i], seasoningID[i]));
        }


    }


}