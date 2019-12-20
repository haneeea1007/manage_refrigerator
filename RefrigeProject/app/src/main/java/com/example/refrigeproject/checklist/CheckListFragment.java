package com.example.refrigeproject.checklist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.refrigeproject.R;

import java.util.ArrayList;

public class CheckListFragment extends Fragment {

    View view;
    private RecyclerView recyclerView;
    private EditText edtText;
    private ImageButton btnAdd;
    private ConstraintLayout empty_text;

    private ArrayList<CheckListData> list = new ArrayList<CheckListData>();
    private LinearLayoutManager linearLayoutManager;
    private CheckListAdapter checkListAdapter;
    private CheckListData checkListData;

    private CheckListDBHelper checkListDBHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_check_list, null, false);

        checkListDBHelper = new CheckListDBHelper(getContext());

        recyclerView = view.findViewById(R.id.recyclerView);
        btnAdd = view.findViewById(R.id.btnRefAdd);
        edtText = view.findViewById(R.id.edtText);
        empty_text = view.findViewById(R.id.empty_text);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        checkListAdapter = new CheckListAdapter(R.layout.fragment_check_list_item, list);
        recyclerView.setAdapter(checkListAdapter);

        DBSelect();

        // 추가 버튼을 클릭하면 data class 에 텍스트를 전달함
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // DB 추가
                sqLiteDatabase = checkListDBHelper.getWritableDatabase();      // 데이터 쓰기 기능 받기
                String str = "INSERT INTO checkListTBL values('" +          // sql 만들기
                        edtText.getText().toString().trim() + "', " +
                        "'false'" + ");";

                list.add(new CheckListData(edtText.getText().toString().trim(), "false"));
                checkListAdapter.notifyDataSetChanged();
                edtText.setText("");

                sqLiteDatabase.execSQL(str);                            // sql 실행
                sqLiteDatabase.close();                                 // 데이터베이스 닫기

                DBSelect();
            }
        });

        return view;

    } // end of onCreateView()

    public void DBSelect() {

        // DB 조회
        list.clear();
        recyclerView.removeAllViews();

        checkListDBHelper = new CheckListDBHelper(getContext());
        sqLiteDatabase = checkListDBHelper.getReadableDatabase();

        Cursor cursor;
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM checkListTBL;", null);

        while (cursor.moveToNext()) {
            checkListData = new CheckListData(cursor.getString(0), cursor.getString(1));
            list.add(checkListData);
        }

        checkListAdapter.notifyDataSetChanged();

        cursor.close();
        sqLiteDatabase.close();
    }

    //================================================================================================================================================//

    public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.CustomViewHolder> {

        private Context context;
        private int layout;
        private ArrayList<CheckListData> list;

        private CheckListDBHelper checkListDBHelper;
        private SQLiteDatabase sqLiteDatabase;

        public CheckBox checkBox;
        public ImageButton btnTrash;
        public TextView checkText;

        //=============================================================================================//

        public CheckListAdapter(int layout, ArrayList<CheckListData> list) {
            this.layout = layout;
            this.list = list;
        }

        //=============================================================================================//

        @RequiresApi(api = Build.VERSION_CODES.N)
        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);

            context = viewGroup.getContext();
            CustomViewHolder viewHolder = new CustomViewHolder(view);

            return viewHolder;
        }

        //=============================================================================================//

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onBindViewHolder(@NonNull final CustomViewHolder customViewHolder, final int position) {

            // 셋팅하기
            checkBox.setOnCheckedChangeListener(null);

            checkText.setText(list.get(position).getCheckItem());
            checkBox.setChecked(Boolean.parseBoolean(list.get(position).getCheckChecked()));
            customViewHolder.itemView.setTag(position);

              // 초기화
//            sqLiteDatabase = checkListDBHelper.getWritableDatabase();    // 데이터 쓰기 기능 받기
//            checkListDBHelper.onUpgrade(sqLiteDatabase, 1, 2);     // 초기화 (삭제 후 다시 생성)
//            sqLiteDatabase.close();                               // 데이터베이스 닫기

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                    // 체크박스가 변하면 그 위치의 아이템의 체크박스를 바꿔서 셋팅
                    list.get(position).setCheckChecked(String.valueOf(isChecked));

//                    Toast.makeText(context, list.get(position).getCheckItem() + " : " + list.get(position).getCheckChecked(), Toast.LENGTH_LONG).show();

                   // 체크박스가 변하면 체크박스 DB 업데이트
                   // DB 수정
                    checkListDBHelper = new CheckListDBHelper(context);
                    sqLiteDatabase = checkListDBHelper.getWritableDatabase();      // 데이터 쓰기 기능 받기

                    String str = "UPDATE checkListTBL SET checked ='"
                            + isChecked + "' WHERE text ='"
                            + list.get(position).getCheckItem() + "';";

                    sqLiteDatabase.execSQL(str);                            // sql 실행
                    sqLiteDatabase.close();                                 // 데이터베이스 닫기
                }
            });

            // 삭제 버튼을 눌렀을 때 이벤트
            btnTrash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                    final CheckListData selectedItem = list.get(position);
                    dialog.setTitle(selectedItem.getCheckItem() + " 삭제 확인");
                    dialog.setMessage("정말 삭제하시겠습니까?");

                    dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            checkListDBHelper = new CheckListDBHelper(context);
                            sqLiteDatabase = checkListDBHelper.getWritableDatabase();      // 데이터 쓰기 기능 받기
                            sqLiteDatabase.execSQL("DELETE FROM checkListTBL WHERE text = '"
                                    + selectedItem.getCheckItem() + "';");

                            list.remove(selectedItem);
                            recyclerView.removeAllViews();
                            notifyDataSetChanged();
                            sqLiteDatabase.close();                                         // 데이터베이스 닫기

                            Toast.makeText(view.getContext(), selectedItem.getCheckItem() + " 삭제가 완료되었습니다.", Toast.LENGTH_LONG).show();
                        }
                    });

                    dialog.setNegativeButton("취소", null);
                    dialog.show();
                }
            });
        }

        //=============================================================================================//

        @Override
        public int getItemCount() {

            int count = list.size();

            if(count == 0){
                empty_text.setVisibility(View.VISIBLE);
            }else {
                empty_text.setVisibility(View.GONE);
            }

            return count;
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.checkBox);
                checkText = itemView.findViewById(R.id.checkText);
                btnTrash = itemView.findViewById(R.id.btnTrash);
            }
        }
    }
}


