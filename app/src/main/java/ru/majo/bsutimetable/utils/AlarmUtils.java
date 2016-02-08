package ru.majo.bsutimetable.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.service.AlarmService;

/**
 * Created by Majo on 05.02.2016.
 */
public class AlarmUtils {
    public static void startAlarmService(Context context){
        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra(context.getString(R.string.start_alarm),true);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        if(Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent);
    }

    public static void stopAlarmService(Context context){
        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra(context.getString(R.string.start_alarm),false);
        context.startService(intent);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}
