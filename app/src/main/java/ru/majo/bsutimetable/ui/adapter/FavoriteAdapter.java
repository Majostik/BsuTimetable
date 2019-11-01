package ru.majo.bsutimetable.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.majo.bsutimetable.Application;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.manager.DatabaseManager;
import ru.majo.bsutimetable.model.User;

/**
 * Created by Majo on 24.01.2016.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{

    @Inject
    DatabaseManager mDatabaseManager;

    private ArrayList<User> mFavoriteList;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(User user);
    }

    public FavoriteAdapter(Context context, ArrayList<User> favoriteList){
        mFavoriteList = favoriteList;
        Application.getComponent(context).inject(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = mFavoriteList.get(position);
        holder.mAbbreviationTextView.setText(String.valueOf(mFavoriteList.get(position).getConvertedType().charAt(0)));
        holder.mTypeTextView.setText(mFavoriteList.get(position).getConvertedType());
        holder.mValueTextView.setText(mFavoriteList.get(position).getTitleValue());
        holder.mItemLayout.setOnClickListener(v ->{
            if (user.isUserATeacher())
                user.setValue(mDatabaseManager.convertNameToId(user.getValue()));
            mOnItemClickListener.onItemClick(user);
        });
    }

    @Override
    public int getItemCount() {
        return mFavoriteList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void notifyDelete(int position){
        mFavoriteList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.favorite_abbreviation)
        TextView mAbbreviationTextView;
        @BindView(R.id.favorite_type)
        TextView mTypeTextView;
        @BindView(R.id.favorite_value)
        TextView mValueTextView;
        @BindView(R.id.favorite_layout)
        LinearLayout mItemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
