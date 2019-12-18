package com.example.refrigeproject.show_foods;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.refrigeproject.R;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;

public class SearchFoodActivity extends AppCompatActivity {
    private GridView gridView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_food);
        Slidr.attach(this);

        gridView = findViewById(R.id.gridView);

        // 검색결과가 저장된 리스트 받기
        Intent intent = getIntent();
        ArrayList<AddFoodGridViewData> searchList = intent.getParcelableArrayListExtra("searchList");

        // 어댑터에 전달
        GridViewAdapter gridViewAdapter = new GridViewAdapter(SearchFoodActivity.this, R.layout.fragment_add_food, searchList);
        gridView.setAdapter(gridViewAdapter);
    }
}
