package com.example.refrigeproject.show_foods;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.refrigeproject.R;

import java.util.ArrayList;
import java.util.logging.LogRecord;


public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<AddFoodGridViewData> list;
    private LayoutInflater layoutInflater;
    private TextView tvFoodName;
    private ImageView foodImageView;


    public static int select = -1;

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

        foodImageView = view.findViewById(R.id.foodImageView);
        tvFoodName = view.findViewById(R.id.tvFoodName);

        final AddFoodGridViewData addFoodGridViewData = list.get(position);

        foodImageView.setImageResource(addFoodGridViewData.getImageID());
        tvFoodName.setText(addFoodGridViewData.getSection());

        foodImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FoodDetailsActivity.class);
                intent.putExtra("foodData", addFoodGridViewData);
//                intent.putExtra("category", addFoodGridViewData.getCategory());
//                intent.putExtra("section", addFoodGridViewData.getSection());
//                intent.putExtra("image", addFoodGridViewData.getImageID());
                intent.setAction("add");
                context.startActivity(intent);
            }
        });

        return view;
    }

}