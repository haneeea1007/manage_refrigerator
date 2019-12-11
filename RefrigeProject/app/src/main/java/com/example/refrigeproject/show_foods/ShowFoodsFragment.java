package com.example.refrigeproject.show_foods;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refrigeproject.DBHelper;
import com.example.refrigeproject.R;
import com.saber.stickyheader.stickyView.StickHeaderItemDecoration;
import com.saber.stickyheader.stickyView.StickHeaderRecyclerView;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShowFoodsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ShowFoodsFragment";
    private View view;

    // for test
    int size_fridge, size_freezer, size_pantry;
    HeaderDataInfo headerData = new HeaderDataInfo(HeaderDataInfo.HEADER_TYPE, R.layout.header_item_recycler);

    List<FoodData> fridgeItems = new ArrayList<>(); // header별 구분용
    List<FoodData> freezerItems = new ArrayList<>(); // header별 구분용
    List<FoodData> pantryItems = new ArrayList<>(); // header별 구분용
    List<FoodData> foodList = new ArrayList<>(); // 전체 foodlist

    // 냉장고 리스트
    public static ArrayList<RefrigeratorData> refrigeratorList = new ArrayList<RefrigeratorData>();

    // Widget in ViewHolder
    public TextView tvFoodName, tvFridgeName, tvHeader, tvName;
    public ImageView delete, open;
    public CheckBox checkBox;

    // 체크박스 값 저장
    public boolean removeMode;
    int i = 0;
    Set<FoodData> removed = new HashSet<>(); // 현재 체크된 체크박스의 MainData 모음 - delete 시 사용
    ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>(); // 현재 리사이클러뷰에 있는 아이템의 체크박스 모음 - Visiblity 관리용
    Menu menu;
    private RecyclerView rvFoods;
    private LinearLayout llRefrigerator;

    private OnFragmentInteractionListener mListener;// 객체참조변수
    RecyclerAdapter adapter;

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
        setRefirigeratorData();
        llRefrigerator.setOnClickListener(this);

        adapter = new RecyclerAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        setData(adapter);

        rvFoods.setAdapter(adapter);
        rvFoods.setLayoutManager(layoutManager);
        rvFoods.addItemDecoration(new StickHeaderItemDecoration(adapter));

        setHasOptionsMenu(true);
        return view;
    }

    private void setRefirigeratorData() {
        getRefrigeratorData();
    }

    // 냉장고 정보 가져오기
    private void getRefrigeratorData() {
        Log.d(TAG, "getRefrigeratorData");
        sqLite = foodDBHelper.getReadableDatabase();

        String sql = "SELECT * FROM refrigeratorTBL;";
        Cursor cursor = sqLite.rawQuery(sql,null);

        RefrigeratorData data = null;
        while (cursor.moveToNext()) {
            data = new RefrigeratorData(cursor.getString(0), cursor.getString(1), cursor.getInt(2));
            refrigeratorList.add(data);
        }
        cursor.close();
        sqLite.close();
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
                Log.d("메뉴클릭", "action_add");
                Intent intent = new Intent(getContext(), AddFoodActivity.class);
                intent.putExtra("refrigerator", tvFridgeName.getText().toString()); // 냉장고 정보 전달
                startActivity(intent);

                break;

            case R.id.action_remove:
                // 삭제 모드로 전환
                Log.d("메뉴클릭", "action_remove");
                removeMode = true;
                getActivity().invalidateOptionsMenu();

                for(CheckBox checkBox : checkBoxes){
                    checkBox.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.action_search:
                // 해당 냉장고속 재료 검색
                Log.d("메뉴클릭", "action_search");

                break;

            case R.id.action_delete:
                // 선택한 목록 삭제
                Log.d("메뉴클릭", "action_delete");

                // 삭제 모드 중에 냉장고 바꿀 수 없음
                llRefrigerator.setEnabled(false);

                // 데이터 제거
                fridgeItems.removeAll(removed);
                freezerItems.removeAll(removed);
                pantryItems.removeAll(removed);

                removed.clear();
                checkBoxes.clear();

                rvFoods.removeAllViews();
                adapter.notifyDataSetChanged();

                // 자동으로 완료
                menu.performIdentifierAction(R.id.action_done, 0);
                break;

            case R.id.action_done:
                // 삭제 모드 해제
                Log.d("메뉴클릭", "action_done");

                llRefrigerator.setEnabled(true);

                removeMode = false;
                getActivity().invalidateOptionsMenu();

                for(CheckBox checkBox : checkBoxes){
                    checkBox.setVisibility(View.INVISIBLE);
                    checkBox.setChecked(false);
                }

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
                        ListViewAdapter listViewAdapter = new ListViewAdapter();
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

    private void setData(RecyclerAdapter adapter) {
        //DB에서 값 가져오기
        fridgeItems.add(new FoodData("토마토"));
        fridgeItems.add(new FoodData("된장찌개"));
        fridgeItems.add(new FoodData("마늘쫑"));
        fridgeItems.add(new FoodData("국밥"));
        fridgeItems.add(new FoodData("카레"));
        fridgeItems.add(new FoodData("치킨"));
        fridgeItems.add(new FoodData("무말랭이"));
        size_fridge = fridgeItems.size();
        adapter.setHeaderAndData(fridgeItems, headerData);

        freezerItems.add(new FoodData("감자"));
        freezerItems.add(new FoodData("아이스크림"));
        freezerItems.add(new FoodData("비비고왕교자"));
        freezerItems.add(new FoodData("토마토"));
        freezerItems.add(new FoodData("다진마늘"));
        freezerItems.add(new FoodData("쌀밥"));
        freezerItems.add(new FoodData("해쉬브라운"));
        freezerItems.add(new FoodData("목살"));
        size_freezer = freezerItems.size();
        adapter.setHeaderAndData(freezerItems, headerData);

        pantryItems = new ArrayList<>();
        pantryItems.add(new FoodData("짜파게티"));
        pantryItems.add(new FoodData("귤"));
        pantryItems.add(new FoodData("고구마"));
        pantryItems.add(new FoodData("참치"));
        pantryItems.add(new FoodData("스프"));
        pantryItems.add(new FoodData("라면"));
        pantryItems.add(new FoodData("과자"));
        size_pantry = pantryItems.size();
        adapter.setHeaderAndData(pantryItems, headerData);

        for(FoodData food : foodList){
            Log.d(TAG, food.getName());
        }

        Log.d(TAG, "냉장고 : " + size_fridge);
        Log.d(TAG, "냉장고 : " + size_freezer);
        Log.d(TAG, "냉장고 : " + size_pantry);

    }

    // 음식 데이터 관리 - sticky ver
    public class RecyclerAdapter extends StickHeaderRecyclerView<FoodData, HeaderDataInfo> {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case HeaderDataInfo.HEADER_TYPE:
                    return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item_recycler, parent, false));
                default:
                    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            // header인지 구분하여 홀더와 바인드
            if (holder instanceof ViewHolder) {
                ((ViewHolder) holder).bindData(position);
            } else if (holder instanceof HeaderViewHolder){
                ((HeaderViewHolder) holder).bindData(position);
            }
        }

        @Override
        public void bindHeaderData(View header, int headerPosition) {
            // 헤더가 상단에 닿았을 때
            TextView tv = header.findViewById(R.id.tvHeader);
            setHeaderTitle(tv, headerPosition, fridgeItems.size(), freezerItems.size());
        }


        // 헤더
        class HeaderViewHolder extends RecyclerView.ViewHolder {

            HeaderViewHolder(View itemView) {
                super(itemView);
                tvHeader = itemView.findViewById(R.id.tvHeader);
            }

            void bindData(int position) {
                Log.d(TAG, "header bind " + position);
                setHeaderTitle(tvHeader, position, fridgeItems.size(), freezerItems.size());
            }
        }

        // 내용
        class ViewHolder extends RecyclerView.ViewHolder{
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvFoodName = itemView.findViewById(R.id.tvFoodName);
                delete = itemView.findViewById(R.id.delete);
                open = itemView.findViewById(R.id.open);
                checkBox = itemView.findViewById(R.id.checkBox);
            }

            void bindData(final int position) {
                Log.d(TAG, "content bind " + position);

            }
        }

        private void setHeaderTitle(TextView tv, int headerPosition, int size_fridge, int size_freezer) {
            if(headerPosition <= size_fridge){
//                Log.d(TAG,"0번, 냉장입니다 ");
                tv.setText("냉장");
            }else if(headerPosition > size_fridge && headerPosition < size_fridge + size_freezer + 2) {
//                Log.d(TAG,(size_fridge+1)+"번, 냉동입니다 setheader");
//                Log.d(TAG,(headerPosition)+"번 header");
                tv.setText("냉동");
            }else if(headerPosition >= size_fridge + size_freezer +2 ){
//                Log.d(TAG,(size_fridge + size_freezer + 2)+"번, 실온입니다 ");
                tv.setText("실온");
            }
        }
    }

    // 냉장고 선택하기 쿠키바에 나올 냉장고 리스트
    class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return refrigeratorList.size();
        }

        @Override
        public Object getItem(int position) {
            return refrigeratorList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.refrigerator_item, null);
            }

            tvName = convertView.findViewById(R.id.tvName);
            tvName.setText(refrigeratorList.get(position).getName());

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setData(adapter);

                    tvFridgeName.setText(refrigeratorList.get(position).getName());
                    Log.d("log", refrigeratorList.get(position).getName());

                    CookieBar.dismiss(getActivity());
                }
            });

            return convertView;
        }
    }

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
