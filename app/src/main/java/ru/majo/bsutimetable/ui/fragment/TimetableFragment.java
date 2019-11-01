package ru.majo.bsutimetable.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.majo.bsutimetable.Application;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.base.BaseFragment;
import ru.majo.bsutimetable.customview.NonSwipeableViewPager;
import ru.majo.bsutimetable.model.MonthWithDays;
import ru.majo.bsutimetable.model.TransmittedInfo;
import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.presenter.TimetablePresenter;
import ru.majo.bsutimetable.ui.adapter.ViewPagerAdapter;
import ru.majo.bsutimetable.ui.view.TimetableView;
import ru.majo.bsutimetable.utils.TimetableUtils;

/**
 * Created by Majo on 16.01.2016.
 */
public class TimetableFragment extends BaseFragment implements TimetableView {

    public static final String EXTRA_USER = "ru.majo.bsutimetable.extra.USER";

    @BindView(R.id.main_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.main_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.main_non_swipeable_viewpager)
    NonSwipeableViewPager mNonSwipeableViewPager;

    @Inject
    TimetablePresenter mTimetablePresenter;

    private User mUser;
    private ArrayList<Fragment> mFragmentArrayList;

    public static TimetableFragment newInstance(User user){
        TimetableFragment timetableFragment = new TimetableFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_USER, user);
        timetableFragment.setArguments(bundle);
        return timetableFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timetable, container, false);
        ButterKnife.bind(this, v);
        Application.getComponent(getActivity()).inject(this);

        mUser = (User)getArguments().getSerializable(EXTRA_USER);
        mTimetablePresenter.attachView(this);
        mTimetablePresenter.setUser(mUser);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTimetablePresenter.onDestroy();
    }


    private void setupViewPager(ViewPager viewPager, User user,ArrayList<MonthWithDays> list) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        int position = 0;
        for (int i=0;i<list.size();i++){
            if (list.get(i).getMonth() == TimetableUtils.getCurrentMonth()) {
                position = i;
                adapter.addFrag(PartTimeDaysFragment.newInstance(user, list.get(i).getDays(), true),
                        TimetableUtils.getMonth(list.get(i).getMonth()));
            }
            else
                adapter.addFrag(PartTimeDaysFragment.newInstance(user, list.get(i).getDays(),false),
                        TimetableUtils.getMonth(list.get(i).getMonth()));

        }
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }

    private void setupTabs(NonSwipeableViewPager viewPager, User user) {
        mFragmentArrayList = new ArrayList<>();
        int week = mTimetablePresenter.getWeek();

        mFragmentArrayList.add(FullTimeDaysFragment.newInstance(new TransmittedInfo(user, 1, week)));
        mFragmentArrayList.add(FullTimeDaysFragment.newInstance(new TransmittedInfo(user, 2, week)));

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        if (week != -1) {
            if (week == 1) {
                adapter.addFrag(mFragmentArrayList.get(0), getString(R.string.first_week_current));
                adapter.addFrag(mFragmentArrayList.get(1), getString(R.string.second_week));
            } else {
                adapter.addFrag(mFragmentArrayList.get(0), getString(R.string.first_week));
                adapter.addFrag(mFragmentArrayList.get(1), getString(R.string.second_week_current));
            }
        } else {
            if (TimetableUtils.getParity(TimetableUtils.todayDate()) == 1) {
                adapter.addFrag(mFragmentArrayList.get(0), getString(R.string.first_week_current));
                adapter.addFrag(mFragmentArrayList.get(1), getString(R.string.second_week));
            } else {
                adapter.addFrag(mFragmentArrayList.get(0), getString(R.string.first_week));
                adapter.addFrag(mFragmentArrayList.get(1), getString(R.string.second_week_current));
            }
        }
        viewPager.setAdapter(adapter);
        if (week != -1) {
            viewPager.setCurrentItem(week - 1);
        } else  {
            viewPager.setCurrentItem(TimetableUtils.getParity(TimetableUtils.todayDate()) - 1);
        }
    }

    @Override
    public void showFullTimeTimetable() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mViewPager.setVisibility(View.GONE);
        mNonSwipeableViewPager.setVisibility(View.VISIBLE);
        setupTabs(mNonSwipeableViewPager, mUser);
        mTabLayout.setupWithViewPager(mNonSwipeableViewPager);
    }

    @Override
    public void showPartTimeTimetable(ArrayList<MonthWithDays> months) {
        setupViewPager(mViewPager, mUser, months);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initViews() {
        setToolbarTitle(mUser.getTitleValue());
    }
}
