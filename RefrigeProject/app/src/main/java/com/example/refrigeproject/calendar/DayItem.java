package com.example.refrigeproject.calendar;

public class DayItem {
    private int dayValue;
    private boolean mark;
    private String event;

    public DayItem(int dayValue){
        this.dayValue = dayValue;
        mark = false;
        event = null;
    }

    public int getDayValue() {
        return dayValue;
    }

    public boolean isMark() {
        return mark;
    }

    public String getEvent() {
        return event;
    }

    public void setDayValue(int dayValue) {
        this.dayValue = dayValue;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
