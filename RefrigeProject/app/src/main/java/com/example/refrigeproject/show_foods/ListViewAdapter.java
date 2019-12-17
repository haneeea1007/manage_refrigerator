package com.example.refrigeproject.show_foods;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.refrigeproject.R;

import org.aviran.cookiebar2.CookieBar;

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

                if(tvFridgeName != null){
                    // 선택한 냉장고 이름 세팅
                    tvFridgeName.setText(ShowFoodsFragment.refrigeratorList.get(position).getName());
                    // 선택한 냉장고 정보 저장
                    ShowFoodsFragment.selectedFridge = ShowFoodsFragment.refrigeratorList.get(position);
                    Log.d("선택 냉장고 이름", ShowFoodsFragment.refrigeratorList.get(position).getName());
                } else {
                    // 공유하기에서 눌렀을 때
                    Intent sharedMessage = new Intent(Intent.ACTION_SEND);
                    sharedMessage.addCategory(Intent.CATEGORY_DEFAULT);
                    sharedMessage.putExtra(Intent.EXTRA_SUBJECT, "냉장고 공유");
                    sharedMessage.putExtra(Intent.EXTRA_TEXT, "냉장고 같이 관리해요!\n냉장고 열쇠: " + ShowFoodsFragment.refrigeratorList.get(position).getCode());
                    sharedMessage.setType("text/plain");
                    parent.getContext().startActivity(Intent.createChooser(sharedMessage, "냉장고 공유하기"));
                }

                CookieBar.dismiss(activity);
            }
        });

        return convertView;
    }
}