package com.example.refrigeproject.calendar;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refrigeproject.R;
import com.example.refrigeproject.search_recipe.SearchRecipeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CalendarFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    View view;
    Context context;

    private RecyclerView rvSeasonalFood;
    private ImageButton ibtPrev;
    private ImageButton ibtNext;

    // 제철음식
    private Button btnFood;
    private LinearLayoutManager layoutManager;
    private ArrayList<SeasonalFood> seasonalFoods = new ArrayList<SeasonalFood>();
    private RecyclerViewAdapter recyclerAdapter;

    //달력
    private final static String TAG = "CalendarFragment";

    GridView gvCalendar;
    TextView tvYearMonth, tvDateSelected;
    private CalendarAdapter calendarAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, null, false);
        context = container.getContext();

        rvSeasonalFood = view.findViewById(R.id.rvSeasonalFood);
        gvCalendar = view.findViewById(R.id.gvCalendar);
        ibtPrev = view.findViewById(R.id.ibtPrev);
        ibtNext = view.findViewById(R.id.ibtNext);

        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvSeasonalFood.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerViewAdapter();
        rvSeasonalFood.setAdapter(recyclerAdapter);

        calendarAdapter = new CalendarAdapter(context);
        gvCalendar.setAdapter(calendarAdapter);
        tvYearMonth = view.findViewById(R.id.tvYearMonth);

        setSeasonalFoodInfo();

        ibtPrev.setOnClickListener(this);
        ibtNext.setOnClickListener(this);
        gvCalendar.setOnItemClickListener(this);

        return view;
    }

    private void setSeasonalFoodInfo() {
        setYearMonth();
        if(seasonalFoods!=null) seasonalFoods.clear();
        searchSeasonalFood(SearchRecipeFragment.getJsonString("SeasonalFood", context), seasonalFoods, (calendarAdapter.currentMonth+1)+"월");
        rvSeasonalFood.removeAllViews();
        recyclerAdapter.notifyDataSetChanged();
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
                    Log.d(TAG, food.getFoodName());
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtPrev:
                calendarAdapter.setPreviousMonth();
                break;
            case R.id.ibtNext:
                calendarAdapter.setNextMonth();
                break;
        }
        calendarAdapter.notifyDataSetChanged();
        setSeasonalFoodInfo();
    }

    private void setYearMonth() {
        String yearMonth = calendarAdapter.currentYear+"년 " + (calendarAdapter.currentMonth+1) + "월";
        tvYearMonth.setText(yearMonth);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DayItem item = calendarAdapter.items[position];
        final int day = item.getDayValue();
        if(day == 0) return;
//        final String currentDate = calendarAdapter.currentYear+"년 " + (calendarAdapter.currentMonth+1) + "월 " + day + "일";
    }

    // 제철음식 리사이클러뷰
    private class RecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder>{

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seasonal_food_item, null);
            CustomViewHolder customViewHolder = new CustomViewHolder(view);
            return customViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {
            final SeasonalFood food = seasonalFoods.get(position);
            btnFood.setText(food.getFoodName());
            btnFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 클릭 시 인텐트
                    Intent intent = new Intent(getActivity(), SeasonFoodDetails.class);
                    intent.putExtra("food", food);
                    startActivity(intent);
                }
            });
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
