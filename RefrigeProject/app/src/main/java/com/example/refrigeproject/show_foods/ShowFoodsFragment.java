package com.example.refrigeproject.show_foods;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.refrigeproject.DBHelper;
import com.example.refrigeproject.MainActivity;
import com.example.refrigeproject.R;
import com.example.refrigeproject.setting.AddFridgeActivity;
import com.example.refrigeproject.setting.ManageFridgeActivity;
import com.shuhart.stickyheader.StickyAdapter;
import com.shuhart.stickyheader.StickyHeaderItemDecorator;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static android.content.Context.ALARM_SERVICE;

public class ShowFoodsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ShowFoodsFragment";
    private View view;

//    List<FoodData> fridgeItems = new ArrayList<>(); // header별 구분용
//    List<FoodData> freezerItems = new ArrayList<>(); // header별 구분용
//    List<FoodData> pantryItems = new ArrayList<>(); // header별 구분용
//    List<FoodData> foodList = new ArrayList<>(); // 전체 foodlist

    ArrayList<Section> items = new ArrayList<>();
    SectionAdapter adapter;

    public static RefrigeratorData selectedFridge;

    // 냉장고 리스트
    public static ArrayList<RefrigeratorData> refrigeratorList = new ArrayList<RefrigeratorData>();

    // Widget
    private RecyclerView rvFoods;
    private ConstraintLayout loading;
    private LinearLayout llRefrigerator;

    // Widget in ViewHolder
    public TextView tvFridgeName;

    // 체크박스 값 저장
    public boolean removeMode;
    int i = 0;
    Set<FoodData> removed = new HashSet<>(); // 현재 체크된 체크박스의 MainData 모음 - delete 시 사용
    Menu menu;

    private OnFragmentInteractionListener mListener;// 객체참조변수

    // DB
    private DBHelper foodDBHelper;
    private SQLiteDatabase sqLite;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_foods, container, false);

        loading = view.findViewById(R.id.loading);
        rvFoods = view.findViewById(R.id.rvFoods);
        tvFridgeName = view.findViewById(R.id.tvFridgeName);
        llRefrigerator = view.findViewById(R.id.llRefrigerator);

        // 테이블 생성 및 냉장고 세팅
        foodDBHelper = new DBHelper(getContext());
        getRefrigeratorData();
        llRefrigerator.setOnClickListener(this);

        // 어댑터 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFoods.setLayoutManager(layoutManager);
        adapter = new SectionAdapter();
        rvFoods.setAdapter(adapter);
        StickyHeaderItemDecorator decorator = new StickyHeaderItemDecorator(adapter);
        decorator.attachToRecyclerView(rvFoods);

        Log.d(TAG, "온크리에이트 실행?");

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "온리줌 실행?");
        adapter.notifyDataSetChanged();

    }

    private void selectItems(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        final AtomicInteger requestsCounter = new AtomicInteger(0);

        ArrayList<String> placeList = new ArrayList<String>();
        placeList.add("냉장");
        placeList.add("냉동");
        placeList.add("실온");

        for (final String place: placeList) {
            Log.d("DONE?", place + " " + requestsCounter);
            requestsCounter.incrementAndGet();

            queue.add(new StringRequest(
                Request.Method.POST,
                "http://jms1132.dothome.co.kr/getFoodByPlace.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("food");

                            Log.d("selectItems " + place,jsonArray.length()+"");
                            Log.d("ThreadName", Thread.currentThread().getName()+"");

                            if(place.equals("냉장")){
                                items.add(new SectionHeader(1));
                            } else if(place.equals("냉동")){
                                items.add(new SectionHeader(2));
                            } else if(place.equals("실온")){
                                items.add(new SectionHeader(3));
                            }

                            for(int i = 0 ; i < jsonArray.length() ; i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                FoodData foodData = new FoodData();

                                if(place.equals("냉장")){
                                    foodData.setPostion(1);
                                } else if(place.equals("냉동")){
                                    foodData.setPostion(2);
                                } else if(place.equals("실온")){
                                    foodData.setPostion(3);
                                }

                                foodData.setId(object.getInt("id"));
                                foodData.setCategory(object.getString("category"));
                                foodData.setSection(object.getString("section"));
                                foodData.setName(object.getString("name"));
                                foodData.setImagePath(object.getString("imagePath"));
                                foodData.setMemo(object.getString("memo"));
                                foodData.setPurchaseDate(object.getString("purchaseDate"));
                                foodData.setExpirationDate(object.getString("expirationDate"));
                                foodData.setCode(object.getString("code"));
                                foodData.setPlace(object.getString("place"));
                                foodData.setAlarmID(object.getInt("alarmID"));

                                items.add(foodData);

                                //debug
                                Log.d(TAG, items.size()+"");
                                Log.d(TAG, foodData.getName());
                                Log.d(TAG, foodData.getPlace());
                                Log.d(TAG, foodData.getImagePath());
                                Log.d(TAG, foodData.getId()+"");
                                Log.d(TAG, foodData.getCategory());
                                Log.d(TAG, foodData.getAlarmID()+"");
                                Log.d(TAG, foodData.getPurchaseDate());

                            }

                        }catch (JSONException e){
                            Log.d(TAG, e.toString());
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Log.d(TAG, error.toString());
                        Toast.makeText(getContext(), "음식 데이터를 불러오지 못했습니다. \n다시 시도해주세요", Toast.LENGTH_LONG).show();
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // 보내줄 인자
                params.put("code", selectedFridge.getCode());
                params.put("place", place);
                return params;
            }
        });
            queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                @Override
                public void onRequestFinished(Request<Object> request) {
                    requestsCounter.decrementAndGet();

                    Log.d(TAG, "SOMETHING DONE!" + requestsCounter.get());
                    if(requestsCounter.get() == 0){
                        Log.d(TAG, "ALL DONE!" + requestsCounter.get());
                        loading.setVisibility(View.GONE);
                    } else if(requestsCounter.get() < 0){
                        return;
                    }
                    adapter.items = items;
                    adapter.notifyDataSetChanged();

                }
            });
        }
    }

