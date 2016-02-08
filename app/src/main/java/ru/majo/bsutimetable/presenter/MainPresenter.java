package ru.majo.bsutimetable.presenter;

import android.content.Context;

import javax.inject.Inject;

import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.base.BasePresenter;
import ru.majo.bsutimetable.manager.DatabaseManager;
import ru.majo.bsutimetable.manager.NetworkManager;
import ru.majo.bsutimetable.manager.SharedPreferenceHelper;
import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.ui.view.MainView;
import ru.majo.bsutimetable.utils.TimetableUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Majo on 27.01.2016.
 */
public class MainPresenter extends BasePresenter<MainView> {

    private SharedPreferenceHelper mSharedPreferenceHelper;
    private DatabaseManager mDatabaseManager;
    private NetworkManager mNetworkManager;
    private Context mContext;

    @Inject
    public MainPresenter(Context context,SharedPreferenceHelper sharedPreferenceHelper,
                         DatabaseManager databaseManager, NetworkManager networkManager) {
        mContext = context;
        mSharedPreferenceHelper = sharedPreferenceHelper;
        mDatabaseManager = databaseManager;
        mNetworkManager = networkManager;
    }

    public void setNavView(){
        if (mSharedPreferenceHelper.isUserExist()) {
            User user = mSharedPreferenceHelper.getUser();
            getView().setNavigationHeader(user);
        }
    }

    public void onInitFirstFragment(){
        if (mSharedPreferenceHelper.isUserExist()){
            User user = mSharedPreferenceHelper.getUser();
            if (user.isUserATeacher())
                user.setTitleValue(mDatabaseManager.convertIdToName(user.getValue()));
            getView().showMainFragment(user);
        } else
            getView().showLoginFragment();
    }

    public void onWriteDatabase(){
        if (mSharedPreferenceHelper.isFirstTime()) {
            getView().onStartProgress();
            mSubscription = mDatabaseManager
                    .writeDatabase()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(res -> getView().onFinishProgress());
        }
    }

    public void isTodayToUpdate(){
        if (mSharedPreferenceHelper.isAutomaticEnabled()){
            if (TimetableUtils.isWeekDifference(mSharedPreferenceHelper.getAutomaticUpdateDate()))
                getView().showUpdateDialog();
        }
    }

    public void startUpdate(){
        if (mNetworkManager.checkInternet()) {
            getView().onStartProgress();
            mNetworkManager.donwloadFullDatabase()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(aBoolean -> {
                        mSharedPreferenceHelper.setAutomaticUpdateDate(TimetableUtils.todayDate());
                        getView().onFinishProgress();
                    },throwable -> {
                        mDatabaseManager.writeDatabase()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.newThread())
                                .subscribe(bool -> {
                                    getView().onFinishProgress();
                                    getView().showErrorToast(mContext.getString(R.string.download_error));
                                });
                    });
        } else
            getView().showErrorToast(mContext.getString(R.string.internet_error));
    }

}
