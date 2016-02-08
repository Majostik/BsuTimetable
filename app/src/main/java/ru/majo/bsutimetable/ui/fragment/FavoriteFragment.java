package ru.majo.bsutimetable.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.majo.bsutimetable.Application;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.base.BaseFragment;
import ru.majo.bsutimetable.model.User;
import ru.majo.bsutimetable.presenter.FavoritePresenter;
import ru.majo.bsutimetable.ui.adapter.FavoriteAdapter;
import ru.majo.bsutimetable.ui.view.FavoriteView;

/**
 * Created by Majo on 24.01.2016.
 */
public class FavoriteFragment extends BaseFragment implements FavoriteView{

    @Bind(R.id.favorite_recylcerview)
    RecyclerView mFavoriteRecyclerView;

    @Inject
    FavoritePresenter mFavoritePresenter;


    private FavoriteAdapter mFavoriteAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        Application.getComponent(getActivity()).inject(this);
        ButterKnife.bind(this, view);
        mFavoritePresenter.attachView(this);
        mFavoritePresenter.onCreate();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mFavoritePresenter.onDestroy();
    }

    @Override
    protected void initViews(){
        setToolbarTitle(getResources().getString(R.string.drawer_favorites));
        mFavoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void setList(ArrayList<User> list) {
        mFavoriteAdapter = new FavoriteAdapter(getActivity(), list);
        mFavoriteAdapter.setOnItemClickListener(user ->
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, TimetableFragment.newInstance(user))
                        .commit());
        mFavoriteRecyclerView.setAdapter(mFavoriteAdapter);
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(android.support.v7.widget.RecyclerView recyclerView, android.support.v7.widget.RecyclerView.ViewHolder viewHolder, android.support.v7.widget.RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mFavoritePresenter.deleteFavorite(viewHolder.getAdapterPosition());
                mFavoriteAdapter.notifyDelete(viewHolder.getAdapterPosition());
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(mFavoriteRecyclerView);
    }


}
