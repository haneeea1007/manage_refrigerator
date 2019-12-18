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

public class AddSeafood extends Fragment {
    private View view;
    private GridView gridView;
    public static ArrayList<AddFoodGridViewData> seafoodList = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;
    public  static Integer[] seafoodID = new Integer[]{R.drawable.seafood_seaweed, R.drawable.seafood_anchovy, R.drawable.seafood_crab, R.drawable.seafood_salmon,
            R.drawable.seafood_shellfish, R.drawable.seafood_shrimp, R.drawable.seafood_squid, R.drawable.seafood_fish};
    public  static String[] seafoodName = new String[]{"미역", "멸치", "게", "회", "조개", "새우", "오징어", "생선_직접입력"};

    public static AddSeafood newInstance() {
        AddSeafood addSeafood = new AddSeafood(); //싱글톤과 같은 기능
        return addSeafood;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_food, container, false);
        gridView = view.findViewById(R.id.gridView);


        gridViewAdapter = new GridViewAdapter(getContext(), R.layout.add_food_item, seafoodList);
        gridView.setAdapter(gridViewAdapter);

        listInsertFoodData();

        return view;
    }

    private void listInsertFoodData() {
        seafoodList.clear();


        for (int i = 0; i < 8; i++) {
            seafoodList.add(new AddFoodGridViewData(getResources().getString(R.string.seafood), seafoodName[i], seafoodID[i]));
        }

    }


}

