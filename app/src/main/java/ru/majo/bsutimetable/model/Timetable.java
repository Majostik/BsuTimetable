package ru.majo.bsutimetable.model;

import java.io.Serializable;

/**
 * Created by Majo on 16.01.2016.
 */
public class Timetable implements Serializable{
    private String mTime;
    private String mAuditory;
    private String mLessonType;
    private String mType;
    private String mLesson;

    public Timetable(String time, String auditory, String lessonType, String type, String lesson) {
        mTime = time;
        mAuditory = auditory;
        mLessonType = lessonType;
        mType = type;
        mLesson = lesson;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getTime() {
        return mTime;
    }

    public String getAuditory() {
        return mAuditory;
    }

    public String getLessonType() {
        return mLessonType;
    }

    public String getType() {
        return mType;
    }

    public String getLesson() {
        return mLesson;
    }
}