//    private void selectItems(final String place) {
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        StringRequest jsonArrayRequest = new StringRequest(
//                Request.Method.POST,
//                "http://jms1132.dothome.co.kr/getFoodByPlace.php",
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        try{
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray("food");
//
//                            Log.d("selectItems " + place,jsonArray.length()+"");
//                            Log.d("ThreadName", Thread.currentThread().getName()+"");
//
//                            for(int i = 0 ; i < jsonArray.length() ; i++) {
//                                JSONObject object = jsonArray.getJSONObject(i);
//                                FoodData foodData = new FoodData();
//
//                                if(place.equals("냉장")){
//                                    foodData.setPostion(1);
//                                } else if(place.equals("냉동")){
//                                    foodData.setPostion(2);
//                                } else if(place.equals("실온")){
//                                    foodData.setPostion(3);
//                                }
//
//                                foodData.setId(object.getInt("id"));
//                                foodData.setCategory(object.getString("category"));
//                                foodData.setSection(object.getString("section"));
//                                foodData.setName(object.getString("name"));
//                                foodData.setImagePath(object.getString("imagePath"));
//                                foodData.setMemo(object.getString("memo"));
//                                foodData.setPurchaseDate(object.getString("purchaseDate"));
//                                foodData.setExpirationDate(object.getString("expirationDate"));
//                                foodData.setCode(object.getString("code"));
//                                foodData.setPlace(object.getString("place"));
//                                foodData.setAlarmID(object.getInt("alarmID"));
//
//                                items.add(foodData);
//
//                                //debug
//                                Log.d(TAG, items.size()+"");
//                                Log.d(TAG, foodData.getName());
//                                Log.d(TAG, foodData.getPlace());
//                                Log.d(TAG, foodData.getImagePath());
//                                Log.d(TAG, foodData.getId()+"");
//                                Log.d(TAG, foodData.getCategory());
//                                Log.d(TAG, foodData.getAlarmID()+"");
//                                Log.d(TAG, foodData.getPurchaseDate());
//
//                                Log.d("setFoodsData 1", items.size()+"");
//                            }
//
//                        }catch (JSONException e){
//                            Log.d(TAG, e.toString());
//                        }
//                    }
//                },
//                new Response.ErrorListener(){
//                    @Override
//                    public void onErrorResponse(VolleyError error){
//                        // Do something when error occurred
//                        Log.d(TAG, error.toString());
//                    }
//                }
//
//        ){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                // 보내줄 인자
//                params.put("code", selectedFridge.getCode());
//                params.put("place", place);
//                return params;
//            }
//        };
//
//        // Add JsonArrayRequest to the RequestQueue
//        requestQueue.add(jsonArrayRequest);
//
//    }

    // 냉장고 정보 가져오기
    private void getRefrigeratorData() {
        refrigeratorList.clear();
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Log.d("testest","getRefrigeratorData");
        Log.d("testest 아이디",MainActivity.strId);

        // Initialize a new JsonArrayRequest instance
        StringRequest jsonArrayRequest = new StringRequest(
                Request.Method.POST,
                "http://jms1132.dothome.co.kr/getFridgeByUser.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("refrigerator");

                            Log.d("testest",jsonArray.length()+"");

                            for(int i = 0 ; i < jsonArray.length() ; i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                RefrigeratorData refrigerator = new RefrigeratorData();

                                refrigerator.setCode(object.getString("code"));
                                refrigerator.setName(object.getString("name"));
                                refrigerator.setType(object.getString("type"));

                                refrigeratorList.add(refrigerator);
                                Log.d(TAG, refrigerator.getName());

                            }

                            // 첫 냉장고 값 세팅
                            if(refrigeratorList.size() == 0 ){
                                Intent intent = new Intent(getContext(), AddFridgeActivity.class);
                                startActivity(intent);
                                Toast.makeText(getContext(), "등록된 냉장고가 없습니다.\n냉장고를 등록해주세요", Toast.LENGTH_SHORT).show();
                            }else{
                                selectedFridge = refrigeratorList.get(0);
                                tvFridgeName.setText(selectedFridge.getName());
//                            setFoodsData(adapter);

                                ////// 헤더는 빨리 추가되고 selectItems()가 늦게 작동해서 순서 안 맞음
                                selectItems();// test
//                            items.clear();
//                            items.add(new SectionHeader(1));
//                            selectItems("냉장");
//                            items.add(new SectionHeader(2));
//                            selectItems("냉동");
//                            items.add(new SectionHeader(3));
//                            selectItems("실온");

                                adapter.items = items;
                                adapter.notifyDataSetChanged();
                            }

                        }catch (JSONException e){
                            Log.e(TAG,e.toString());
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Log.d(TAG, error.toString());
                    }

                }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // 보내줄 인자
                params.put("id", MainActivity.strId);
                return params;
            }
        };

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);


