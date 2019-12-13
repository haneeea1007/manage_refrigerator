package com.example.refrigeproject.show_foods;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.CompoundButton;
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
import com.shuhart.stickyheader.StickyAdapter;
import com.shuhart.stickyheader.StickyHeaderItemDecorator;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        setFoodsData(adapter);

        setHasOptionsMenu(true);
        return view;
    }

    private void setFoodsData(SectionAdapter adapter) {
        items.add(new SectionHeader(1));
        items.add(new FoodData(1, "냉장템1 "));
        items.add(new FoodData(1, "냉장템2 "));
        items.add(new FoodData(1, "냉장템3 "));
        items.add(new FoodData(1, "냉장템4 "));

        items.add(new SectionHeader(2));
        items.add(new FoodData(2, "냉동템1 "));
        items.add(new FoodData(2, "냉동템2 "));
        items.add(new FoodData(2, "냉동템3 "));
        items.add(new FoodData(2, "냉동템4 "));

        items.add(new SectionHeader(3));
        items.add(new FoodData(3, "실온템1 "));
        items.add(new FoodData(3, "실온템2 "));
        items.add(new FoodData(3, "실온템3 "));
        items.add(new FoodData(3, "실온템4 "));
        items.add(new FoodData(3, "실온템5 "));
        items.add(new FoodData(3, "실온템6 "));

        adapter.items = items;
        adapter.notifyDataSetChanged();
    }

    // 냉장고 정보 가져오기
    private void getRefrigeratorData() {
        refrigeratorList.clear();
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
                        ((HeaderViewholder) holder).tvHeader.setText("냉장 " + section);
                        break;
                    case 2:
                        ((HeaderViewholder) holder).tvHeader.setText("냉동 " + section);
                        break;
                    case 3:
                        ((HeaderViewholder) holder).tvHeader.setText("실온 " + section);
                        break;
                }

            } else if (type == Section.ITEM){
                final FoodData item = (FoodData) items.get(position); // 해당 item 객체
                ((ItemViewHolder) holder).tvFoodName.setText("Item " + item.getName() + position);

                // 모드에 따른 Visibility 설정
                if(removeMode){
                    ((ItemViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
                }else{
                    ((ItemViewHolder) holder).checkBox.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).checkBox.setChecked(false);
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
                        bundle.putString("name", item.getName());
                        mListener.onFragmentInteraction(bundle);
                        // 로딩이 길다 ..
                    }
                });

                // 아이템 삭제
                ((ItemViewHolder) holder).delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("onClick", item.getName() + position +" "+ item.sectionPosition());
                        items.remove(position);
                        notifyDataSetChanged();
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
                    ((HeaderViewholder) holder).tvHeader.setText("냉장 " + headerPosition);
                    break;
                case 2:
                    ((HeaderViewholder) holder).tvHeader.setText("냉동 " + headerPosition);
                    break;
                case 3:
                    ((HeaderViewholder) holder).tvHeader.setText("실온 " + headerPosition);
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
            TextView tvFoodName;
            CheckBox checkBox;
            ImageView open;
            TextView delete;

            ItemViewHolder(View itemView) {
                super(itemView);
                tvFoodName = itemView.findViewById(R.id.tvFoodName);
                checkBox = itemView.findViewById(R.id.checkBox);
                delete = itemView.findViewById(R.id.delete);
                open = itemView.findViewById(R.id.open);
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
