package ru.majo.bsutimetable.presenter;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.majo.bsutimetable.base.BasePresenter;
import ru.majo.bsutimetable.manager.DatabaseManager;
import ru.majo.bsutimetable.manager.SharedPreferenceHelper;
import ru.majo.bsutimetable.model.MonthWithDays;
import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.ui.view.TimetableView;
import ru.majo.bsutimetable.utils.TimetableUtils;

/**
 * Created by Majo on 25.01.2016.
 */
public class TimetablePresenter extends BasePresenter<TimetableView> {

    private DatabaseManager mDatabaseManager;
    private SharedPreferenceHelper mSharedPreferenceHelper;
    ArrayList<String> mList;

    @Inject
    public TimetablePresenter(DatabaseManager databaseManager, SharedPreferenceHelper sharedPreferenceHelper){
        mDatabaseManager = databaseManager;
        mSharedPreferenceHelper = sharedPreferenceHelper;
    }

    public void setUser(User user){
        if (!user.isFullTimeUser()) {
            mList = mDatabaseManager.getDatesByMonth(user);
            ArrayList<MonthWithDays> monthWithDayses = new ArrayList<MonthWithDays>();
                        for (int i=1;i<=12;i++)
                            if (TimetableUtils.createObject(i, mList)!=null)
                                monthWithDayses.add(TimetableUtils.createObject(i,mList));
            getView().showPartTimeTimetable(monthWithDayses);
        }
        else{
            getView().showFullTimeTimetable();
        }
    }

    public int getWeek() {
        return mSharedPreferenceHelper.getWeek();
    }

}
