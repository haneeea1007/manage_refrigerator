package com.example.refrigeproject.show_foods;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.swipe.SwipeLayout;
import com.example.refrigeproject.DBHelper;
import com.example.refrigeproject.MainActivity;
import com.example.refrigeproject.R;
import com.example.refrigeproject.calendar.SeasonalFood;
import com.example.refrigeproject.database.GetRefrigerator;
import com.example.refrigeproject.database.ManageRequest;
import com.example.refrigeproject.search_recipe.Recipe;
import com.example.refrigeproject.setting.AddFridgeActivity;
import com.example.refrigeproject.setting.ManageFridgeActivity;
import com.google.android.material.snackbar.Snackbar;
import com.shuhart.stickyheader.StickyAdapter;
import com.shuhart.stickyheader.StickyHeaderItemDecorator;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

    // Widget in ViewHolder
    public TextView tvFridgeName, tvName;

    // 체크박스 값 저장
    public boolean removeMode;
    int i = 0;
    Set<FoodData> removed = new HashSet<>(); // 현재 체크된 체크박스의 MainData 모음 - delete 시 사용
    Menu menu;
    private RecyclerView rvFoods;
    private LinearLayout llRefrigerator;

    private OnFragmentInteractionListener mListener;// 객체참조변수

    // DB
    private DBHelper foodDBHelper;
    private SQLiteDatabase sqLite;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_foods, container, false);

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


        setHasOptionsMenu(true);
        return view;
    }

    private void setFoodsData(SectionAdapter adapter) {
        items.add(new SectionHeader(1));
        Log.d("ThreadName", Thread.currentThread().getName()+"");
        FoodData foodData = new FoodData();
        foodData.setPostion(1);
        foodData.setName("왕감자");
        foodData.setCategory("야채");
        foodData.setSection("감자");
        foodData.setMemo("베란다에 있음");
        foodData.setPlace("실온");


        items.add(foodData);
//        items.addAll(selectItems("냉장"));
        Log.d("setFoodsData 1", items.size()+"");
        items.add(new FoodData(1, "냉장템2 "));
//        items.add(new FoodData(1, "냉장템3 "));
//        items.add(new FoodData(1, "냉장템4 "));

        items.add(new SectionHeader(2));
//        selectItems(items,"냉동");
//        items.addAll(selectItems("냉동"));
        Log.d("setFoodsData 2", items.size()+"");
//        items.add(new FoodData(2, "냉동템1 "));
//        items.add(new FoodData(2, "냉동템2 "));
//        items.add(new FoodData(2, "냉동템3 "));
//        items.add(new FoodData(2, "냉동템4 "));

//        selectItems(items,"실온");
//        selectItems("실온");
        items.add(new SectionHeader(3));
        Log.d("setFoodsData 3", items.size()+"");
//        items.add(new FoodData(3, "실온템1 "));
//        items.add(new FoodData(3, "실온템2 "));
//        items.add(new FoodData(3, "실온템3 "));
//        items.add(new FoodData(3, "실온템4 "));
//        items.add(new FoodData(3, "실온템5 "));
//        items.add(new FoodData(3, "실온템6 "));

        adapter.items = items;
        adapter.notifyDataSetChanged();
    }

    private ArrayList<FoodData> selectItems(final String place) {
        final ArrayList<FoodData> dbList = new ArrayList<FoodData>();;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest jsonArrayRequest = new StringRequest(
                Request.Method.POST,
                "http://jms1132.dothome.co.kr/getFoodByPlace.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("food");

                            Log.d("testest " + place,jsonArray.length()+"");
                            Log.d("ThreadName", Thread.currentThread().getName()+"");

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

                                dbList.add(foodData);
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
                                Log.d(TAG, dbList.size()+"");

                                Log.d("setFoodsData 1", items.size()+"");
                            }

                        }catch (JSONException e){
                            Log.d("testest","catch");
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
                params.put("code", selectedFridge.getCode());
                params.put("place", place);
                return params;
            }
        };

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);

        return dbList;
    }

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
                            selectedFridge = refrigeratorList.get(0);
                            tvFridgeName.setText(selectedFridge.getName());
                            setFoodsData(adapter);

                            selectItems("냉장");
                            selectItems("냉동");
                            selectItems("실온");

                        }catch (JSONException e){
                            Log.d("testest","catch");
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
                                                simpleCookieBar(item.getName() + "을(를) 삭제하였습니다.");
//                                                Toast.makeText(getContext(), item.getName() + "(을)를 삭제에 하였습니다.", Toast.LENGTH_SHORT).show();

                                                // 데이터 변경 알림
                                                Log.d("onClick", item.getName() + position +" "+ item.sectionPosition());
                                                items.remove(position);
                                                notifyDataSetChanged();
                                            } else {
                                                simpleCookieBar("삭제에 실패하였습니다.");
//                                                Toast.makeText(getContext(), ".", Toast.LENGTH_SHORT).show();
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
                ((SectionAdapter.HeaderViewholder) holder).tvHeader.setText("Custom header");
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
//            ConstraintLayout constraintLayout;
            TextView tvFoodName;
            CheckBox checkBox;
            ImageView open;
            TextView delete;

            ItemViewHolder(View itemView) {
                super(itemView);
                swipeLayout = itemView.findViewById(R.id.swipeLayout);
                tvFoodName = itemView.findViewById(R.id.tvFoodName);
                checkBox = itemView.findViewById(R.id.checkBox);
                delete = itemView.findViewById(R.id.delete);
                open = itemView.findViewById(R.id.open);
            }
        }
    }

    public void simpleCookieBar(String message){
        CookieBar.build(getActivity())
                .setTitle(message)
                .setSwipeToDismiss(true)
                .setEnableAutoDismiss(true)
                .setDuration(2000)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
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
