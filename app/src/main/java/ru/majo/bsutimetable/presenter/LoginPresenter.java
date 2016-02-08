package ru.majo.bsutimetable.presenter;

import android.content.Context;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.majo.bsutimetable.base.BasePresenter;
import ru.majo.bsutimetable.manager.DatabaseManager;
import ru.majo.bsutimetable.manager.SharedPreferenceHelper;
import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.ui.view.LoginView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Majo on 23.01.2016.
 */
public class LoginPresenter extends BasePresenter<LoginView> {

    private Context mContext;
    private DatabaseManager mDatabaseManager;
    private SharedPreferenceHelper mSharedPreferenceHelper;

    private ArrayList<String> mHintList = new ArrayList<>();

    @Inject
    public LoginPresenter(Context context, DatabaseManager databaseManager, SharedPreferenceHelper sharedPreferenceHelper){
        mContext = context;
        mDatabaseManager = databaseManager;
        mSharedPreferenceHelper = sharedPreferenceHelper;
    }

    public void setType(int type){
        mSubscription = getSubscribe(type);
    }

    private Subscription getSubscribe(int type) {
        return mDatabaseManager
                .getHintList(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list ->{
                    mHintList = list;
                    getView().showHints(mHintList);
                });
    }

    public void saveValue(User user){
        if (mHintList.contains(user.getValue())) {
            if (user.isUserATeacher())
                user.setValue(mDatabaseManager.convertNameToId(user.getValue()));
            mSharedPreferenceHelper.saveValue(user);
            getView().replaceFragment(user);

        }
    }

}
