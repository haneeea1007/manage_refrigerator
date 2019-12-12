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

public class AddDrinks extends Fragment {
    private View view;
    private GridView gridView;
    public static ArrayList<AddFoodGridViewData> drinksList = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;

    public static AddDrinks newInstance() {
        AddDrinks addDrinks = new AddDrinks(); //싱글톤과 같은 기능
        return addDrinks;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_food, container, false);
        gridView = view.findViewById(R.id.gridView);


        gridViewAdapter = new GridViewAdapter(getContext(), R.layout.add_food_item, drinksList);
        gridView.setAdapter(gridViewAdapter);

        listInsertFoodData();

        return view;
    }

    private void listInsertFoodData() {
        drinksList.clear();
        Integer[] drinkID = {R.drawable.drink_alcohol, R.drawable.drink_beverage, R.drawable.drink_juice,R.drawable.drink_softdrink,
                R.drawable.drink_soju, R.drawable.drink_water,R.drawable.drink_wine};

        for (int i = 0; i < 7; i++) {
            drinksList.add(new AddFoodGridViewData(drinkID[i], AddFoodActivity.drinkName[i]));
        }

    }


}

