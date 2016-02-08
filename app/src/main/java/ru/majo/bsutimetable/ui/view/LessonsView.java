package ru.majo.bsutimetable.ui.view;

import java.util.ArrayList;

import ru.majo.bsutimetable.model.Timetable;
import ru.majo.bsutimetable.base.BaseView;

/**
 * Created by Majo on 16.01.2016.
 */
public interface LessonsView extends BaseView {
    void showLessons(ArrayList<Timetable> list);
    void showEmpty();
}
