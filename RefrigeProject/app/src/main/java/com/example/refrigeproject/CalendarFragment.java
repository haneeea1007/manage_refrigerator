package com.example.refrigeproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CalendarFragment extends Fragment {
    View view;
    Context context;

    private RecyclerView rvSeasonalFood;

    // 리사이블러뷰
    private Button btnFood;
    private LinearLayoutManager layoutManager;
    private ArrayList<SeasonalFood> seasonalFoods = new ArrayList<SeasonalFood>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calendar_fragment, null, false);
        context = container.getContext();

        rvSeasonalFood = view.findViewById(R.id.rvSeasonalFood);

        setSeasonalFoodInfo();

        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvSeasonalFood.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter();
        rvSeasonalFood.setAdapter(adapter);


        return view;
    }

    private void setSeasonalFoodInfo() {
        searchSeasonalFood(SearchRecipeFragment.getJsonString("SeasonalFood", context), seasonalFoods, "11월");

    }

    private void searchSeasonalFood(String json, ArrayList<SeasonalFood> seasonalFoods, String month) {
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray foodArray = jsonObject.getJSONArray("data");

            Log.d("testest",foodArray.length()+"");

            for(int i = 0 ; i < foodArray.length() ; i++) {
                JSONObject foodObject = foodArray.getJSONObject(i);
                SeasonalFood food = new SeasonalFood();

                if(foodObject.getString("M_DISTCTNS").equals(month)){
                    food.setFoodID(foodObject.getString("IDNTFC_NO"));
                    food.setFoodName(foodObject.getString("PRDLST_NM"));
                    food.setMonth(foodObject.getString("M_DISTCTNS"));
                    food.setClassification(foodObject.getString("PRDLST_CL"));
                    food.setProductionRegion(foodObject.getString("MTC_NM"));
                    food.setEffect(foodObject.getString("EFFECT"));
                    food.setPurchaseTips(foodObject.getString("PURCHASE_MTH"));
                    food.setCookTips(foodObject.getString("COOK_MTH"));
                    food.setTrimmingTips(foodObject.getString("TRT_MTH"));
                    food.setDetailsUrl(foodObject.getString("URL"));
                    food.setImageUrl(foodObject.getString("IMG_URL"));
                    food.setRegistDate(foodObject.getString("REGIST_DE"));
                    seasonalFoods.add(food);
                    Log.d("testest",food.getFoodName());
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder>{

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seasonal_food_item, null);
            CustomViewHolder customViewHolder = new CustomViewHolder(view);
            return customViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            btnFood.setText(seasonalFoods.get(position).getFoodName());

        }

        @Override
        public int getItemCount() {
            return seasonalFoods.size();
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder{

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            btnFood = itemView.findViewById(R.id.btnFood);
        }
    }

}
