package com.example.refrigeproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

import java.util.ArrayList;

public class ShowItemsFragment extends Fragment implements View.OnClickListener {
    View view;
    TextView tvRefrigerator;
    LinearLayout llRefrigerator;
    RecyclerView rvFridge, rvFreezer, rvPantry;
    LinearLayoutManager layoutManagerC;
    RecyclerView.Adapter<FoodItemViewHolder> adapterC1, adapterC2, adapterC3;

    Context context;
    Activity activity;

    TextView tvName;

    static ArrayList<String> foodList1 = new ArrayList<String>();
    static ArrayList<String> foodList2 = new ArrayList<String>();
    static ArrayList<String> foodList3 = new ArrayList<String>();
    static ArrayList<String> refrigeratorList = new ArrayList<String>();


    //for listpopupwindow
    Button btnfood;

    // for remove mode
    boolean removeMode;

    public ShowItemsFragment(Activity activity) {
        this.activity = activity;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.show_items_fragment, null, false);
        context = container.getContext();

        rvFreezer = view.findViewById(R.id.rvFreezer);
        rvFridge = view.findViewById(R.id.rvFridge);
        rvPantry = view.findViewById(R.id.rvPantry);
        tvRefrigerator = view.findViewById(R.id.tvRefrigerator);
        llRefrigerator = view.findViewById(R.id.llRefrigerator);

        refrigeratorList.add("메인 냉장고");
        refrigeratorList.add("김치냉장고");

        layoutManagerC = new LinearLayoutManager(container.getContext());
        rvFridge.setLayoutManager(layoutManagerC);
        adapterC1 = new FoodItemAdapter(foodList1);
        rvFridge.setAdapter(adapterC1);

        layoutManagerC = new LinearLayoutManager(container.getContext());
        rvFreezer.setLayoutManager(layoutManagerC);
        adapterC2 = new FoodItemAdapter(foodList2);
        rvFreezer.setAdapter(adapterC2);

        layoutManagerC = new LinearLayoutManager(container.getContext());
        rvPantry.setLayoutManager(layoutManagerC);
        adapterC3 = new FoodItemAdapter(foodList3);
        rvPantry.setAdapter(adapterC3);

        setFoodItems();

