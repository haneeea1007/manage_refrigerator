package com.example.refrigeproject.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.refrigeproject.R;
import com.r0adkll.slidr.Slidr;

public class SeasonFoodDetails extends AppCompatActivity {
    TextView tvFoodName, tvCategory, tvRegion, tvEra, tvEffect, tvTip, tvURL, tvTrimming;
    SeasonalFood food;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season_food_details);

        Intent intent = getIntent();
        food = intent.getParcelableExtra("food");
        tvFoodName = findViewById(R.id.tvFoodName);
        tvCategory = findViewById(R.id.tvCategory);
        tvRegion = findViewById(R.id.tvRegion);
        tvEra = findViewById(R.id.tvEra);
        tvEffect = findViewById(R.id.tvEffect);
        tvTip = findViewById(R.id.tvTip);
        tvURL = findViewById(R.id.tvURL);
        tvTrimming = findViewById(R.id.tvTrimming);

        setData();

        Slidr.attach(this);

    }
    public void setData(){
        tvFoodName.setText(food.getFoodName());
        tvCategory.setText(food.getClassification());
        tvRegion.setText(food.getProductionRegion());
        tvEra.setText(food.getProductionEra());
        tvEffect.setText(food.getEffect());
        tvTip.setText(food.getCookTips());
        tvURL.setText(food.getDetailsUrl());
        tvTrimming.setText(food.getTrimmingTips());

    }
}
