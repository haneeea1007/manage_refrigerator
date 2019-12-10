package com.example.refrigeproject.show_foods;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refrigeproject.R;
import com.saber.stickyheader.stickyView.StickHeaderItemDecoration;
import com.saber.stickyheader.stickyView.StickHeaderRecyclerView;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShowFoodsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private View view;

    // for test
    int size_fridge, size_freezer, size_pantry;
    HeaderDataInfo headerData = new HeaderDataInfo(HeaderDataInfo.HEADER_TYPE, R.layout.header_item_recycler);

    List<FoodData> fridgeItems = new ArrayList<>(); // header별 구분용
    List<FoodData> freezerItems = new ArrayList<>(); // header별 구분용
    List<FoodData> pantryItems = new ArrayList<>(); // header별 구분용
    List<FoodData> foodList = new ArrayList<>(); // 전체 foodlist


    // 냉장고, 음식 데이터
    RecyclerAdapter adapter;
    static ArrayList<String> foodList1 = new ArrayList<String>();
    static ArrayList<String> foodList2 = new ArrayList<String>();
    static ArrayList<String> foodList3 = new ArrayList<String>();
    public static ArrayList<String> refrigeratorList = new ArrayList<String>(Arrays.asList("메인 냉장고", "김치 냉장고", "sdf", "23", "142"));

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_foods, container, false);

        rvFoods = view.findViewById(R.id.rvFoods);
        tvFridgeName = view.findViewById(R.id.tvFridgeName);
        llRefrigerator = view.findViewById(R.id.llRefrigerator);

        // 냉장고 선택하기
        llRefrigerator.setOnClickListener(this);

//        // 냉장실 음식
//        linearLayoutManager = new LinearLayoutManager(container.getContext());
//        rvFridge.setLayoutManager(linearLayoutManager);
//        fridgeAdapter = new MainAdapter(foodList1);
//        rvFridge.setAdapter(fridgeAdapter);
//
//        // 냉동실 음식
//        linearLayoutManager = new LinearLayoutManager(container.getContext());
//        rvFreezer.setLayoutManager(linearLayoutManager);
//        freezerAdapter = new MainAdapter(foodList2);
//        rvFreezer.setAdapter(freezerAdapter);
//
//        // 실온 음식
//        linearLayoutManager = new LinearLayoutManager(container.getContext());
//        rvPantry.setLayoutManager(linearLayoutManager);
//        pantryAdapter = new MainAdapter(foodList3);
//        rvPantry.setAdapter(pantryAdapter);


        adapter = new RecyclerAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        setData(adapter);

        rvFoods.setAdapter(adapter);
        rvFoods.setLayoutManager(layoutManager);
        rvFoods.addItemDecoration(new StickHeaderItemDecoration(adapter));
//        setFoodItems();

        setHasOptionsMenu(true);
        return view;
    }

    private void setFoodItems() {
        // 비우기
        foodList1.clear();
        foodList2.clear();
        foodList3.clear();

//        rvFridge.removeAllViews();
//        rvFreezer.removeAllViews();
//        rvPantry.removeAllViews();

        // 새로운 값 받아오기
        // switch문 통합해서 DB에서 받으려면 id로 쿼리문실행해서 리스트에 add
        foodList1.add("메인 냉장실 aaa");
        foodList1.add("메인 냉장실 bbb");
        foodList1.add("메인 냉장실 ccc");
        foodList2.add("메인 냉동 1aaa");
        foodList2.add("메인 냉동 1bbb");
        foodList2.add("메인 냉동 1ccc");
        foodList3.add("참치");

        // notify
        notifyToAdapter();

        tvFridgeName.setText(refrigeratorList.get(0));
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

    private void notifyToAdapter() {
//        fridgeAdapter.notifyDataSetChanged();
//        freezerAdapter.notifyDataSetChanged();
//        pantryAdapter.notifyDataSetChanged();
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

    }

    // 음식 데이터 관리 - sticky ver
    public class RecyclerAdapter extends StickHeaderRecyclerView<FoodData, HeaderDataInfo> {
//        String headerName;
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
            if (holder instanceof ViewHolder) {
                ((ViewHolder) holder).bindData(position);
            } else if (holder instanceof HeaderViewHolder){
                ((HeaderViewHolder) holder).bindData(position);
            }
        }

        @Override
        public void bindHeaderData(View header, int headerPosition) {
            // 들러 붙었을 때
            TextView tv = header.findViewById(R.id.tvHeader);

            if (headerPosition <= size_fridge){
                tv.setText("냉장");
            } else if (headerPosition > size_fridge && headerPosition < (size_fridge + size_freezer + 1)){
                tv.setText("냉동");
            } else {
                tv.setText("실온");
            }
        }

        class HeaderViewHolder extends RecyclerView.ViewHolder {

            HeaderViewHolder(View itemView) {
                super(itemView);
                tvHeader = itemView.findViewById(R.id.tvHeader);
            }

            void bindData(int position) {
                if (position > size_fridge && position < (size_fridge + size_freezer + 1)){
                    tvHeader.setText("냉동");
                } else {
                    tvHeader.setText("실온");
                }
            }
        }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            delete = itemView.findViewById(R.id.delete);
            open = itemView.findViewById(R.id.open);
            checkBox = itemView.findViewById(R.id.checkBox);
        }


        void bindData(final int position) {
            if (position <= size_fridge){
                tvFoodName.setText("냉장 " + position);
            } else if (position > size_fridge && position < (size_fridge + size_freezer + 1)){
                tvFoodName.setText("냉동 " + position);
            } else {
                tvFoodName.setText("실온 " + position);
            }

            tvFoodName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), tvFoodName.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
