package ru.majo.bsutimetable.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.utils.TimetableUtils;

/**
 * Created by Majo on 24.01.2015.
 */
public class PickTypeAdapter extends RecyclerView.Adapter<PickTypeAdapter.ViewHolder>{

    private Context mContext;
    private int mLastPosition = -1;

    private String[] mList;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public PickTypeAdapter(Context context){
        mContext = context;
        mList = TimetableUtils.getTimetableArray(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pick_type,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mValueTextView.setText(mList[position]);
        holder.mAbbreviationTextView.setText(String.valueOf(mList[position].charAt(0)));
        holder.mLinearLayout.setOnClickListener(v -> mOnItemClickListener.onItemClicked(position));
        setAnimation(holder.mLinearLayout, position);
    }

    @Override
    public int getItemCount() {
        return mList.length;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > mLastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
            animation.setDuration((position+1)*100);
            viewToAnimate.startAnimation(animation);
            mLastPosition = position;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.pick_item_type_abbreviation)
        TextView mAbbreviationTextView;
        @BindView(R.id.pick_item_type_value)
        TextView mValueTextView;
        @BindView(R.id.pick_item_type_layout)
        LinearLayout mLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