//        Log.d(TAG, "getRefrigeratorData");
//        sqLite = foodDBHelper.getReadableDatabase();
//
//        String sql = "SELECT * FROM refrigeratorTBL;";
//        Cursor cursor = sqLite.rawQuery(sql,null);
//
//        RefrigeratorData data = null;
//        while (cursor.moveToNext()) {
//            data = new RefrigeratorData(cursor.getString(0), cursor.getString(1), cursor.getInt(2));
//            refrigeratorList.add(data);
//        }
//        cursor.close();
//        sqLite.close();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (removeMode) {
            this.menu = menu;
            inflater.inflate(R.menu.remove_mode_menu, menu);
        } else {
            inflater.inflate(R.menu.manage_food_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                // 아이템 추가 인텐트
                Log.d(TAG, "action_add");
                Intent intent = new Intent(getContext(), AddFoodActivity.class);
                intent.putExtra("refrigerator", tvFridgeName.getText().toString()); // 냉장고 정보 전달
                startActivity(intent);

                break;

            case R.id.action_remove:
                // 삭제 모드로 전환
                Log.d(TAG, "action_remove");
                removeMode = true;
                getActivity().invalidateOptionsMenu();

                adapter.notifyDataSetChanged();

                break;

            case R.id.action_search:
                // 해당 냉장고속 재료 검색
                Log.d(TAG, "action_search");

                break;

            case R.id.action_delete:
                // 선택한 목록 삭제
                Log.d(TAG, "action_delete");

                // 삭제 모드 중에 냉장고 바꿀 수 없음
                llRefrigerator.setEnabled(false);

                // 데이터 제거
                items.removeAll(removed);

                removed.clear();
                rvFoods.removeAllViews();
                adapter.notifyDataSetChanged();

                // 자동으로 완료
                menu.performIdentifierAction(R.id.action_done, 0);
                break;

            case R.id.action_done:
                // 삭제 모드 해제
                Log.d(TAG, "action_done");

                llRefrigerator.setEnabled(true);

                removeMode = false;
                getActivity().invalidateOptionsMenu();

                adapter.notifyDataSetChanged(); // 체크박스 재설정을 위함
                removed.clear();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        CookieBar.build(getActivity())
                .setCustomView(R.layout.cookiebar_select_fridge)
                .setCustomViewInitializer(new CookieBar.CustomViewInitializer() {
                    @Override
                    public void initView(View view) {

                        ListView listView = view.findViewById(R.id.listView);
                        ListViewAdapter listViewAdapter = new ListViewAdapter(getActivity(), tvFridgeName);
                        listView.setAdapter(listViewAdapter);
                    }
                })
                .setAction("Close", new OnActionClickListener() {
                    @Override
                    public void onClick() {
                        CookieBar.dismiss(getActivity());
                    }
                })
                .setSwipeToDismiss(true)
                .setEnableAutoDismiss(true)
                .setDuration(5000)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    // 음식 데이터 관리 - sticky ver
    public class SectionAdapter extends StickyAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder> {
        List<Section> items = new ArrayList<>();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == Section.HEADER || viewType == Section.CUSTOM_HEADER) {
                return new SectionAdapter.HeaderViewholder(inflater.inflate(R.layout.recycler_header_item, parent, false));
            }
            return new SectionAdapter.ItemViewHolder(inflater.inflate(R.layout.food_list_item, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            int type = items.get(position).type(); // 홀더 타입
            final int section = items.get(position).sectionPosition(); // section 번호

            if (type == Section.HEADER) {
                switch (section){
                    case 1:
                        ((HeaderViewholder) holder).tvHeader.setText("냉장");
                        break;
                    case 2:
                        ((HeaderViewholder) holder).tvHeader.setText("냉동");
                        break;
                    case 3:
                        ((HeaderViewholder) holder).tvHeader.setText("실온");
                        break;
                }

            } else if (type == Section.ITEM){
                final FoodData item = (FoodData) items.get(position); // 해당 item 객체
                ((ItemViewHolder) holder).tvFoodName.setText(item.getName());

                // 모드에 따른 Visibility, swipe 설정
                if(removeMode){
                    ((ItemViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).swipeLayout.setSwipeEnabled(false);
                }else{
                    ((ItemViewHolder) holder).checkBox.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).checkBox.setChecked(false);
                    ((ItemViewHolder) holder).swipeLayout.setSwipeEnabled(true);
                }

                // 이미지 세팅
                if(item.getImagePath() != null){
                    Glide.with(getContext()).load(item.getImagePath()).into(((ItemViewHolder) holder).imageView);
                    // 아이콘일 경우 scaleType //////////////////// 파라미터 수정 요망
                    if(item.getImagePath().contains("icon")){
                        ((ItemViewHolder) holder).imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    }
                }

                // 프로그레스바 세팅
                // 최대값 = 만료일 - 구입일
                // 현재 프로그레스 = 만료일 - 오늘
                int max = calculateDday(item.getPurchaseDate(), item.getExpirationDate());
                int value = calculateDday(null, item.getExpirationDate());
                Log.d("프로그레스바 최대", max+" "+item.getName());
                Log.d("프로그레스바 최대", value+" "+item.getName());
                if(max == 0){
                    // 오늘 구입 오늘 만료인 경우
                    ((ItemViewHolder) holder).progressBar.setMax(1);
                    ((ItemViewHolder) holder).progressBar.setProgress(1);
                }else{
                    ((ItemViewHolder) holder).progressBar.setMax(max);
                    ((ItemViewHolder) holder).progressBar.setProgress(max - value);
                }

                // D-day 값 세팅
                if(value == 0){
                    ((ItemViewHolder) holder).tvDday.setText("D-day!");
                    ((ItemViewHolder) holder).tvDday.setTextColor(Color.RED);
                }else if(value < 0){
                    ((ItemViewHolder) holder).tvDday.setText("기간만료");
                    ((ItemViewHolder) holder).tvFoodName.setTextColor(Color.RED);
                    ((ItemViewHolder) holder).tvDday.setTextColor(Color.RED);
                }else{
                    ((ItemViewHolder) holder).tvDday.setText("D-" + value);
                }

                // 체크박스 리스너
                ((ItemViewHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.d(TAG, "checkbox" + item.getName());
                        if(isChecked){
                            removed.add(item);
                        }else{
                            removed.remove(item);
                        }
                    }
                });

                // 레시피 검색
                ((ItemViewHolder) holder).open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle(1);
                        bundle.putString("name", item.getSection());
                        mListener.onFragmentInteraction(bundle);
                        // 로딩이 길다 ..
                    }
                });

                // 아이템 삭제
                ((ItemViewHolder) holder).delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // DB에서 삭제
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        Log.d(TAG, MainActivity.strId);

                        // Initialize a new JsonArrayRequest instance
                        StringRequest jsonArrayRequest = new StringRequest(
                                Request.Method.POST,
                                "http://jms1132.dothome.co.kr/deleteFood.php",
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if(success){
                                                // 삭제 확인
                                                ManageFridgeActivity.simpleCookieBar(item.getName() + "을(를) 삭제하였습니다.", getActivity());

                                                // 알람도 같이 삭제
                                                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
                                                Intent intent = new Intent(getContext(), AlarmReceiver.class);
                                                PendingIntent pender = PendingIntent.getBroadcast(getContext(), item.getAlarmID(), intent, 0);
                                                alarmManager.cancel(pender);

                                                // 데이터 변경 알림
                                                Log.d("onClick", item.getName() + position +" "+ item.sectionPosition());
                                                items.remove(position);
                                                notifyDataSetChanged();
                                            } else {
                                                ManageFridgeActivity.simpleCookieBar("삭제에 실패하였습니다.", getActivity());
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError error){
                                        // Do something when error occurred
                                        Log.d(TAG, error.toString());
                                    }

                                }

                        ){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                // 보내줄 인자
                                params.put("id", String.valueOf(item.getId()));
                                return params;
                            }
                        };
                        requestQueue.add(jsonArrayRequest);
                    }
                });

                ((ItemViewHolder)holder).tvFoodName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), FoodDetailsActivity.class);
                        intent.putExtra("food", item); // 음식 정보 전달하기
                        intent.setAction("edit");
                        startActivity(intent);
                    }
                });


            } else {
                ((HeaderViewholder) holder).tvHeader.setText("Custom header");
            }
        }


