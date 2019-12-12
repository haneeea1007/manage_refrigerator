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
        Integer[] sauceID = {R.drawable.sauce_honey, R.drawable.sauce_jam, R.drawable.sauce_ketchup,R.drawable.sauce_mayonnaise, R.drawable.sauce_mustard, R.drawable.sauce_jar};

        for (int i = 0; i < 5; i++) {
            sauceList.add(new AddFoodGridViewData(sauceID[i], AddFoodActivity.sauceName[i]));
        }

    }


}