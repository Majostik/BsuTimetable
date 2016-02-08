package ru.majo.bsutimetable.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

import ru.majo.bsutimetable.Application;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.manager.DatabaseManager;
import ru.majo.bsutimetable.manager.SharedPreferenceHelper;
import ru.majo.bsutimetable.model.Timetable;
import ru.majo.bsutimetable.model.TransmittedInfo;
import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.receiver.AlarmReceiver;
import ru.majo.bsutimetable.utils.TimetableUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Majo on 29.01.2016.
 */
public class AlarmService extends Service{

    public static final String EXTRA_TIMETABLE = "ru.majo.bsutimetable.extra.TIMETABLE";

    @Inject
    SharedPreferenceHelper mSharedPreferenceHelper;
    @Inject
    DatabaseManager mDatabaseManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Application.getComponent(this).inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra(getString(R.string.start_alarm),true))
            setAlarmNotifications();
        else
            cancelAlarmNotifications();
        return super.onStartCommand(intent, flags, startId);
    }

    private void cancelAlarmNotifications() {
        if (mSharedPreferenceHelper.isUserExist()) {
            User user = mSharedPreferenceHelper.getUser();
            TransmittedInfo transmittedInfo = new TransmittedInfo();
            transmittedInfo.setUser(user);
            if (user.isFullTimeUser()) {
                transmittedInfo.setWeek(TimetableUtils.getParity(TimetableUtils.todayDate()));
                transmittedInfo.setDay(TimetableUtils.getNumberDayOfWeek(TimetableUtils.todayDate()));
            } else {
                transmittedInfo.setDate(TimetableUtils.todayDate());
            }
            ArrayList<Timetable> timetables = mDatabaseManager.getLessons(transmittedInfo);
            for (int i=0;i<timetables.size();i++) {
                Intent notificationIntent = new Intent(this, AlarmReceiver.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_TIMETABLE, timetables.get(i));
                notificationIntent.putExtras(bundle);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                        i+timetables.get(i).getLesson().hashCode(), notificationIntent, 0);

                AlarmManager alarmManager = (AlarmManager) this.getSystemService(Service.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }
            stopSelf();
        }
    }

    private void setAlarmNotifications(){

        if (mSharedPreferenceHelper.isUserExist()) {
            User user = mSharedPreferenceHelper.getUser();
            TransmittedInfo transmittedInfo = new TransmittedInfo();
            transmittedInfo.setUser(user);
            if (user.isFullTimeUser()){
                transmittedInfo.setWeek(TimetableUtils.getParity(TimetableUtils.todayDate()));
                transmittedInfo.setDay(TimetableUtils.getNumberDayOfWeek(TimetableUtils.todayDate()));
            } else{
                transmittedInfo.setDate(TimetableUtils.todayDate());
            }
            Log.e("ALARM",transmittedInfo.toString());
            mDatabaseManager.getLessonsObservable(transmittedInfo)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(timetables -> {
                        for (int i=0;i<timetables.size();i++){
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(TimetableUtils.addToTodayDate(timetables.get(i).getTime()));
                            cal.add(Calendar.MINUTE,-5);

                            Intent notificationIntent = new Intent(this, AlarmReceiver.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(EXTRA_TIMETABLE,timetables.get(i));
                            notificationIntent.putExtras(bundle);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                                    i+timetables.get(i).getLesson().hashCode(), notificationIntent, 0);

                            AlarmManager alarmManager = (AlarmManager)this.getSystemService(Service.ALARM_SERVICE);
                            alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
                        }
                        stopSelf();
                    });
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
