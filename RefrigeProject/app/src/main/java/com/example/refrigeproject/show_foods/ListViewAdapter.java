package com.example.refrigeproject.show_foods;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.refrigeproject.R;

import org.aviran.cookiebar2.CookieBar;

class ListViewAdapter extends BaseAdapter {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.refrigerator_item, null);
        }

        TextView tvName = convertView.findViewById(R.id.tvName);
        tvName.setText(ShowFoodsFragment.refrigeratorList.get(position).getName());

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvFridgeName.setText(ShowFoodsFragment.refrigeratorList.get(position).getName());
                Log.d("log", ShowFoodsFragment.refrigeratorList.get(position).getName());

                CookieBar.dismiss(activity);
            }
        });

        return convertView;
    }
}