package ru.majo.bsutimetable.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import javax.inject.Inject;

import ru.majo.bsutimetable.Application;
import ru.majo.bsutimetable.manager.SharedPreferenceHelper;
import ru.majo.bsutimetable.service.AlarmService;

/**
 * Created by Majo on 29.01.2016.
 */
public class BootReceiver extends BroadcastReceiver {

    @Inject
    SharedPreferenceHelper mSharedPreferenceHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        Application.getComponent(context).inject(this);
        if (mSharedPreferenceHelper.isNotificationEnabled())
            startAlarmService(context);
    }

    public void startAlarmService(Context context){
        Intent intent = new Intent(context , AlarmService.class);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        if(Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
    }
}
