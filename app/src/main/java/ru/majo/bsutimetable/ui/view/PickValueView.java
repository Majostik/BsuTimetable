package ru.majo.bsutimetable.ui.view;

import java.util.ArrayList;

import ru.majo.bsutimetable.base.BaseView;
import ru.majo.bsutimetable.model.FavoriteValue;

/**
 * Created by Majo on 24.01.2016.
 */
public interface PickValueView extends BaseView {
    void setPickedValue(ArrayList<FavoriteValue> list);
}
