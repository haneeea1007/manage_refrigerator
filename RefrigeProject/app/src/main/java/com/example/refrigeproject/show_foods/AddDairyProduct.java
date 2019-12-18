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
    public static ArrayList<AddFoodGridViewData> dairyList = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;
    public static Integer[] dairyID = new Integer[]{R.drawable.dairy_butter, R.drawable.dairy_cream, R.drawable.dairy_milk, R.drawable.dairy_whipping,
            R.drawable.dairy_yogurt};
    public  static String[] dairyName = new String[]{"버터", "생크림", "우유", "휘핑크림", "요거트"};

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


        for (int i = 0; i < 5; i++) {
            dairyList.add(new AddFoodGridViewData(getResources().getString(R.string.dairy_product), dairyName[i], dairyID[i]));
        }

    }


}

