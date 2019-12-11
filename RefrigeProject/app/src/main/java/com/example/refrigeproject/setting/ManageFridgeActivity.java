package com.example.refrigeproject.setting;

import android.app.Activity;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.swipe.SwipeLayout;
import com.example.refrigeproject.DBHelper;
import com.example.refrigeproject.R;
import com.example.refrigeproject.checklist.CheckListData;
import com.example.refrigeproject.show_foods.ShowFoodsFragment;
import com.r0adkll.slidr.Slidr;

import org.aviran.cookiebar2.CookieBar;

import java.util.Random;

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

        // 냉장고 추가하기 다이얼로그 창
        RadioGroup radioGroup;
        RadioButton rbRef1, rbRef2, rbRef3;
        EditText edtTxt;
        Button btnRefAdd;
        String refName;

        DBHelper fridgeDBHelper;
        SQLiteDatabase sqLiteDatabase;

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

                                //====================================================================================//
                                // 냉장고 추가하기 버튼을 눌렀을 때 이벤트
                                tvNew.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        fridgeDBHelper = new DBHelper(v.getContext());

                                        // 초기화 할때
                                        sqLiteDatabase = fridgeDBHelper.getWritableDatabase();
                                        fridgeDBHelper.onUpgrade(sqLiteDatabase,1,2);
                                        sqLiteDatabase.close();

                                        final View dialogView = View.inflate(v.getContext(), R.layout.add_new_fridge, null);

                                        Dialog dialog = new Dialog(ManageFridgeActivity.this);

                                        // 이걸로하면 화면이 작아짐
                                        dialog.setContentView(dialogView);

                                        // 이걸로하면 화면은 정상
//                                        dialog.setContentView(R.layout.add_new_fridge);

                                        radioGroup = dialogView.findViewById(R.id.radioGroup);
                                        rbRef1 = dialogView.findViewById(R.id.rbRef1);
                                        rbRef2 = dialogView.findViewById(R.id.rbRef2);
                                        rbRef3 = dialogView.findViewById(R.id.rbRef3);
                                        btnRefAdd = dialogView.findViewById(R.id.btnRefAdd);
                                        edtTxt = dialogView.findViewById(R.id.edtTxt);

                                        Log.d("ID", "아이디찾기");

                                        // dialog.setContentView(R.layout.add_new_fridge); 일때 여기까지 실행됨
                                        //================================================================//


                                        btnRefAdd.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                Log.d("BUTTON", "click");

                                                // 이름 입력 안했을 때 저장X (정상작동)
                                                if ((edtTxt.getText().toString()).equals("")) {
                                                    Toast.makeText(getApplicationContext(), "이름을 입력해 주세요.", Toast.LENGTH_LONG).show();

                                                } else if (radioGroup.getCheckedRadioButtonId() != R.id.rbRef1 &&
                                                        radioGroup.getCheckedRadioButtonId() != R.id.rbRef2 &&
                                                        radioGroup.getCheckedRadioButtonId() != R.id.rbRef3) {
                                                    Toast.makeText(getApplicationContext(), "냉장고 유형을 선택해 주세요.", Toast.LENGTH_LONG).show();

                                                } else {

                                                    // 랜덤코드 만들기 (정상작동)
                                                    Random random = new Random();
                                                    StringBuffer bufCode = new StringBuffer();
                                                    String randomCode;

                                                    for (int i = 0; i < 20; i++) {
                                                        if (random.nextBoolean()) {
                                                            bufCode.append((char) ((int) (random.nextInt(26)) + 97));
                                                        } else {
                                                            bufCode.append((random.nextInt(10)));
                                                        }
                                                    }

                                                    randomCode = bufCode.toString().trim();
                                                    Log.d("CODE", randomCode);

                                                    // 냉장고 이름 가져오기
                                                    refName = edtTxt.getText().toString().trim();
                                                    Log.d("NAME", refName);

                                                    // 선택한 라디오버튼 이미지 아이디를 구별해서 DB에 저장(로그보면 정상)
                                                    switch (radioGroup.getCheckedRadioButtonId()) {

                                                        case R.id.rbRef1:
                                                            Log.d("IMAGE", String.valueOf(R.drawable.fridge1));

                                                            sqLiteDatabase = fridgeDBHelper.getWritableDatabase();

                                                            String str = "INSERT INTO refrigeratorTBL values('" +
                                                                    randomCode + "', '" +
                                                                    refName + "', " +
                                                                    R.drawable.fridge1 + ");";

                                                            sqLiteDatabase.execSQL(str);
                                                            sqLiteDatabase.close();
                                                            break;

                                                        case R.id.rbRef2:
                                                            Log.d("IMAGE", String.valueOf(R.drawable.fridge2));

                                                            sqLiteDatabase = fridgeDBHelper.getWritableDatabase();

                                                            String str2 = "INSERT INTO refrigeratorTBL values('" +
                                                                    randomCode + "', '" +
                                                                    refName + "', '" +
                                                                    R.drawable.fridge2 + "');";

                                                            sqLiteDatabase.execSQL(str2);
                                                            sqLiteDatabase.close();
                                                            break;

                                                        case R.id.rbRef3:
                                                            Log.d("IMAGE", String.valueOf(R.drawable.fridge3));

                                                            sqLiteDatabase = fridgeDBHelper.getWritableDatabase();

                                                            String str3 = "INSERT INTO refrigeratorTBL values('" +
                                                                    randomCode + "', '" +
                                                                    refName + "', '" +
                                                                    R.drawable.fridge3 + "');";

                                                            sqLiteDatabase.execSQL(str3);
                                                            sqLiteDatabase.close();

                                                            break;
                                                    }

                                                    Toast.makeText(getApplicationContext(), refName + " 추가되었습니다.", Toast.LENGTH_LONG).show();
                                                    edtTxt.setText("");
                                                }
                                            }
                                        });

                                        dialog.show();
                                        CookieBar.dismiss(activity);

                                        // dialog.setContentView(dialogView); 일때 여기까지 잘 실행됨
                                        //==========================================================================//
                                    }
                                });

                                // 기존 냉장고 불러오기
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

                final String str = ShowFoodsFragment.refrigeratorList.get(position).getName();
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
