package com.example.refrigeproject.show_foods;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class ShowFoodsFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ShowFoodsFragment";
    private final String PREFERENCE = "com.example.refrigeproject";
    private View view;

    // Widget
    private RecyclerView rvFoods;
    private ConstraintLayout loading;
    private LinearLayout llRefrigerator;
    private SwipeRefreshLayout swipeRefreshLayout;

    // for adapter
    ArrayList<Section> items = new ArrayList<>();
    LinearLayoutManager layoutManager;
    SectionAdapter adapter;

    // 현재 선택한 냉장고
    public static RefrigeratorData selectedFridge;

    // 냉장고 리스트
    public static ArrayList<RefrigeratorData> refrigeratorList = new ArrayList<RefrigeratorData>();

    // 알람 리스트
    public static ArrayList<FoodData> alarmList = new ArrayList<FoodData>();

    // Widget in ViewHolder
    private TextView tvFridgeName;

    // 체크박스 값 저장
    private boolean removeMode;
    private Set<FoodData> removed = new HashSet<>(); // 현재 체크된 체크박스의 FoodData 모음 - delete 시 사용
    private Menu menu;

    // 내부인터페이스 객체참조변수
    private OnFragmentInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_foods, container, false);

        loading = view.findViewById(R.id.loading);
        rvFoods = view.findViewById(R.id.rvFoods);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        tvFridgeName = view.findViewById(R.id.tvFridgeName);
        llRefrigerator = view.findViewById(R.id.llRefrigerator);

        // 옵션메뉴 설정
        setHasOptionsMenu(true);

        // 로그인한 사용자의 알람을 설정
        getAlarmIdByUser();

        // 테이블 생성 및 냉장고 세팅
        llRefrigerator.setOnClickListener(this);
        getRefrigeratorData();

        // 어댑터 설정
        layoutManager = new LinearLayoutManager(getContext());
        rvFoods.setLayoutManager(layoutManager);
        adapter = new SectionAdapter();
        rvFoods.setAdapter(adapter);
        StickyHeaderItemDecorator decorator = new StickyHeaderItemDecorator(adapter);
        decorator.attachToRecyclerView(rvFoods);

        swipeRefreshLayout.setOnRefreshListener(this);
        Log.d("onResume", "크리에이트 실행");

        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d("onResume", "onResume 실행");
