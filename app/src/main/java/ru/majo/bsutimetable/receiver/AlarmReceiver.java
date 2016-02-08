package ru.majo.bsutimetable.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.majo.bsutimetable.service.NotificationService;

/**
 * Created by Majo on 29.01.2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mServiceNotification = new Intent(context, NotificationService.class);
        mServiceNotification.putExtras(intent.getExtras());
        context.startService(mServiceNotification);
    }
}
