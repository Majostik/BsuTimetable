package ru.majo.bsutimetable.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.majo.bsutimetable.Application;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.base.BaseFragment;
import ru.majo.bsutimetable.model.FavoriteValue;
import ru.majo.bsutimetable.presenter.PickValuePresenter;
import ru.majo.bsutimetable.ui.adapter.PickValueAdapter;
import ru.majo.bsutimetable.ui.view.PickValueView;

/**
 * Created by Majo on 24.01.2016.
 */
public class PickValueFragment extends BaseFragment implements PickValueView{

    public static final String EXTRA_TYPE = "ru.majo.bsutimetable.EXTRA_TYPE";

    @BindView(R.id.pick_value_recyclerview)
    RecyclerView mValueRecyclerView;
    @BindView(R.id.pick_edittext_search)
    EditText mSearchEditText;
    @BindView(R.id.pick_layout_search)
    LinearLayout mSearchLinearLayout;
    @BindView(R.id.pick_value_fab)
    FloatingActionButton mSearchFab;

    @Inject
    PickValuePresenter mPresenter;

    private int mType;
    private PickValueAdapter mAdapter;

    public static PickValueFragment newInstance(int type){
        PickValueFragment pickValueFragment = new PickValueFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_TYPE, type);
        pickValueFragment.setArguments(bundle);
        return pickValueFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_value, container, false);
        ButterKnife.bind(this, view);
        Application.getComponent(getActivity()).inject(this);

        mType = getArguments().getInt(EXTRA_TYPE);
        mPresenter.attachView(this);
        mPresenter.setType(mType);
        mSearchEditText.setText("");

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
        dissmissKeyboard();
    }

    @Override
    protected void initViews() {
        setToolbarTitle(getResources().getString(R.string.drawer_timetable));

        mValueRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSearchFab.attachToRecyclerView(mValueRecyclerView);
        mSearchFab.setOnClickListener(v -> {
            if (mSearchLinearLayout.isShown()) {
                animateViewUp(mSearchLinearLayout);
            } else
                animateViewDown(mSearchLinearLayout);
        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.setAfterEditText(mSearchEditText.getText().toString());
            }
        };
        mSearchEditText.removeTextChangedListener(textWatcher);
        mSearchEditText.getText().clear();
        mSearchEditText.addTextChangedListener(textWatcher);
    }


    @Override
    public void setPickedValue(ArrayList<FavoriteValue> list) {
        if (getActivity()!=null) {
            mAdapter = new PickValueAdapter(getActivity(), list, mType);
            mAdapter.setOnItemClickListener(user ->{
                getFragmentManager().beginTransaction()
                    .replace(R.id.container, TimetableFragment.newInstance(user))
                    .addToBackStack(TimetableFragment.class.getName())
                    .commit();
            });
            mValueRecyclerView.setAdapter(mAdapter);
        }
    }

    private void animateViewDown(View view){
        view.setVisibility(View.VISIBLE);
        view.setTranslationY(-100);
        view.animate().translationY(0).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
    }

    private void animateViewUp(View view){
        view.animate().translationY(-100).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
        view.setVisibility(View.GONE);
    }
}
