package ru.majo.bsutimetable.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.majo.bsutimetable.Application;
import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.base.BaseFragment;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.presenter.LoginPresenter;
import ru.majo.bsutimetable.provider.TimetableWidgetProvider;
import ru.majo.bsutimetable.ui.MainActivity;
import ru.majo.bsutimetable.ui.view.LoginView;
import ru.majo.bsutimetable.utils.TimetableUtils;

/**
 * Created by Majo on 23.01.2016.
 */
public class LoginFragment extends BaseFragment implements LoginView{

    public static final String EXTRA_FROM_SETTINGS = "ru.majo.bsutimetable.extra.FROM_SETTINGSG";

    @Bind(R.id.login_spinner)
    Spinner mSpinner;
    @Bind(R.id.login_autotextview)
    AutoCompleteTextView mAutoCompleteTextView;

    @Inject
    LoginPresenter mLoginPresenter;


    public static LoginFragment newInstance(boolean isFromSettings){
        LoginFragment loginFragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_FROM_SETTINGS,isFromSettings);
        loginFragment.setArguments(bundle);
        return loginFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        Application.getComponent(getActivity()).inject(this);
        mLoginPresenter.attachView(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLoginPresenter.onDestroy();
        dissmissKeyboard();
    }

    @Override
    protected void initViews(){
        mSpinner.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                TimetableUtils.getTimetableArrayForLogin(getActivity())));
        mSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        mLoginPresenter.setType(position);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
    }

    @Override
    public void showHints(ArrayList<String> list) {
        mAutoCompleteTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list));
    }

    @Override
    public void replaceFragment(User user) {
        TimetableWidgetProvider.updateWidget(getActivity());
        ((MainActivity)getActivity()).setNavigationHeader(user);
        if (getArguments().getBoolean(EXTRA_FROM_SETTINGS)) {
            getFragmentManager().popBackStack();
            getFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment()).commit();
        }
        else
            getFragmentManager().beginTransaction().replace(R.id.container, TimetableFragment.newInstance(user)).commit();
    }

    @OnClick(R.id.login_save_button)
    public void onClick(){
        mLoginPresenter.saveValue(new User(mSpinner.getSelectedItemPosition(), mAutoCompleteTextView.getText().toString()));
    }
}
