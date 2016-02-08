package ru.majo.bsutimetable.model;

import java.util.ArrayList;

/**
 * Created by Majo on 26.01.2016.
 */
public class MonthWithDays {
    private int mMonth;
    private ArrayList<String> mDays;

    public MonthWithDays(int month, ArrayList<String> days) {
        mDays = days;
        mMonth = month;
    }

    public int getMonth() {
        return mMonth;
    }

    public ArrayList<String> getDays() {
        return mDays;
    }
}
