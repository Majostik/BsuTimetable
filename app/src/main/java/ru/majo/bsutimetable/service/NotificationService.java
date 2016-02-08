package ru.majo.bsutimetable.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.model.Timetable;
import ru.majo.bsutimetable.ui.MainActivity;

/**
 * Created by Majo on 29.01.2016.
 */
public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timetable timetable = (Timetable)intent.getExtras().getSerializable(AlarmService.EXTRA_TIMETABLE);
        NotificationManager nm = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(String.format("%s %s %s", timetable.getTime(), timetable.getLessonType(), timetable.getAuditory()))
                        .setContentText(String.format("%s %s", timetable.getLesson(), timetable.getType()))
                        .setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true);

        Intent mNotificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        mNotificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent mContentIntent = PendingIntent.getActivity(getApplicationContext(), timetable.getLesson().hashCode(), mNotificationIntent , 0);
        builder.setContentIntent(mContentIntent);
        nm.notify(timetable.getLesson().hashCode(), builder.build());
    }
}
