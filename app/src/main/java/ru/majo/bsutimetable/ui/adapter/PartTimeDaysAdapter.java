package ru.majo.bsutimetable.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.utils.TimetableUtils;

/**
 * Created by Majo on 26.01.2016.
 */
public class PartTimeDaysAdapter extends RecyclerView.Adapter<PartTimeDaysAdapter.ViewHolder> {

    private ArrayList<String> mDays;
    private boolean mIsCurrentMonth;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        public void onItemClick(String date);
    }

    public PartTimeDaysAdapter(ArrayList<String> days, boolean isCurrentMonth){
        mDays = days;
        mIsCurrentMonth = isCurrentMonth;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timetable_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mAbbreviationTextView.setText(String.valueOf(TimetableUtils.getDayNumber(mDays.get(position))));
        holder.mDecriptionTextView.setText(TimetableUtils.getDayOfWeek(mDays.get(position)));
        if (TimetableUtils.isTodayDate(mDays.get(position)) && mIsCurrentMonth){
            holder.mTodayTextView.setVisibility(View.VISIBLE);
        } else
            holder.mTodayTextView.setVisibility(View.INVISIBLE);
        holder.mDayLayout.setOnClickListener(v -> mOnItemClickListener.onItemClick(mDays.get(position)));
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.day_layout)
        LinearLayout mDayLayout;
        @BindView(R.id.day_abbreviation_textview)
        TextView mAbbreviationTextView;
        @BindView(R.id.day_decription_textview)
        TextView mDecriptionTextView;
        @BindView(R.id.day_today_textview)
        TextView mTodayTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
