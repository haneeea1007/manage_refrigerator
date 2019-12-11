package com.example.refrigeproject.calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.refrigeproject.R;

import java.util.Calendar;

public class CalendarAdapter extends BaseAdapter {
    private final static String TAG = "CalendarAdapter";
    private final static int TOTAL_COUNT = 6 * 7;
    public Context context;
    public DayItem[] items;

    public Calendar mCalendar; // 설정할 달력 객체
    public int mStartDay; // 달력 체제 - Calendar.SUNDAY
    public int startDay; // 달력 체제 - Time.SUNDAY

    public int currentYear; // 현재 년도
    public int currentMonth; // 현재 월
    public int firstDay; // 현재 달 첫 날의 요일
    public int lastDay; // 현재 달 마지막 날의 날짜

    public int selectedPosition = -1; // ????????????

//    SQLiteDatabase sqLiteDatabase;

    LayoutInflater layoutInflater;
    View view;

    public CalendarAdapter(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        items = new DayItem[TOTAL_COUNT];
        mCalendar = Calendar.getInstance();

        recalculate();
        resetDayNumbers();
    }

    @Override
    public int getCount() {
        return TOTAL_COUNT;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.month_item, null);

        ImageView ivMark = view.findViewById(R.id.ivMark);
        TextView tvDayValue = view.findViewById(R.id.tvDayValue);
        TextView tvEvent = view.findViewById(R.id.tvEvent);

        int day = items[position].getDayValue();
        if(day != 0) tvDayValue.setText(String.valueOf(day));
        int columnIndex = position % 7;
        switch (columnIndex){
            case 0:
                tvDayValue.setTextColor(Color.RED);
                break;
            case 6:
                tvDayValue.setTextColor(Color.BLUE);
                break;
            default: tvDayValue.setTextColor(Color.BLACK);
                break;
        }

        String event = null;
        Log.d("12/20", items[19].isMark()+"");


//            tvEvent.setText(items[position].getEvent());
        // DB에서 가져오기 /////////////////////////////////////////////////////
//        dbHelper = new DBHelper(context);
//        sqLiteDatabase = dbHelper.getReadableDatabase();
//        Cursor cursor; // record set
//        cursor = sqLiteDatabase.rawQuery("SELECT event FROM calendarTBL WHERE year = " + currentYear +
//                " AND month = " + (currentMonth + 1) +
//                " AND day = " + day +
//                ";", null);
//
//
//        Log.d("selectAllItems", "1");
//
//        while (cursor.getCount() != 0 && cursor.moveToNext()) {
//            event = cursor.getString(0);
//            ivMark.setImageResource(R.drawable.circle);
//            tvEvent.setText(event);
//            //tvEvent.append(event);
//        }
//
//        cursor.close();
//        sqLiteDatabase.close();

        ////////////////////////////////////////////////////////////////////////////

        return view;
    }

    private void resetDayNumbers() {
        for(int i = 0; i < TOTAL_COUNT ; i++){
            int dayNumber = (i + 1) - firstDay;

            if(dayNumber < 1 || dayNumber > lastDay){
                dayNumber = 0;
            }

            items[i] = new DayItem(dayNumber);
        }
    }

    // 이전달 버튼
    public void setPreviousMonth() {
        // 현재가 11월 이라면 10월로 변경!
        mCalendar.add(Calendar.MONTH, -1);
        recalculate();
        resetDayNumbers();
        // 아직 분석안됐따...
        selectedPosition = -1;
    }

    // 다음달 버튼
    public void setNextMonth() {
        // 현재가 11월 이라면 12월로 변경!
        mCalendar.add(Calendar.MONTH, 1);
        recalculate();
        resetDayNumbers();
        // 아직 분석안됐따...
        selectedPosition = -1;
    }

    private void recalculate() {
        mCalendar.set(Calendar.DAY_OF_MONTH, 1); // DAY_OF_MONTH(날짜)를 1일로 세팅

        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK); // 1일이 무슨 요일인지 가져옴
        firstDay = getFirstDay(dayOfWeek); // 해당 요일을 달력 체에 따라 몇 번째 칸에 넣을지 결정

        mStartDay = mCalendar.getFirstDayOfWeek(); // 달력 체제
        currentYear = mCalendar.get(Calendar.YEAR);
        currentMonth = mCalendar.get(Calendar.MONTH);
        lastDay = getLastDayOfMonth(currentYear, currentMonth);
        startDay = getFirstDayOfWeek();
        resetDayNumbers();

    }

    private int getLastDayOfMonth(int currentYear, int currentMonth) {
        switch (currentMonth + 1) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;

            case 4: case 6: case 9: case 11:
                return 30;

            default:
                if ((currentYear % 4 == 0) && (currentYear % 100 != 0) || (currentYear % 400 == 0)) {
                    return 29; // leap year
                } else {
                    return 28;
                }
        }
    }

    private int getFirstDayOfWeek() {
        int startDay = Calendar.getInstance().getFirstDayOfWeek();
        switch (startDay){
            case Calendar.SATURDAY: return Time.SATURDAY;
            case Calendar.SUNDAY: return Time.SUNDAY;
            case Calendar.MONDAY: return Time.MONDAY;
        }
        return -1;
    }

    private int getFirstDay(int dayOfWeek) {
        switch (dayOfWeek){
            case Calendar.SUNDAY: return 0;
            case Calendar.MONDAY: return 1;
            case Calendar.TUESDAY: return 2;
            case Calendar.WEDNESDAY: return 3;
            case Calendar.THURSDAY: return 4;
            case Calendar.FRIDAY: return 5;
            case Calendar.SATURDAY: return 6;
        }
        return -1;
    }
}
