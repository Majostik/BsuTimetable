package ru.majo.bsutimetable.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import javax.inject.Inject;

import ru.majo.bsutimetable.Application;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.manager.SharedPreferenceHelper;
import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.service.TimetableWidgetService;
import ru.majo.bsutimetable.ui.MainActivity;
import ru.majo.bsutimetable.utils.TimetableUtils;

/**
 * Created by Majo on 28.01.2016.
 */
public class TimetableWidgetProvider extends AppWidgetProvider{

    private static final String ACTION_PREVIOUS_CLICK = "ru.majo.bsutimetable.action.PREVIOUS_CLICK";
    private static final String ACTION_NEXT_CLICK = "ru.majo.bsutimetable.action.NEXT_CLICK";
    private static final String ACTION_TODAY_CLICK = "ru.majo.bsutimetable.action.TODAY_CLICK";
    private static final String ACTION_UPDATE_NOTE = "ru.majo.bsutimetable.action.UPDATE_NOTE";
    private static final String ACTION_UPDATE = "ru.majo.bsutimetable.action.UPDATE";
    private static final String ACTION_HEADER_CLICK = "ru.majo.bsutimetable.action.HEADER_CLICK";

    private static final String EXTRA_DATE = "ru.majo.bsutimetable.extra.DATE";

    @Inject
    SharedPreferenceHelper mSharedPreferenceHelper;

    public RemoteViews getRemoteViews(Context context, int id, String date) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_timetable);
        Application.getComponent(context).inject(this);
        User user = mSharedPreferenceHelper.getUser();
        mSharedPreferenceHelper.setDateForWidget(date);
        remoteViews.setTextViewText(R.id.value_widget, user.getTitleValue());
        if (user.getType()<2){
            if (TimetableUtils.getDayOfWeek(date) == TimetableUtils.getDayOfWeek(TimetableUtils.todayDate())
                    && TimetableUtils.getParity(date) == TimetableUtils.getParity(TimetableUtils.todayDate()))
                remoteViews.setTextViewText(R.id.day_widget, String.format(context.getString(R.string.today),
                        TimetableUtils.getDayOfWeek(date)));
            else
                remoteViews.setTextViewText(R.id.day_widget, TimetableUtils.getDayOfWeek(date));
            remoteViews.setTextViewText(R.id.date_widget, TimetableUtils.getParity(date)+" неделя");
        } else{
            if (TimetableUtils.todayDate().equals(date))
                remoteViews.setTextViewText(R.id.day_widget, String.format(context.getString(R.string.today),
                        TimetableUtils.getDayOfWeek(date)));
            else
                remoteViews.setTextViewText(R.id.day_widget, TimetableUtils.getDayOfWeek(date));

            remoteViews.setTextViewText(R.id.date_widget, TimetableUtils.getDayAndMonth(date));
        }
        setUpPreviousButton(context,remoteViews,id,date);
        setUpNextButton(context, remoteViews, id, date);
        setUpTodayButton(context, remoteViews, id);
        setUpAdapter(context, remoteViews, id, date);
        setUpHeaderClick(context,remoteViews,id);
        return remoteViews;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        setRemotes(context, appWidgetManager, appWidgetIds);
    }

    private void setRemotes(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            RemoteViews remoteViews = getRemoteViews(context, id, TimetableUtils.todayDate());
            if (remoteViews != null) {
                appWidgetManager.updateAppWidget(id, remoteViews);
            }
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_widget);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        String date = intent.getStringExtra(EXTRA_DATE);
        switch (intent.getAction()){
            case ACTION_PREVIOUS_CLICK:
                RemoteViews remoteViews = getRemoteViews(context,id,date);
                if (remoteViews != null) {
                    mSharedPreferenceHelper.setDateForWidget(date);
                    appWidgetManager.updateAppWidget(id, remoteViews);
                }
                break;
            case ACTION_NEXT_CLICK:
                RemoteViews remoteViews1 = getRemoteViews(context,id,date);
                if (remoteViews1 != null) {
                    appWidgetManager.updateAppWidget(id, remoteViews1);
                }
                break;
            case ACTION_TODAY_CLICK:
                RemoteViews remoteViews2 = getRemoteViews(context,id,date);
                if (remoteViews2 != null) {
                    mSharedPreferenceHelper.setDateForWidget(date);
                    appWidgetManager.updateAppWidget(id, remoteViews2);
                }
                break;
            case ACTION_UPDATE:
                int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
                setRemotes(context,AppWidgetManager.getInstance(context),appWidgetIds);
                break;
            case ACTION_UPDATE_NOTE:
                appWidgetManager.notifyAppWidgetViewDataChanged(
                        intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS),
                        R.id.list_widget);
                break;
            case ACTION_HEADER_CLICK:
                Intent i = new Intent(context,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
        }
        super.onReceive(context, intent);
    }

    private void setUpHeaderClick(Context context, RemoteViews remoteViews, int id) {
        Intent intent = new Intent(context, TimetableWidgetProvider.class);
        intent.setAction(ACTION_HEADER_CLICK);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,id);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.header_layout, clickPendingIntent);
    }

    public static void updateWidget(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, TimetableWidgetProvider.class));
        Intent intent = new Intent(context, TimetableWidgetProvider.class);
        intent.setAction(ACTION_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        context.sendBroadcast(intent);
    }

    public static void updateNote(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, TimetableWidgetProvider.class));
        Intent intent = new Intent(context, TimetableWidgetProvider.class);
        intent.setAction(ACTION_UPDATE_NOTE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        context.sendBroadcast(intent);
    }

    private void setUpAdapter(Context context, RemoteViews remoteViews, int id,String date) {
        Intent intent = new Intent(context, TimetableWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        mSharedPreferenceHelper.setDateForWidget(date);
        remoteViews.setRemoteAdapter(R.id.list_widget, intent);
        remoteViews.setEmptyView(R.id.list_widget,R.id.empty_view);
        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(id,
                R.id.list_widget);
    }

    private void setUpPreviousButton(Context context, RemoteViews remoteViews, int id, String date){
        Intent clickIntent = new Intent(context, TimetableWidgetProvider.class);
        clickIntent.setAction(ACTION_PREVIOUS_CLICK);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        clickIntent.putExtra(EXTRA_DATE, TimetableUtils.changeDateByDay(date, -1));
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context,
                0,
                clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.previous_button, clickPendingIntent);
    }

    private void setUpNextButton(Context context, RemoteViews remoteViews, int id, String date){
        Intent clickIntent = new Intent(context, TimetableWidgetProvider.class);
        clickIntent.setAction(ACTION_NEXT_CLICK);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        clickIntent.putExtra(EXTRA_DATE, TimetableUtils.changeDateByDay(date, 1));
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context,
                0,
                clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.next_button, clickPendingIntent);
    }

    private void setUpTodayButton(Context context, RemoteViews remoteViews, int id){
        Intent clickIntent = new Intent(context, TimetableWidgetProvider.class);
        clickIntent.setAction(ACTION_TODAY_CLICK);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        clickIntent.putExtra(EXTRA_DATE, TimetableUtils.todayDate());
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context,
                0,
                clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.today_button, clickPendingIntent);
    }
}
