package ru.majo.bsutimetable.presenter;

import javax.inject.Inject;

import ru.majo.bsutimetable.base.BasePresenter;
import ru.majo.bsutimetable.manager.DatabaseManager;
import ru.majo.bsutimetable.model.TransmittedInfo;
import ru.majo.bsutimetable.ui.view.LessonsView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Majo on 16.01.2016.
 */
public class LessonsPresenter extends BasePresenter<LessonsView> {

    private DatabaseManager mDatabaseManager;

    @Inject
    public LessonsPresenter(DatabaseManager databaseManager) {
        mDatabaseManager = databaseManager;
    }

    public void setTransmittedInfo(TransmittedInfo transmittedInfo){
        mDatabaseManager.getLessonsObservable(transmittedInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(timetables -> {
                    if (!timetables.isEmpty())
                        getView().showLessons(timetables);
                    else
                        getView().showEmpty();
                });
    }


}
