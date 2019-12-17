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

import com.bumptech.glide.Glide;
import com.example.refrigeproject.R;
import com.r0adkll.slidr.Slidr;

public class SeasonFoodDetails extends AppCompatActivity implements View.OnClickListener {
    LinearLayout url;
    TextView tvFoodName, tvCategory, tvRegion, tvEffect, tvPurchaseTip, tvTrimmingTip;
    ImageView imageView;
    SeasonalFood food;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season_food_details);

        Intent intent = getIntent();
        food = intent.getParcelableExtra("food");
        imageView = findViewById(R.id.imageView);
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
        String effect = food.getEffect().substring(1).replace("-", "\n");

        // 공공데이터포털 제공 URL 경로가 잘못돼서 대체
        Glide.with(this).load("https://www.foodnuri.go.kr/cmmn/file/getImage.do?atchFileId=94ae4a12d4c5d4b9a91f05709d97551b4eb172ae7b1dcbffb79573f27b54c2aa&fileSn=1").into(imageView);
        Log.d("parsing URL", food.getImageUrl());
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
