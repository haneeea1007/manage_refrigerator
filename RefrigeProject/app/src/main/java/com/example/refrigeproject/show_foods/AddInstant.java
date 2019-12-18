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

public class AddInstant extends Fragment {
    private View view;
    private GridView gridView;
    public static ArrayList<AddFoodGridViewData> instantList = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;
   public static Integer[] instantID = new Integer[]{R.drawable.instant_pizza,R.drawable.instant_burger,R.drawable.instant_dimsum, R.drawable.instant_friedchicken,
            R.drawable.instant_fries,R.drawable.instant_noodles, R.drawable.instant_sausage, R.drawable.instant_spam};
    public  static String[] instantName = new String[]{"피자", "햄버거", "만두", "치킨", "튀김", "라면", "소세지", "스팸"};

    public static AddInstant newInstance() {
        AddInstant addInstant = new AddInstant(); //싱글톤과 같은 기능
        return addInstant;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_food, container, false);
        gridView = view.findViewById(R.id.gridView);


        gridViewAdapter = new GridViewAdapter(getContext(), R.layout.add_food_item, instantList);
        gridView.setAdapter(gridViewAdapter);

        listInsertFoodData();

        return view;
    }

    private void listInsertFoodData() {
        instantList.clear();


        for (int i = 0; i < 8; i++) {
            instantList.add(new AddFoodGridViewData(getResources().getString(R.string.instant), instantName[i], instantID[i]));
        }

    }


}

