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

public class AddDairyProduct extends Fragment {
    private View view;
    private GridView gridView;
    private ArrayList<AddFoodGridViewData> dairyList = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;

    public static AddDairyProduct newInstance() {
        AddDairyProduct addDairyProduct = new AddDairyProduct(); //싱글톤과 같은 기능
        return addDairyProduct;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_food, container, false);
        gridView = view.findViewById(R.id.gridView);


        gridViewAdapter = new GridViewAdapter(getContext(), R.layout.add_food_item, dairyList);
        gridView.setAdapter(gridViewAdapter);

        listInsertFoodData();

        return view;
    }

    private void listInsertFoodData() {
        dairyList.clear();
        Integer[] dairyID = {R.drawable.dairy_butter, R.drawable.dairy_cream, R.drawable.dairy_milk,R.drawable.dairy_whipping, R.drawable.dairy_yogurt};

        String[] dairyName = {"버터", "생크림", "우유", "휘핑크림", "새우", "요거트"};

        for (int i = 0; i < 5; i++) {
            dairyList.add(new AddFoodGridViewData(dairyID[i], dairyName[i]));
        }

    }


}

