package ru.majo.bsutimetable.presenter;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.base.BasePresenter;
import ru.majo.bsutimetable.manager.DatabaseManager;
import ru.majo.bsutimetable.ui.view.FavoriteView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Majo on 24.01.2016.
 */
public class FavoritePresenter extends BasePresenter<FavoriteView> {

    private DatabaseManager mDatabaseManager;

    private ArrayList<User> mFavoriteList;
    @Inject
    public FavoritePresenter(DatabaseManager databaseManager){
        mDatabaseManager = databaseManager;
    }

    public void onCreate() {
        mSubscription = mDatabaseManager.getFavorites()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(list ->{
                                    mFavoriteList = list;
                                    getView().setList(mFavoriteList);
                                });
    }


    public void deleteFavorite(int position){
        mDatabaseManager.deleteFavorite(mFavoriteList.get(position));
    }

}
