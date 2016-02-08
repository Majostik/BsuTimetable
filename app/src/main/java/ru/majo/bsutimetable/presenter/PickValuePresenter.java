package ru.majo.bsutimetable.presenter;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.majo.bsutimetable.base.BasePresenter;
import ru.majo.bsutimetable.manager.DatabaseManager;
import ru.majo.bsutimetable.model.FavoriteValue;
import ru.majo.bsutimetable.ui.view.PickValueView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Majo on 24.01.2016.
 */
public class PickValuePresenter extends BasePresenter<PickValueView> {

    private DatabaseManager mDatabaseManager;

    private ArrayList<FavoriteValue> mList = new ArrayList<>();

    @Inject
    public PickValuePresenter(DatabaseManager databaseManager){
        mDatabaseManager = databaseManager;
    }

    public void setType(int type) {
        mSubscription = mDatabaseManager.getFavoritesByType(type)
                .flatMap(strings -> mDatabaseManager.createItems(type, strings))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    mList = list;
                    getView().setPickedValue(mList);
                });
    }

    public void setAfterEditText(String text){
        ArrayList<FavoriteValue> sortList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            if (text.length() <= mList.get(i).getValue().length()) {
                if(mList.get(i).getValue().toLowerCase().
                        contains(text.toLowerCase().trim())) {
                    sortList.add(mList.get(i));
                }
            }
        }
        getView().setPickedValue(sortList);
    }

}
