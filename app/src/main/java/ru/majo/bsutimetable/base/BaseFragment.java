package ru.majo.bsutimetable.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import ru.majo.bsutimetable.ui.MainActivity;

/**
 * Created by Majo on 17.01.2016.
 */
public abstract class BaseFragment extends Fragment {


    protected abstract void initViews();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    public void setToolbarTitle(String title){
        ((MainActivity)getActivity()).getToolbarTitle().setText(title);
    }


    public void dissmissKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }
}
