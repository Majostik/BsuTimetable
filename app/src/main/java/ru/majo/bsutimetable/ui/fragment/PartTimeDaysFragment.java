package ru.majo.bsutimetable.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.base.BaseFragment;
import ru.majo.bsutimetable.model.TransmittedInfo;
import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.ui.adapter.PartTimeDaysAdapter;

/**
 * Created by Majo on 25.01.2016.
 */
public class PartTimeDaysFragment extends BaseFragment {

    public static final String EXTRA_USER = "ru.majo.bsutimetable.extra.USER";
    public static final String EXTRA_DAYS = "ru.majo.bsutimetable.extra.DAYS";
    public static final String EXTRA_CURRENT = "ru.majo.bsutimetable.extra.CURRENT";


    @Bind(R.id.days_recyclerview)
    RecyclerView mRecyclerView;

    private PartTimeDaysAdapter mAdapter;

    public static PartTimeDaysFragment newInstance(User user, ArrayList<String> days, boolean isCurrent){
        PartTimeDaysFragment partTimeDaysFragment = new PartTimeDaysFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_USER,user);
        bundle.putStringArrayList(EXTRA_DAYS, days);
        bundle.putBoolean(EXTRA_CURRENT, isCurrent);
        partTimeDaysFragment.setArguments(bundle);
        return partTimeDaysFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_part_time_days, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PartTimeDaysAdapter(getArguments().getStringArrayList(EXTRA_DAYS),
                getArguments().getBoolean(EXTRA_CURRENT));
        mAdapter.setOnItemClickListener(date -> {
            TransmittedInfo transmittedInfo = new TransmittedInfo((User)getArguments().getSerializable(EXTRA_USER),date);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PartTimeLessonsFragment.newInstance(transmittedInfo))
                    .addToBackStack(PartTimeLessonsFragment.class.getName())
                    .commit();
        });
        mRecyclerView.setAdapter(mAdapter);
    }
}
