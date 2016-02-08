package ru.majo.bsutimetable.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.base.BaseFragment;
import ru.majo.bsutimetable.ui.adapter.PickTypeAdapter;

/**
 * Created by Majo on 24.01.2015.
 */
public class PickTypeFragment extends BaseFragment {

    @Bind(R.id.pick_type_recyclerview)
    RecyclerView mRecyclerView;

    private PickTypeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_pick_type,null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initViews() {
        setToolbarTitle(getResources().getString(R.string.drawer_timetable));
        mAdapter = new PickTypeAdapter(getActivity());
        mAdapter.setOnItemClickListener(position ->
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.container, PickValueFragment.newInstance(position))
                        .addToBackStack(PickValueFragment.class.getName())
                        .commit());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }
}
