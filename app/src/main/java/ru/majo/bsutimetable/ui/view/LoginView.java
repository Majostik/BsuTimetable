package ru.majo.bsutimetable.ui.view;

import java.util.ArrayList;

import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.base.BaseView;

/**
 * Created by Majo on 23.01.2016.
 */
public interface LoginView extends BaseView {
    void showHints(ArrayList<String> list);
    void replaceFragment(User user);
}
