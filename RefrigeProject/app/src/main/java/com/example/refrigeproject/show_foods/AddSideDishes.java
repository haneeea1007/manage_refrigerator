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

public class AddSideDishes extends Fragment {
    private View view;
    private GridView gridView;
    public static ArrayList<AddFoodGridViewData> sideList = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;
    public static Integer[] sideID = new Integer[]{R.drawable.side_eggroll, R.drawable.side_tufu, R.drawable.side_rice, R.drawable.side_curry,
            R.drawable.side_pickles, R.drawable.side_pot, R.drawable.side_stew, R.drawable.side_food};
    public static String[] sideName = new String[]{"달걀말이", "두부", "밥", "카레", "피클", "찌개", "국", "반찬_직접입력"};


    public static AddSideDishes newInstance() {
        AddSideDishes addSideDishes = new AddSideDishes(); //싱글톤과 같은 기능
        return addSideDishes;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_food, container, false);
        gridView = view.findViewById(R.id.gridView);


        gridViewAdapter = new GridViewAdapter(getContext(), R.layout.add_food_item, sideList);
        gridView.setAdapter(gridViewAdapter);

        listInsertFoodData();

        return view;
    }

    private void listInsertFoodData() {
        sideList.clear();


        for (int i = 0; i < 8; i++) {
            sideList.add(new AddFoodGridViewData(getResources().getString(R.string.side_dish), sideName[i], sideID[i]));

        }

    }

}
