package com.example.refrigeproject.show_foods;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.refrigeproject.R;

import java.util.ArrayList;


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
        Intent intent = new Intent(context, FoodDetailsActivity.class);


    }
}