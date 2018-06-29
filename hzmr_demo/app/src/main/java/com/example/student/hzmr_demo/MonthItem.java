package com.example.student.hzmr_demo;

public class MonthItem {
    private int dayValue;
    private String state;
    public MonthItem(int day, String state) {
        dayValue = day;
        this.state = state;
    }
    public int getDay() {
        return dayValue;
    }
    public void setDay(int day) {
        this.dayValue = day;
    }
    public String getState(){
        return state;
    }
}