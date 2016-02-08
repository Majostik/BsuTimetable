package ru.majo.bsutimetable.model;

import java.io.Serializable;

import ru.majo.bsutimetable.utils.TimetableUtils;

/**
 * Created by Majo on 24.01.2016.
 */
public class User implements Serializable{
    private int mType;
    private String mValue;
    private String mConvertedType;
    private String mTitleValue;

    public User(int type, String value) {
        mType = type;
        mValue = value;
        mTitleValue = value;
        mConvertedType = TimetableUtils.convertType(mType);
    }

    public String getTitleValue() {
        return mTitleValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public int getType() {
        return mType;
    }

    public String getValue() {
        return mValue;
    }

    public void setTitleValue(String titleValue) {
        mTitleValue = titleValue;
    }

    public String getConvertedType() {
        return mConvertedType;
    }

    public boolean isFullTimeUser(){
        if (mType<2)
            return true;
        else
            return false;
    }

    public boolean isUserATeacher(){
        if (mType==1 || mType==3)
            return true;
        else
            return false;
    }
}