        llRefrigerator.setOnClickListener(this);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if(removeMode){
            inflater.inflate(R.menu.remove_mode_menu, menu);
        }else{
            inflater.inflate(R.menu.manage_food_menu, menu);
            menu.getItem(0).setIcon(R.drawable.add);
            menu.getItem(1).setIcon(R.drawable.remove);
            menu.getItem(2).setIcon(R.drawable.search);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                // 아이템 추가 인텐트
                Log.d("메뉴클릭", "action_add");
                break;

            case R.id.action_remove:
                // 삭제 모드로 전환
                // - 달린 아이템들
                Log.d("메뉴클릭", "action_remove");
                removeMode = true;
                getActivity().invalidateOptionsMenu();
                break;

            case R.id.action_search:
                // 해당 냉장고속 재료 검색
                Log.d("메뉴클릭", "action_search");
                break;

            case R.id.action_done:
                // 삭제 모드 해제
                Log.d("메뉴클릭","action_back");
                removeMode = false;
                getActivity().invalidateOptionsMenu();
                break;

            case R.id.action_delete:
                // 선택한 목록 삭제
                Log.d("메뉴클릭","action_delete");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFoodItems() {
        // 비우기
        foodList1.clear();
        foodList2.clear();
        foodList3.clear();
        rvFridge.removeAllViews();
        rvFreezer.removeAllViews();
        rvPantry.removeAllViews();

        // 새로운 값 받아오기
        // switch문 통합해서 DB에서 받으려면 id로 쿼리문실행해서 리스트에 add
        foodList1.add("메인 냉장실 aaa");
        foodList1.add("메인 냉장실 bbb");
        foodList1.add("메인 냉장실 ccc");
        foodList2.add("메인 냉동 1aaa");
        foodList2.add("메인 냉동 1bbb");
        foodList2.add("메인 냉동 1ccc");
        foodList3.add("메인 실온 참치");

        // notify
        adapterC1.notifyDataSetChanged();
        adapterC2.notifyDataSetChanged();
        adapterC3.notifyDataSetChanged();
        tvRefrigerator.setText(refrigeratorList.get(0));
    }
    

    @Override
    public void onClick(View v) {
        CookieBar.build(activity)
                .setCustomView(R.layout.select_refrigerator)
                .setCustomViewInitializer(new CookieBar.CustomViewInitializer() {
                    @Override
                    public void initView(View view) {
                        tvName = view.findViewById(R.id.tvName);

                        ListView listView = view.findViewById(R.id.listView);
                        ListViewAdapter listViewAdapter = new ListViewAdapter();
                        listView.setAdapter(listViewAdapter);
                    }
                })
                .setDuration(10000)
                .setAction("Close", new OnActionClickListener() {
                    @Override
                    public void onClick() {
                        CookieBar.dismiss(activity);
                    }
                })
                .setTitle("test")
                .setSwipeToDismiss(true)
                .setEnableAutoDismiss(true)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    class FoodItemAdapter extends RecyclerView.Adapter<FoodItemViewHolder>{
        ArrayList<String> list = new ArrayList<String>();

        public FoodItemAdapter(ArrayList<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d("onCreateView", (parent.getId() == R.id.rvFridge)+"");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item, null);
            FoodItemViewHolder foodItemViewHolder = new FoodItemViewHolder(view);
            return foodItemViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {

            return list.size();
        }
    }

    class FoodItemViewHolder extends RecyclerView.ViewHolder{
        public FoodItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class ListViewAdapter extends BaseAdapter{

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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.refrigerator_item, null);

            tvName = convertView.findViewById(R.id.tvName);
            tvName.setText(refrigeratorList.get(position));

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position){
                        case 0:
                            Log.d("onClick", "메인냉장고");

                            // 비우기
                            foodList1.clear();
                            foodList2.clear();
                            foodList3.clear();
                            rvFridge.removeAllViews();
                            rvFreezer.removeAllViews();
                            rvPantry.removeAllViews();

                            // 새로운 값 받아오기
                            // switch문 통합해서 DB에서 받으려면 id로 쿼리문실행해서 리스트에 add
                            foodList1.add("메인 냉장실 aaa");
                            foodList1.add("메인 냉장실 bbb");
                            foodList1.add("메인 냉장실 ccc");
                            foodList2.add("메인 냉동 1aaa");
                            foodList2.add("메인 냉동 1bbb");
                            foodList2.add("메인 냉동 1ccc");
                            foodList3.add("메인 실온 참치");
                            Log.d("메인냉장고 실온", foodList3.get(0));
                            Log.d("메인냉장고 냉장실", foodList1.get(2));

                            // notify
                            adapterC1.notifyDataSetChanged();
                            adapterC2.notifyDataSetChanged();
                            adapterC3.notifyDataSetChanged();
                            Log.d("메인냉장고 실온 notify", foodList3.get(0));
                            Log.d("메인냉장고 냉장실 notify", foodList1.get(2));


                            break;

                        case 1:
                            Log.d("onClick", "김치냉장고");
                            foodList1.clear();
                            foodList2.clear();
                            foodList3.clear();
                            rvFridge.removeAllViews();
                            rvFreezer.removeAllViews();
                            rvPantry.removeAllViews();


                            foodList1.add("김치 냉장 123");
                            foodList1.add("김치 냉장 456");
                            foodList2.add("김치 냉동 2");
                            foodList2.add("김치 냉동 2df");
                            foodList3.add("김치 실온 김치 된장");
                            foodList3.add("김치 실온 김치 고추장");

                            adapterC1.notifyDataSetChanged();
                            adapterC2.notifyDataSetChanged();
                            adapterC3.notifyDataSetChanged();
                            break;

                    }
                    tvRefrigerator.setText(refrigeratorList.get(position));
                    CookieBar.dismiss(activity);
                }
            });

            return convertView;
        }
    }
}