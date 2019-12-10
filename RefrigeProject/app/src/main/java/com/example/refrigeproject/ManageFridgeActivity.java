package com.example.refrigeproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.swipe.SwipeLayout;
import com.r0adkll.slidr.Slidr;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

public class ManageFridgeActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lvFridgeList;
    LinearLayout llAddFridge;
    Activity activity;

    Dialog dialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_fridge);
        Slidr.attach(this);
        activity = this;

//        llAddFridge = findViewById(R.id.llAddFridge);
        lvFridgeList = findViewById(R.id.lvFridgeList);
        ListViewAdapter adapter = new ListViewAdapter();
        lvFridgeList.setAdapter(adapter);

//        llAddFridge.setOnClickListener(this);

        dialog = new Dialog(ManageFridgeActivity.this);

    }

    @Override
    public void onClick(View v) {

    }

    private class ListViewAdapter extends BaseAdapter{
        SwipeLayout llFridgeItem;
        ImageView ivFridge;
        TextView tvName, tvCode;

        @Override
        public int getCount() {
            return ShowFoodsFragment.refrigeratorList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if(position == ShowFoodsFragment.refrigeratorList.size() + 1) return "냉장고 추가하기";

            return ShowFoodsFragment.refrigeratorList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fridge_list_item, null);
            }

            llFridgeItem = convertView.findViewById(R.id.llFridgeItem);
            ivFridge = convertView.findViewById(R.id.ivFridge);
            tvName = convertView.findViewById(R.id.tvName);
            tvCode = convertView.findViewById(R.id.tvCode);

            if(ShowFoodsFragment.refrigeratorList.size() == position){
                Log.d("log", position+" / "+ ShowFoodsFragment.refrigeratorList.size());
                ivFridge.setImageResource(R.drawable.add_box);
                tvName.setText("냉장고 추가하기");
                tvCode.setVisibility(View.GONE);

                llFridgeItem.setSwipeEnabled(false);

                llFridgeItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final CookieBar.Builder build = CookieBar.build(activity);
                        build.setCustomView(R.layout.cookiebar_add_fridge);
                        build.setCustomViewInitializer(new CookieBar.CustomViewInitializer() {
                            @Override
                            public void initView(View view) {
                                TextView tvNew = view.findViewById(R.id.tvNew);
                                TextView tvExisting = view.findViewById(R.id.tvExisting);

                                tvNew.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        View dialogView = View.inflate(v.getContext(), R.layout.add_new_fridge, null);

                                        Dialog dialog = new Dialog(ManageFridgeActivity.this);
                                        dialog.setContentView(R.layout.add_new_fridge);
                                        dialog.show();
                                        CookieBar.dismiss(activity);
                                    }
                                });
                                tvExisting.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        View dialogView = View.inflate(v.getContext(), R.layout.add_existing_fridge, null);

                                        Dialog dialog = new Dialog(ManageFridgeActivity.this);
                                        dialog.setContentView(R.layout.add_existing_fridge);
                                        dialog.show();
                                        CookieBar.dismiss(activity);
                                    }
                                });
                            }
                        });
                        build.setTitle("냉장고 추가하기");
                        build.setSwipeToDismiss(true);
                        build.setEnableAutoDismiss(true);
                        build.setDuration(5000);
                        build.setCookiePosition(CookieBar.BOTTOM);
                        build.show();

                    }
                });
                Log.d("log", tvName.getText().toString());
            } else {
                llFridgeItem.addDrag(SwipeLayout.DragEdge.Bottom, llFridgeItem.findViewWithTag("bottom_tag"));

                final String str = ShowFoodsFragment.refrigeratorList.get(position);
                ivFridge.setImageResource(R.drawable.fridge);
                tvName.setText(str);
                tvCode.setVisibility(View.VISIBLE);
                tvCode.setText("aBcDeFg12345678"); // ArrayList 타입 다시 정의하기!
                llFridgeItem.setSwipeEnabled(true);

                llFridgeItem.setOnClickListener(null);
//                llFridgeItem.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        CookieBar.dismiss(activity);
//                        View dialogView = View.inflate(v.getContext(), R.layout.edit_fridge_info, null);
//                        EditText edtName = dialogView.findViewById(R.id.edtName);
//                        TextView dTextCode = dialogView.findViewById(R.id.tvCode);
//
//                        edtName.setText(str);
//                        dTextCode.setText("aBcDeFg12345678");
//
//                        Dialog dialog = new Dialog(ManageFridgeActivity.this);
//                        dialog.setContentView(R.layout.edit_fridge_info);
//                        dialog.show();
//                        return false;
//                    }
//                });

                llFridgeItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llFridgeItem.open();
                        CookieBar.dismiss(activity);
//                        View dialogView = View.inflate(v.getContext(), R.layout.edit_fridge_info, null);
//                        EditText edtName = dialogView.findViewById(R.id.edtName);
//                        TextView dTextCode = dialogView.findViewById(R.id.tvCode);
//
//                        edtName.setText(str);
//                        dTextCode.setText("aBcDeFg12345678");
//
//                        dialog = new Dialog(ManageFridgeActivity.this);
//                        dialog.setContentView(R.layout.edit_fridge_info);
//                        dialog.show();
                    }
                });



                llFridgeItem.addSwipeListener(new SwipeLayout.SwipeListener() {
                    @Override
                    public void onStartOpen(SwipeLayout layout) { }

                    @Override
                    public void onOpen(final SwipeLayout layout) {
                        // onOpen이 여러 번 호출되는 문제
                        dialog.dismiss();
//
//                        Log.d("log", "open");
//
//                        AlertDialog.Builder dialog = new AlertDialog.Builder(ManageFridgeActivity.this);
//                        dialog.setTitle("냉장고 삭제")
//                                .setMessage("냉장고를 삭제하시겠습니까?")
//                                .setCancelable(false)
//                                .setNeutralButton("yes", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // 삭제
//                                        layout.close();
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .setNegativeButton("no", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        layout.close();
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .show();

                        Toast.makeText(ManageFridgeActivity.this, "open", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStartClose(SwipeLayout layout) { }

                    @Override
                    public void onClose(SwipeLayout layout) { }

                    @Override
                    public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) { }

                    @Override
                    public void onHandRelease(SwipeLayout layout, float xvel, float yvel) { }
                });

            }

            return convertView;
        }
    }
}
