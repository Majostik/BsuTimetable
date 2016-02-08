package ru.majo.bsutimetable.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Majo on 28.01.2016.
 */
public class TimetableWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TimetableWidgetFactory(this);
    }
}