//            if(position < size_fridge + 1){
//                tvFoodName.setText(position+"냉장");
//            } else if (position < size_fridge + size_freezer + 2){
//                tvFoodName.setText(position+"냉동");
//            } else {
//                tvFoodName.setText(position+"실온");
//            }
//
//
//            // 아이템 삭제
//            delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    // 어느 장소의 음식인지 확인
//                    if(position <= size_fridge){
//                        fridgeItems.remove(position-1);
//                        adapter.setHeaderAndData(fridgeItems, headerData);
//                        rvFoods.removeAllViews();
//                        notifyItemRemoved(position);
////                            adapter.notifyDataSetChanged();
//                    }
//
//                    for(FoodData food : fridgeItems){
//                        Log.d(TAG,food.getName());
//                    }
//
//                    String currentName = tvFoodName.getText().toString().trim();
//                    Toast.makeText(v.getContext(), currentName + " 삭제 완료", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            // 활용 레시피 열기
//            open.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    String currentName = tvFoodName.getText().toString().trim();
//
//                    Bundle bundle = new Bundle(1);
//                    bundle.putString("name", currentName);
//                    mListener.onFragmentInteraction(bundle);
//
//
//                    Toast.makeText(v.getContext(), currentName, Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            // 체크박스 리스트에 담아놓기 - Visibility 관리용
//            checkBoxes.add(checkBox);
////                Log.d("checkBoxes에 add중", foodList.get(position)+"번 만들어짐 " + position);
//
//            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if(isChecked){
//                        Log.d("onCheckedChanged", position+"번 체크 설정");
//                        removed.add(fridgeItems.get(position));
//                    } else {
//                        Log.d("onCheckedChanged", position+"번 체크 해제");
//                        removed.remove(fridgeItems.get(position));
//                    }
//                }
//            });
        }


        }
    }

    // 음식 데이터를 관리하기 위한 어댑터
//    public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {
//
//        private ArrayList<String> list = new ArrayList<String>();
//
//        public MainAdapter(ArrayList<String> list) {
//            this.list = list;
//        }
//
//        @NonNull
//        @Override
//        public MainAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_list_item, viewGroup, false);
//            CustomViewHolder viewHolder = new CustomViewHolder(view);
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull final MainAdapter.CustomViewHolder customViewHolder, final int position) {
//            tvFoodName.setText(list.get(position));
//            customViewHolder.itemView.setTag(position);
//
//            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(v.getContext(), list.get(position) + " 선택", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//
//        @Override
//        public int getItemCount() {
//            return (list != null) ? (list.size()) : (0);
//        }
//
//        public class CustomViewHolder extends RecyclerView.ViewHolder {
//
//            public CustomViewHolder(@NonNull View itemView) {
//                super(itemView);
//                imgProfile = itemView.findViewById(R.id.imageView);
//                tvFoodName = itemView.findViewById(R.id.tvFoodName);
//                delete = itemView.findViewById(R.id.delete);
//                open = itemView.findViewById(R.id.open);
//                checkBox = itemView.findViewById(R.id.checkBox);
//            }
//
//        }
//    }

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
            tvName.setText(refrigeratorList.get(position));

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 비우기
                    foodList1.clear();
                    foodList2.clear();
                    foodList3.clear();
//                    rvFridge.removeAllViews();
//                    rvFreezer.removeAllViews();
//                    rvPantry.removeAllViews();

                    // 새로운 값 받아오기
                    // switch문 통합해서 DB에서 받으려면 id로 쿼리문실행해서 리스트에 add
                    foodList1.add("메인 냉장실 aaa");
                    foodList1.add("메인 냉장실 bbb");
                    foodList1.add("메인 냉장실 ccc");
                    foodList2.add("메인 냉동 1aaa");
                    foodList2.add("메인 냉동 1bbb");
                    foodList2.add("메인 냉동 1ccc");
                    foodList3.add("참치");

                    // notify
                    notifyToAdapter();

                    tvFridgeName.setText(refrigeratorList.get(position));
                    Log.d("log", refrigeratorList.get(position));

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
