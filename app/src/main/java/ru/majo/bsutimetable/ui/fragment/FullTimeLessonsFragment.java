package ru.majo.bsutimetable.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.majo.bsutimetable.Application;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.base.BaseFragment;
import ru.majo.bsutimetable.model.Timetable;
import ru.majo.bsutimetable.model.TransmittedInfo;
import ru.majo.bsutimetable.presenter.LessonsPresenter;
import ru.majo.bsutimetable.ui.adapter.LessonsAdapter;
import ru.majo.bsutimetable.ui.view.LessonsView;

/**
 * Created by Majo on 25.01.2016.
 */
public class FullTimeLessonsFragment extends BaseFragment implements LessonsView {

    public static final String EXTRA_INFO = "ru.majo.bsutimetable.extra.INFO";

    @Bind(R.id.days_empty_textview)
    TextView mEmptyTextView;
    @Bind(R.id.days_recylcerview)
    RecyclerView mRecyclerView;

    @Inject
    LessonsPresenter mPresenter;

    public static FullTimeLessonsFragment newInstance(TransmittedInfo transmittedInfo){
        FullTimeLessonsFragment fullTimeFullTimeLessonsFragment = new FullTimeLessonsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_INFO, transmittedInfo);
        fullTimeFullTimeLessonsFragment.setArguments(bundle);
        return fullTimeFullTimeLessonsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_time_lessons, container, false);
        ButterKnife.bind(this, view);
        Application.getComponent(getActivity()).inject(this);
        mPresenter.attachView(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
    }

    @Override
    protected void initViews() {
        mPresenter.setTransmittedInfo((TransmittedInfo)getArguments().getSerializable(EXTRA_INFO));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void showLessons(ArrayList<Timetable> list) {
            mRecyclerView.setAdapter(new LessonsAdapter(getActivity(), list));
    }

    @Override
    public void showEmpty() {
        mEmptyTextView.setVisibility(View.VISIBLE);
    }
}
