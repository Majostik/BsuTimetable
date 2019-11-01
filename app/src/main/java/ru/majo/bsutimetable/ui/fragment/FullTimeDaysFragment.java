package ru.majo.bsutimetable.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.base.BaseFragment;
import ru.majo.bsutimetable.model.TransmittedInfo;
import ru.majo.bsutimetable.ui.adapter.ViewPagerAdapter;
import ru.majo.bsutimetable.utils.TimetableUtils;

/**
 * Created by Majo on 25.01.2016.
 */
public class FullTimeDaysFragment extends BaseFragment{

    public static final String EXTRA_INFO = "ru.majo.bsutimetable.extra.INFO";

    @BindView(R.id.days_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.days_viewpager)
    ViewPager mViewPager;

    private TransmittedInfo mTransmittedInfo;



    public static FullTimeDaysFragment newInstance(TransmittedInfo transmittedInfo){
        FullTimeDaysFragment fullTimeDaysFragment = new FullTimeDaysFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_INFO, transmittedInfo);
        fullTimeDaysFragment.setArguments(bundle);
        return fullTimeDaysFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_time_days, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initViews() {
        mTransmittedInfo = (TransmittedInfo)getArguments().getSerializable(EXTRA_INFO);

        setupViewPager(mViewPager, mTransmittedInfo);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
        setCurrent(mViewPager, mTransmittedInfo);
    }

    private void setCurrent(ViewPager viewPager,TransmittedInfo transmittedInfo){
        if ((transmittedInfo.getSelectWeek() == -1 &&
                transmittedInfo.getWeek() == TimetableUtils.getParity(TimetableUtils.todayDate())) ||
                (transmittedInfo.getSelectWeek() == transmittedInfo.getWeek()) &&
                TimetableUtils.todayDay()!=7)
            viewPager.setCurrentItem(TimetableUtils.todayDay()-1);
    }

    private void setupViewPager(ViewPager viewPager,TransmittedInfo transmittedInfo) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (int i=0;i<6;i++){
            adapter.addFrag(FullTimeLessonsFragment.newInstance(new TransmittedInfo(
                    transmittedInfo.getUser(),
                    transmittedInfo.getWeek(),
                    i + 1,
                    transmittedInfo.getSelectWeek())), TimetableUtils.getShortDay(i));
        }
        viewPager.setAdapter(adapter);
        //viewPager.setOffscreenPageLimit(5);
    }
}