//        Intent intent = getActivity().getIntent();
//        String action = intent.getAction();
//
//        if(action != null && action.equals("refresh")){
//            Log.d("onResume", "onResume 실행");
//            Log.d("onResume", action);
//            adapter.items.clear();
//            getRefrigeratorData();
//            // 어댑터 설정
//            layoutManager = new LinearLayoutManager(getContext());
//            rvFoods.setLayoutManager(layoutManager);
//            adapter = new SectionAdapter();
//            rvFoods.setAdapter(adapter);
//            StickyHeaderItemDecorator decorator = new StickyHeaderItemDecorator(adapter);
//            decorator.attachToRecyclerView(rvFoods);
//
//            swipeRefreshLayout.setOnRefreshListener(this);
//
//        }
//    }

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
        switch (item.getItemId()) {// 아이템 추가 인텐트
            case R.id.action_add:
                Log.d(TAG, "action_add");
                Intent intent = new Intent(getContext(), AddFoodActivity.class);
                intent.putExtra("refrigerator", tvFridgeName.getText().toString()); // 냉장고 정보 전달
                startActivity(intent);

                break;

            case R.id.action_remove:// 삭제 모드로 전환
                Log.d(TAG, "action_remove");
                removeMode = true;
                getActivity().invalidateOptionsMenu(); // 삭제모드 메뉴로 바꿈
                adapter.notifyDataSetChanged(); // 체크박스 띄우기

                break;

            case R.id.action_search:// 해당 냉장고속 재료 검색
                Log.d(TAG, "action_search");
                searchFoodItems();

                break;

            case R.id.action_delete:// 선택한 목록 삭제
                Log.d(TAG, "action_delete");

                // 삭제 모드 중에 냉장고 바꿀 수 없음
                llRefrigerator.setEnabled(false);

                // 데이터 제거
                items.removeAll(removed);

                removed.clear();
                rvFoods.removeAllViews();
                adapter.notifyDataSetChanged();

                // 자동으로 완료 상태로
                menu.performIdentifierAction(R.id.action_done, 0);
                break;

            case R.id.action_done:// 삭제 모드 해제
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

    // 냉장고 코드와 보관유형에 따른 데이터 불러오기
    private void selectItems(){
        items.clear();
        rvFoods.removeAllViews();
        try {
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

                            Log.d(TAG,place + jsonArray.length()+"");

                            // 헤더 세팅
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
                        Toast.makeText(getContext(), "음식 데이터를 불러오지 못했습니다. \n다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    }
                }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

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
                        if(requestsCounter.get() < 0){
                            adapter.items = items;
                            adapter.notifyDataSetChanged();
                            loading.setVisibility(View.GONE);
                            swipeRefreshLayout.setRefreshing(false);
                            return;
                        }
                    }
                });
            }
        }catch (NullPointerException e){
            Log.e(TAG, e.toString());
        }
    }

    // 로그인한 사용자의 냉장고들 정보 가져오기
    private void getRefrigeratorData() {
        refrigeratorList.clear();

        try{
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            Log.d(TAG, "사용자 아이디 " + MainActivity.strId);

            StringRequest jsonArrayRequest = new StringRequest(
                    Request.Method.POST,
                    "http://jms1132.dothome.co.kr/getFridgeByUser.php",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("refrigerator");

                                Log.d(TAG,"refrigerator list length" + jsonArray.length()+"");

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
                                    // 등록된 냉장고가 없을 경우 등록 페이지로 연결
                                    Intent intent = new Intent(getContext(), AddFridgeActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getContext(), "등록된 냉장고가 없습니다.\n냉장고를 등록해주세요", Toast.LENGTH_SHORT).show();
                                } else{
                                    if(selectedFridge == null) {
                                        // 리스트 중 첫 번째 냉장고의 값을 리사이클러뷰에 세팅
                                        selectedFridge = refrigeratorList.get(0);
                                    }
                                    tvFridgeName.setText(selectedFridge.getName());
                                    selectItems(); // 냉장고 코드에 맞는 음식 데이터 불러오기
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

            requestQueue.add(jsonArrayRequest);
        }catch (NullPointerException e){
            Log.e(TAG, e.toString());
        }

    }

    // 로그아웃하고 다시 로그인했을 때 모든 알람 아이디 가져오기
    public void getAlarmIdByUser() {
        // 로그인한 사용자의 모든 알람정보 가져오기
        alarmList.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest jsonArrayRequest = new StringRequest(
                Request.Method.POST,
                "http://jms1132.dothome.co.kr/getAlarmIdByUser.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("food");

                            Log.d(TAG,"alarmID list length" + jsonArray.length()+"");

                            for(int i = 0 ; i < jsonArray.length() ; i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                FoodData foodData = new FoodData();
                                foodData.setName(object.getString("name"));
                                foodData.setAlarmID(object.getInt("alarmID"));
                                foodData.setExpirationDate(object.getString("expirationDate"));
                                alarmList.add(foodData);
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
                        Log.e(TAG, error.toString());
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

        requestQueue.add(jsonArrayRequest);

        // 로그아웃을 했는지 확인
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        boolean logout = sharedPreferences.getBoolean("logout", false);

        // 로그아웃을 했었다면 알람을 재등록
        if(logout){
            // alarmList에 받아온 알람 다시 등록
            allNotificationSetting();
            // 불리언값 바꾸기
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("logout", false);
            editor.commit();
        }
    }

    private void allNotificationSetting() {
        // 쉐어드프레퍼런스 체크값 가져와서 셋팅하기
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFERENCE, MODE_PRIVATE);

        int dateSetting = sharedPreferences.getInt("radioPref", 0);
        boolean switchSetting = sharedPreferences.getBoolean("switchPref", false);

        Log.d("switchPref", sharedPreferences.getBoolean("switchPref", false) + "");
        Log.d("radioPref", sharedPreferences.getInt("radioPref", 0) + "");

        // 테스트용 calendarTemp
        Calendar calendarTemp = Calendar.getInstance();
        for(int i = 0 ; i < alarmList.size() ; i++){
            FoodData foodData = alarmList.get(i);
            //expirationDate를 밀리초로 환산
            int year = Integer.parseInt(foodData.getExpirationDate().substring(0, 3));
            int month = Integer.parseInt(foodData.getExpirationDate().substring(5, 6));
            int day = Integer.parseInt(foodData.getExpirationDate().substring(8, 9));
            Log.d("밀리초환산테스트 ", year +""+ month +""+ day);

            calendarTemp.set(year, month, day);
            long millisTemp = calendarTemp.getTimeInMillis();

            if (dateSetting == 1) {
                millisTemp -= 86400000;

            } else if (dateSetting == 3) {
                millisTemp -= 86400000 * 3;

            } else if (dateSetting == 7) {
                millisTemp -= 86400000 * 7;
            }

            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            intent.putExtra("foodName", foodData.getName());
            intent.putExtra("dateSetting", dateSetting);
            intent.putExtra("switchSetting", switchSetting);
            intent.putExtra("id", foodData.getAlarmID());
            PendingIntent pender = PendingIntent.getBroadcast(getContext(), foodData.getAlarmID(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.set(AlarmManager.RTC_WAKEUP, millisTemp, pender);
            Log.d(TAG, "알람세팅" + calendarTemp.get(Calendar.YEAR) + "년 " + (calendarTemp.get(Calendar.MONTH) + 1) + "월 " + calendarTemp.get(Calendar.DAY_OF_MONTH) + "일 " + calendarTemp.get(Calendar.HOUR_OF_DAY) + "시 " + calendarTemp.get(Calendar.MINUTE) + "분 " + calendarTemp.get(Calendar.SECOND) + "초 " + calendarTemp.getTimeInMillis() + "원래 millis " + millisTemp + "수정 millis");

        }
    }

    private void searchFoodItems() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_find_food, null);
        final EditText editText = view.findViewById(R.id.edtSearchFood);

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("찾는 음식을 입력해주세요");
        dialog.setIcon(R.drawable.search_gray);
        dialog.setCancelable(true);
        dialog.setView(view);
        dialog.setPositiveButton("검색", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            filter(editText.getText().toString());
            }
        });
        dialog.show();
    }

    public void filter(String charText) {
        ArrayList<Section> arrayList = new ArrayList<>();
        arrayList.addAll(items);
        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();
        if (charText.length() == 0) {
            items.addAll(arrayList);
        } else {
            for (Section section : arrayList) {
                if(section.type() == 1){
                    String name = ((FoodData)section).getName();
                    if (name.contains(charText)) {
                        // 섹션 포지션에 따른 보관유형 헤더 달기
                        switch (((FoodData) section).getPostion()){
                            case 1: items.add(new SectionHeader(1)); break;
                            case 2: items.add(new SectionHeader(2)); break;
                            case 3: items.add(new SectionHeader(3)); break;
                        }
                        items.add(section);
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        try{
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
        }catch (NullPointerException e){
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onRefresh() {
        selectItems();
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
                }

                // 프로그레스바 세팅
                // 최대값 = 만료일 - 구입일
                // 현재 프로그레스 = 만료일 - 오늘
                int max = calculateDday(item.getPurchaseDate(), item.getExpirationDate()) - 1;
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
                    ((ItemViewHolder) holder).tvFoodName.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    ((ItemViewHolder) holder).tvDday.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                    ((ItemViewHolder) holder).swipeLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
                }else if(value < 0){
                    ((ItemViewHolder) holder).tvDday.setText("기간만료");
                    ((ItemViewHolder) holder).tvFoodName.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                    ((ItemViewHolder) holder).tvDday.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                    ((ItemViewHolder) holder).swipeLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
                }else{
                    ((ItemViewHolder) holder).tvDday.setText("D-" + value);
                    ((ItemViewHolder) holder).tvFoodName.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    ((ItemViewHolder) holder).tvDday.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    ((ItemViewHolder) holder).swipeLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
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
                    }
                });

                // 아이템 삭제
                ((ItemViewHolder) holder).delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // DB에서 삭제
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        Log.d(TAG, MainActivity.strId);

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
                                                // 삭제 확인 메세지
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
                                        Log.d(TAG, error.toString());
                                        Toast.makeText(getContext(), "아이템 삭제중 오류 발생", Toast.LENGTH_SHORT).show();
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

        // d-day 계산
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

    // 냉장고 리스트 어댑터
    public class ListViewAdapter extends BaseAdapter {
        private Activity activity;
        private TextView tvFridgeName;

        public ListViewAdapter(Activity activity, TextView tvFridgeName) {
            this.activity = activity;
            this.tvFridgeName = tvFridgeName;
        }

        @Override
        public int getCount() {
            return ShowFoodsFragment.refrigeratorList.size();
        }

        @Override
        public Object getItem(int position) {
            return ShowFoodsFragment.refrigeratorList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.refrigerator_item, null);
            }

            TextView tvName = convertView.findViewById(R.id.tvName);
            tvName.setText(ShowFoodsFragment.refrigeratorList.get(position).getName());

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        CookieBar.dismiss(activity);
                    }catch (NullPointerException e){
                        Log.e(TAG, e.toString());
                    }
                    if(tvFridgeName != null){
                        // 선택한 냉장고 이름 세팅
                        tvFridgeName.setText(ShowFoodsFragment.refrigeratorList.get(position).getName());
                        // 선택한 냉장고 정보 저장
                        ShowFoodsFragment.selectedFridge = ShowFoodsFragment.refrigeratorList.get(position);
                        Log.d("선택 냉장고 이름", ShowFoodsFragment.refrigeratorList.get(position).getName());
                        selectItems();
                    } else {
                        // 공유하기에서 눌렀을 때
                        Intent sharedMessage = new Intent(Intent.ACTION_SEND);
                        sharedMessage.addCategory(Intent.CATEGORY_DEFAULT);
                        sharedMessage.putExtra(Intent.EXTRA_SUBJECT, "냉장고 공유");
                        sharedMessage.putExtra(Intent.EXTRA_TEXT, "냉장고 같이 관리해요!\n냉장고 열쇠: " + ShowFoodsFragment.refrigeratorList.get(position).getCode());
                        sharedMessage.setType("text/plain");
                        parent.getContext().startActivity(Intent.createChooser(sharedMessage, "냉장고 공유하기"));
                    }
                }
            });

            return convertView;
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
