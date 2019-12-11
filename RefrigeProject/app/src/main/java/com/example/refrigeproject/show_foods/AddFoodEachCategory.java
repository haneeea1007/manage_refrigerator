package com.example.refrigeproject.show_foods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.refrigeproject.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AddFoodEachCategory extends Fragment {

    private View view;
    private GridView gridView;
    private ArrayList<AddFoodGridViewData> list = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;
    public static int position;
    static final String TAG = "check";



    public static AddFoodEachCategory newInstance() {
        AddFoodEachCategory addFoodEachCategory = new AddFoodEachCategory();
        return addFoodEachCategory;
    }

    @Nullable
    @Override //onCreate() 와 같은 기능을 한다. // 자동으로 콜백함수처럼 불러지게 된다.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_food, container, false);
        gridView = view.findViewById(R.id.gridView);


        gridViewAdapter = new GridViewAdapter(getContext(), R.layout.add_food_item, list);
        gridView.setAdapter(gridViewAdapter);

        listInsertFoodData();
        return view;
    }
    private void listInsertFoodData() {

        switch (position) {
            case 0:
                Integer[] vegeID = {R.drawable.vege_cucumber, R.drawable.vege_broccoli, R.drawable.vege_carrot, R.drawable.vege_chili, R.drawable.vege_corn,
                        R.drawable.vege_eggplant, R.drawable.vege_garlic, R.drawable.vege_radish, R.drawable.vege_onion};

                String[] vegeName = {"오이", "브로콜리", "당근", "고추", "옥수수", "가지", "마늘", "무", "양파"};
                list.clear();
                for (int i = 0; i < 8; i++) {
                    list.add(new AddFoodGridViewData(vegeID[i], vegeName[i]));
                }
                break;

            case 1:
                Integer[] fruitID = {R.drawable.fruit_apple, R.drawable.fruit_banana, R.drawable.fruit_blueberries, R.drawable.fruit_cherries, R.drawable.fruit_grapes, R.drawable.fruit_kiwi, R.drawable.fruit_lemon, R.drawable.fruit_melon,R.drawable.fruit_orange, R.drawable.fruit_peach, R.drawable.fruit_pear, R.drawable.fruit_pineapple, R.drawable.fruit_plum,R.drawable.fruit_tomato, R.drawable.fruit_watermelon};
                String[] fruitName = {"사과", "바나나", "블루베리", "체리", "포도", "키위", "레몬", "멜론", "오렌지", "복숭아", "배", "파인애플", "자두", "토마토", "수박"};

                list.clear();
                for (int i = 0; i < 14; i++) {
                    list.add(new AddFoodGridViewData(fruitID[i], fruitName[i]));
                }
                break;

        }
    }


        public class GridViewAdapter extends BaseAdapter implements View.OnClickListener {
            private Context context;
            private int layout;
            private ArrayList<AddFoodGridViewData> list;
            private LayoutInflater layoutInflater;
            private TextView tvFoodName;

            public GridViewAdapter(Context context, int layout, ArrayList<AddFoodGridViewData> list) {
                this.context = context;
                this.layout = layout;
                this.list = list;

                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View view, ViewGroup viewGroup) {

                if (view == null) {
                    view = layoutInflater.inflate(R.layout.add_food_item, null);
                }

                ImageView foodImageView = view.findViewById(R.id.foodImageView);
                tvFoodName = view.findViewById(R.id.tvFoodName);

                final AddFoodGridViewData addFoodGridViewData = list.get(position);
                foodImageView.setImageResource(addFoodGridViewData.getImageID());
                tvFoodName.setText(addFoodGridViewData.getFoodName());


                foodImageView.setOnClickListener(this);
                return view;
            }

            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getContext(), FoodDetailsActivity.class);

                intent.putExtra("FoodName", tvFoodName.getText().toString().trim());

                startActivityForResult(intent, 1000);
                Log.d(TAG, "vegeList");
            }

        }

        @Override
        public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1000 && resultCode == 1001) {


            }
        }
    }