//        private int calculateTotalDay(String startDate, String expirationDate) {
//            try {
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Date start = simpleDateFormat.parse(startDate);
//                Date end = simpleDateFormat.parse(expirationDate);
//                long startMilli = start.getTime() / (24 * 60 * 60 * 1000);
//                long endMilli = end.getTime() / (24 * 60 * 60 * 1000);
//                // 오늘로부터 만료일까지 남은 일 수
//                int result = (int)(endMilli - startMilli);
//
//                Log.d("Milli start", startMilli+"");
//                Log.d("Milli end", endMilli+"");
//                Log.d("calculateDays result", result+"");
//
//                return result; // 오늘로부터 처리일자까지 남은 일 수 리턴
//            } catch (ParseException e) {
//                e.printStackTrace();
//                return -1;
//            }
//        }

        private int calculateDday(String purchaseDate, String expirationDate) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date end = simpleDateFormat.parse(expirationDate);
                long endMilli = end.getTime() / (24 * 60 * 60 * 1000);
                long startMilli = 0;
                if(purchaseDate == null){
                    // 오늘로부터의 D-day
                    startMilli = Calendar.getInstance().getTimeInMillis() / (24 * 60 * 60 * 1000);
                } else {
                    // 구입일로부터의 D-day
                    Date start = simpleDateFormat.parse(purchaseDate);
                    startMilli = start.getTime() / (24 * 60 * 60 * 1000);
                }
                // 오늘로부터 만료일까지 남은 일 수
                int result = (int)(endMilli - startMilli) + 1;

                Log.d("Milli start", startMilli+"");
                Log.d("Milli end", endMilli+"");
                Log.d("calculateDays result", result+"");

                return result; // 오늘로부터 처리일자까지 남은 일 수 리턴
            } catch (ParseException e) {
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        public int getItemViewType(int position) {
            return items.get(position).type();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getHeaderPositionForItem(int itemPosition) {
            return items.get(itemPosition).sectionPosition();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int headerPosition) {
            Log.d("onBindHeaderViewHolder",headerPosition+"");
            // 상단에 헤더가 붙어 있을 때
            switch (headerPosition){
                case 1:
                    ((HeaderViewholder) holder).tvHeader.setText("냉장");
                    break;
                case 2:
                    ((HeaderViewholder) holder).tvHeader.setText("냉동");
                    break;
                case 3:
                    ((HeaderViewholder) holder).tvHeader.setText("실온");
                    break;
            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            return createViewHolder(parent, Section.HEADER);
        }

        public class HeaderViewholder extends RecyclerView.ViewHolder {
            TextView tvHeader;

            HeaderViewholder(View itemView) {
                super(itemView);
                tvHeader = itemView.findViewById(R.id.tvHeader);
            }
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            SwipeLayout swipeLayout;
            CircleProgressBar progressBar;
            ImageView imageView;
            TextView tvFoodName, tvDday;
            CheckBox checkBox;
            ImageView open;
            TextView delete;

            ItemViewHolder(View itemView) {
                super(itemView);
                swipeLayout = itemView.findViewById(R.id.swipeLayout);
                progressBar = itemView.findViewById(R.id.progressBar);
                tvFoodName = itemView.findViewById(R.id.tvFoodName);
                imageView = itemView.findViewById(R.id.imageView);
                checkBox = itemView.findViewById(R.id.checkBox);
                tvDday = itemView.findViewById(R.id.tvDday);
                delete = itemView.findViewById(R.id.delete);
                open = itemView.findViewById(R.id.open);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }
    }

    // 프래그먼트 간 데이터 전달을 위한 인터페이스
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Bundle bundle); // 추상메소드
    }

        @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            // 이 컨텍스트 속에 리스너가 들어있냐, 즉 자식이냐
            mListener = (OnFragmentInteractionListener) context; // MainActivity(자식)의 객체를 가져옴 - 부모로 형변환
        } else {
            throw new RuntimeException(context.toString() + "OnFragmentInteractionListener을 구현하라");
        }
    }

}
