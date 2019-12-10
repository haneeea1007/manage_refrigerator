package com.example.refrigeproject.calendar;

import android.content.Context;
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

import com.example.refrigeproject.FoodDBHelper;
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

    // 리사이블러뷰
    private Button btnFood;
    private LinearLayoutManager layoutManager;
    private ArrayList<SeasonalFood> seasonalFoods = new ArrayList<SeasonalFood>();

    //달력
    private final static String TAG = "MainActivity";

    GridView gvCalendar;
    Button btnPrevious, btnNext;
    TextView tvYearMonth, tvDateSelected;
    EditText edtEvent;
    private CalendarAdapter calendarAdapter;

    FoodDBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, null, false);
        context = container.getContext();

        rvSeasonalFood = view.findViewById(R.id.rvSeasonalFood);
        gvCalendar = view.findViewById(R.id.gvCalendar);
        ibtPrev = view.findViewById(R.id.ibtPrev);
        ibtNext = view.findViewById(R.id.ibtNext);

        setSeasonalFoodInfo();

        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvSeasonalFood.setLayoutManager(layoutManager);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter();
        rvSeasonalFood.setAdapter(recyclerViewAdapter);


        calendarAdapter = new CalendarAdapter(context);
        gvCalendar.setAdapter(calendarAdapter);
        tvYearMonth = view.findViewById(R.id.tvYearMonth);


        setYearMonth();

        ibtPrev.setOnClickListener(this);
        ibtNext.setOnClickListener(this);
        gvCalendar.setOnItemClickListener(this);

        dbHelper = new FoodDBHelper(context);


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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtPrev:
                calendarAdapter.setPreviousMonth();
                calendarAdapter.notifyDataSetChanged();
                setYearMonth();
                break;
            case R.id.ibtNext:
                calendarAdapter.setNextMonth();;
                calendarAdapter.notifyDataSetChanged();
                setYearMonth();
                break;
        }
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
        final String currentDate = calendarAdapter.currentYear+"년 " + (calendarAdapter.currentMonth+1) + "월 " + day + "일";

//        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_event_dialog, null);
//        final EditText edtEvent = view.findViewById(R.id.edtEvent);
//        TextView tvDateSelected = view.findViewById(R.id.tvDateSelected);
//        tvDateSelected.setText(currentDate);
//
//
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//        dialog.setTitle("생일 추가하기");
//        dialog.setView(view);
//
//        dialog.setPositiveButton("등록", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // DB에 저장하기 /////////////////////////////////////////////////
//                sqLiteDatabase = dbHelper.getWritableDatabase();
//                String str = "INSERT INTO calendarTBL values(" +
//                        calendarAdapter.currentYear + ", " +
//                        (calendarAdapter.currentMonth + 1) + ", " + day + ", '" + edtEvent.getText().toString() +"');";
//                sqLiteDatabase.execSQL(str);
//                sqLiteDatabase.close();
//                Log.d("MainActivity", currentDate+"정보 입력완료 ");
//                /////////////////////////////////////////////////////////////////
//
//                calendarAdapter.notifyDataSetChanged();
//            }
//        });
//        dialog.setNegativeButton("취소", null);
//        dialog.show();
//
//        Log.d(TAG, currentDate);
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
