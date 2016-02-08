package ru.majo.bsutimetable.ui.view;

import java.util.ArrayList;

import ru.majo.bsutimetable.base.BaseView;
import ru.majo.bsutimetable.model.MonthWithDays;

/**
 * Created by Majo on 25.01.2016.
 */
public interface TimetableView extends BaseView{
    void showFullTimeTimetable();
    void showPartTimeTimetable(ArrayList<MonthWithDays> months);
}
