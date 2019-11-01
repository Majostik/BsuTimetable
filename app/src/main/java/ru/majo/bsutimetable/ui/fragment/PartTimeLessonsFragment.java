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

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.majo.bsutimetable.Application;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.base.BaseFragment;
import ru.majo.bsutimetable.model.Timetable;
import ru.majo.bsutimetable.model.TransmittedInfo;
import ru.majo.bsutimetable.presenter.LessonsPresenter;
import ru.majo.bsutimetable.ui.adapter.LessonsAdapter;
import ru.majo.bsutimetable.ui.view.LessonsView;
import ru.majo.bsutimetable.utils.TimetableUtils;

/**
 * Created by Majo on 26.01.2016.
 */
public class PartTimeLessonsFragment extends BaseFragment implements LessonsView{

    public static final String EXTRA_INFO = "ru.majo.bsutimetable.extra.INFO";

    @BindView(R.id.lessons_empty_textview)
    TextView mEmptyTextView;
    @BindView(R.id.lessons_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.lessons_day_textview)
    TextView mDayTextView;
    @BindView(R.id.lessons_day_of_week_textview)
    TextView mDayOfWeekTextView;

    @Inject
    LessonsPresenter mPresenter;

    private TransmittedInfo mTransmittedInfo;

    public static PartTimeLessonsFragment newInstance(TransmittedInfo transmittedInfo) {
        PartTimeLessonsFragment fragment = new PartTimeLessonsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_INFO,transmittedInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_part_time_lessons, container, false);
        ButterKnife.bind(this, view);
        Application.getComponent(getActivity()).inject(this);

        mTransmittedInfo = (TransmittedInfo)getArguments().getSerializable(EXTRA_INFO);
        mPresenter.attachView(this);
        mPresenter.setTransmittedInfo(mTransmittedInfo);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
    }

    @Override
    protected void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDayTextView.setText(TimetableUtils.getDayAndMonth(mTransmittedInfo.getDate()));
        mDayOfWeekTextView.setText(TimetableUtils.getFullDayOfWeek(mTransmittedInfo.getDate()));
    }

    @Override
    public void showLessons(ArrayList<Timetable> list) {
        mRecyclerView.setAdapter(new LessonsAdapter(getActivity(),list));
    }

    @Override
    public void showEmpty() {
        mEmptyTextView.setVisibility(View.VISIBLE);
    }
}
