package ru.majo.bsutimetable.ui.view;

import ru.majo.bsutimetable.base.BaseView;
import ru.majo.bsutimetable.model.User;

/**
 * Created by Majo on 27.01.2016.
 */
public interface MainView extends BaseView {
    void showLoginFragment();
    void showMainFragment(User user);
    void setNavigationHeader(User user);
    void onStartProgress();
    void onFinishFirstTime();
    void onFinishProgress();
    void showUpdateDialog();
    void showErrorToast(String errorText);
}
