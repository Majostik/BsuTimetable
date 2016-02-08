package ru.majo.bsutimetable.model;

import java.io.Serializable;

/**
 * Created by Majo on 16.01.2016.
 */
public class TransmittedInfo implements Serializable{

    private User mUser;
    private int mDay;
    private int mWeek;
    private String mDate;

    public TransmittedInfo(){

    }

    public TransmittedInfo(User user, int week) {
        mUser = user;
        mWeek = week;
    }

    public TransmittedInfo(User user, int week, int day) {
        mUser = user;
        mDay = day;
        mWeek = week;
    }

    public TransmittedInfo(User user, String date){
        mUser = user;
        mDate = date;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public int getDay() {
        return mDay;
    }

    public void setDay(int day) {
        mDay = day;
    }

    public int getWeek() {
        return mWeek;
    }

    public void setWeek(int week) {
        mWeek = week;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    @Override
    public String toString() {
        return "TransmittedInfo{" +
                "mUser=" + mUser.getType() + " "+ mUser.getValue() +
                ", mDay=" + mDay +
                ", mWeek=" + mWeek +
                ", mDate='" + mDate + '\'' +
                '}';
    }
}
