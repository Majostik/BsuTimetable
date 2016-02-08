package ru.majo.bsutimetable.service;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.majo.bsutimetable.Application;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.manager.DatabaseManager;
import ru.majo.bsutimetable.manager.SharedPreferenceHelper;
import ru.majo.bsutimetable.model.Timetable;
import ru.majo.bsutimetable.model.TransmittedInfo;
import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.provider.TimetableWidgetProvider;
import ru.majo.bsutimetable.utils.TimetableUtils;


/**
 * Created by Majo on 28.01.2016.
 */
public class TimetableWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    @Inject
    DatabaseManager mDatabaseManager;
    @Inject
    SharedPreferenceHelper mSharedPreferenceHelper;
    private Context mContext;
    private User mUser;

    private ArrayList<Timetable> mTimetables = new ArrayList<>();
    private String mDate;
    private int mDay;
    private int mWeek;

    public TimetableWidgetFactory(Context context){
        Application.getComponent(context).inject(this);
        mContext = context;
        initFields();
    }

    @Override
    public void onCreate() {
        TimetableWidgetProvider.updateNote(mContext);
    }

    @Override
    public void onDataSetChanged() {
        initFields();
        TransmittedInfo transmittedInfo = new TransmittedInfo();
        transmittedInfo.setUser(mUser);
        if (mUser.isFullTimeUser()){
            transmittedInfo.setWeek(mWeek);
            transmittedInfo.setDay(mDay);
        } else
            transmittedInfo.setDate(mDate);
        mTimetables = mDatabaseManager.getLessons(transmittedInfo);
    }

    private void initFields() {
        mUser = mSharedPreferenceHelper.getUser();
        if (mUser.isFullTimeUser()){
            mWeek = TimetableUtils.getParity(mSharedPreferenceHelper.getDateForWidget());
            mDay = TimetableUtils.getNumberDayOfWeek(mSharedPreferenceHelper.getDateForWidget());
        }else{
            mDate = mSharedPreferenceHelper.getDateForWidget();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mTimetables.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rView = new RemoteViews(mContext.getPackageName(), R.layout.item_timetable);
        rView.setTextViewText(R.id.timetable_item_type_textview,mTimetables.get(position).getLessonType());
        rView.setTextViewText(R.id.timetable_item_auditory,mTimetables.get(position).getAuditory());
        rView.setTextViewText(R.id.timetable_item_discipline,mTimetables.get(position).getLesson());
        rView.setTextViewText(R.id.timetable_item_teacher,mTimetables.get(position).getType());
        rView.setTextViewText(R.id.timetable_item_time, mTimetables.get(position).getTime());
        return rView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
