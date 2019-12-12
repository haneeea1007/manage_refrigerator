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

public class AddMeat extends Fragment {
    private View view;
    private GridView gridView;
    private ArrayList<AddFoodGridViewData> meatList = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;

    public static AddMeat newInstance() {
        AddMeat addMeat = new AddMeat(); //싱글톤과 같은 기능
        return addMeat;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_food, container, false);
        gridView = view.findViewById(R.id.gridView);


        gridViewAdapter = new GridViewAdapter(getContext(), R.layout.add_food_item, meatList);
        gridView.setAdapter(gridViewAdapter);

        listInsertFoodData();

        return view;
    }

    private void listInsertFoodData() {
        meatList.clear();
        Integer[] meatID = {R.drawable.meat_chicken, R.drawable.meat_pork, R.drawable.meat_beef};

        String[] meatName = {"닭고기", "돼지고기", "소고기"};

        for (int i = 0; i < 2; i++) {
            meatList.add(new AddFoodGridViewData(meatID[i], meatName[i]));
        }

    }


}

