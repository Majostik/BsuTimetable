package ru.majo.bsutimetable.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import javax.inject.Inject;

import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.utils.TimetableUtils;

/**
 * Created by Majo on 23.01.2016.
 */
public class SharedPreferenceHelper {

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private DatabaseManager mDatabaseManager;

    @Inject
    public SharedPreferenceHelper(Context context,DatabaseManager databaseManager){
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(context.getString(R.string.preference_name),Context.MODE_PRIVATE);
        mDatabaseManager = databaseManager;
    }

    public boolean isFirstTime(){
        if (mSharedPreferences.getBoolean(mContext.getString(R.string.preference_firsttime), false) == false){
            mSharedPreferences.edit().putBoolean(mContext.getString(R.string.preference_firsttime), true).commit();
            return true;
        } else
            return false;
    }

    public void saveValue(User user){
        mSharedPreferences.edit().putInt(mContext.getString(R.string.preference_user_type),user.getType()).commit();
        mSharedPreferences.edit().putString(mContext.getString(R.string.preference_user_value),user.getValue()).commit();
    }

    public boolean isUserExist(){
        if (mSharedPreferences.getString(mContext.getString(R.string.preference_user_value),null)!=null){
            return true;
        } else
            return false;
    }

    public User getUser(){
        User user = new User(mSharedPreferences.getInt(mContext.getString(R.string.preference_user_type),0)
                ,mSharedPreferences.getString(mContext.getString(R.string.preference_user_value),new String()));
        if (user.isUserATeacher())
            user.setTitleValue(mDatabaseManager.convertIdToName(user.getValue()));
        return user;
    }

    public void setDateForWidget(String dateForWidget){
        mSharedPreferences.edit().putString(mContext.getString(R.string.preference_widget_date),dateForWidget).commit();
    }

    public String getDateForWidget(){
        if (mSharedPreferences.getString(mContext.getString(R.string.preference_widget_date),null)!=null)
            return mSharedPreferences.getString(mContext.getString(R.string.preference_widget_date),null);
        else
            return TimetableUtils.todayDate();
    }

    public boolean isNotificationEnabled(){
        return mSharedPreferences.getBoolean(mContext.getString(R.string.preference_notification), false);
    }

    public void setNotificationEnable(boolean notificationEnable) {
        mSharedPreferences.edit().putBoolean(mContext.getString(R.string.preference_notification),notificationEnable).commit();
    }

    public boolean isAutomaticEnabled(){
        return mSharedPreferences.getBoolean(mContext.getString(R.string.preference_update_enable), false);
    }

    public void setAutomaticEnable(boolean automaticEnable) {
        mSharedPreferences.edit().putBoolean(mContext.getString(R.string.preference_update_enable),automaticEnable).commit();
    }

    public void setAutomaticUpdateDate(String date){
        mSharedPreferences.edit().putString(mContext.getString(R.string.preference_update_date), date).commit();
    }

    public String getAutomaticUpdateDate(){
        Log.e("AUTO",mSharedPreferences.getString(mContext.getString(R.string.preference_update_date),
                mContext.getString(R.string.preference_update_default_date)));
        return mSharedPreferences.getString(mContext.getString(R.string.preference_update_date),
                mContext.getString(R.string.preference_update_default_date));
    }

    public void saveWeek(int week){
        mSharedPreferences.edit().putInt(mContext.getString(R.string.preference_user_week), week).commit();
    }

    public int getWeek() {
        return mSharedPreferences.getInt(mContext.getString(R.string.preference_user_week), -1);
    }
}
