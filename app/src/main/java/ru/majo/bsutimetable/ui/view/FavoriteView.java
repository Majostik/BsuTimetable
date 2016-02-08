package ru.majo.bsutimetable.ui.view;

import java.util.ArrayList;

import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.base.BaseView;

/**
 * Created by Majo on 24.01.2016.
 */
public interface FavoriteView extends BaseView{
    void setList(ArrayList<User> list);
}
