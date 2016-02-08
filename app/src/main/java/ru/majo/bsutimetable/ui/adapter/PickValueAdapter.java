package ru.majo.bsutimetable.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.majo.bsutimetable.Application;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.manager.DatabaseManager;
import ru.majo.bsutimetable.model.FavoriteValue;
import ru.majo.bsutimetable.model.User;

/**
 * Created by Majo on 24.01.2016.
 */
public class PickValueAdapter extends RecyclerView.Adapter<PickValueAdapter.ViewHolder> {

    @Inject
    DatabaseManager mDatabaseManager;

    private ArrayList<FavoriteValue> mFavoriteValues;
    private int mType;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        public void onItemClick(User user);
    }

    public PickValueAdapter(Context context, ArrayList<FavoriteValue> list,int type){
        mFavoriteValues = list;
        mType = type;
        Application.getComponent(context).inject(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pick_value, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mFavoriteValues.get(position).isFavorite())
            holder.mStarImage.setImageResource(R.drawable.ic_action_toggle_star);
        else
            holder.mStarImage.setImageResource(R.drawable.ic_action_toggle_star_outline);
        holder.mValueTextView.setText(mFavoriteValues.get(position).getValue());
        holder.mStarLayout.setOnClickListener(v -> {
            if (mFavoriteValues.get(position).isFavorite()) {
                mFavoriteValues.get(position).setIsFavorite(false);
                holder.mStarImage.setImageResource(R.drawable.ic_action_toggle_star_outline);
                mDatabaseManager.deleteFavorite(new User(mType, mFavoriteValues.get(position).getValue()));
            } else {
                mFavoriteValues.get(position).setIsFavorite(true);
                holder.mStarImage.setImageResource(R.drawable.ic_action_toggle_star);
                mDatabaseManager.insertFavorite(new User(mType, mFavoriteValues.get(position).getValue()));
            }
        });
        holder.mItemLayout.setOnClickListener(v ->{
            User user = new User(mType, mFavoriteValues.get(position).getValue());
            if (user.isUserATeacher())
                user.setValue(mDatabaseManager.convertNameToId(user.getValue()));
            mOnItemClickListener.onItemClick(user);
        });
    }

    @Override
    public int getItemCount() {
        return mFavoriteValues.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.pick_item_layout_star)
        LinearLayout mStarLayout;
        @Bind(R.id.pick_item_image_star)
        ImageView mStarImage;
        @Bind(R.id.pick_item_value_layout)
        LinearLayout mItemLayout;
        @Bind(R.id.pick_item_value_textview)
        TextView mValueTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
