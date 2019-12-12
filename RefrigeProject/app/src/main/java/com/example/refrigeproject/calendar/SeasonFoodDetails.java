package com.example.refrigeproject.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.refrigeproject.R;
import com.r0adkll.slidr.Slidr;

public class SeasonFoodDetails extends AppCompatActivity implements View.OnClickListener {
    LinearLayout url;
    TextView tvFoodName, tvCategory, tvRegion, tvEffect, tvPurchaseTip, tvTrimmingTip;
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
        tvEffect = findViewById(R.id.tvEffect);
        tvPurchaseTip = findViewById(R.id.tvPurchaseTip);
        tvTrimmingTip = findViewById(R.id.tvTrimmingTip);
        url = findViewById(R.id.url);

        setTitle(food.getFoodName());
        setData();

        url.setOnClickListener(this);

        Slidr.attach(this);

    }
    public void setData(){
        String effect;
        effect = food.getEffect().replace("-", "\n").replace("?", "");

        tvFoodName.setText(food.getFoodName());
        tvCategory.setText(food.getClassification());
        tvRegion.setText(food.getProductionRegion());
        tvEffect.setText(effect);
        tvPurchaseTip.setText(food.getPurchaseTips());
        tvTrimmingTip.setText(food.getTrimmingTips());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SeasonFoodDetails.this, DetailsURLActivity.class);
        intent.putExtra("url", food.getDetailsUrl());
        startActivity(intent);
    }
}
