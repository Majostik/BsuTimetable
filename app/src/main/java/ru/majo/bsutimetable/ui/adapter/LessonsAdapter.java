package ru.majo.bsutimetable.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.majo.bsutimetable.R;
import ru.majo.bsutimetable.model.Timetable;

/**
 * Created by Majo on 25.01.2016.
 */
public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Timetable> mTimetables;

    public LessonsAdapter(Context context, ArrayList<Timetable> timetables) {
        mContext = context;
        mTimetables = timetables;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timetable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mLessonTypeTextView.setText(mTimetables.get(position).getLessonType());
        holder.mDisciplineTextView.setText(mTimetables.get(position).getLesson());
        holder.mTeacherTextView.setText(mTimetables.get(position).getType());
        holder.mTimeTextView.setText(mTimetables.get(position).getTime());
        holder.mAuditoryTextView.setText(mTimetables.get(position).getAuditory());
        setType(mTimetables.get(position).getLessonType(), holder);
    }

    @Override
    public int getItemCount() {
        return mTimetables.size();
    }

    private void setType(String type, LessonsAdapter.ViewHolder vh){
        if (type.equals(mContext.getString(R.string.lesson_type_lb)))
            vh.mTypeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_circle));
        else if (type.equals(mContext.getString(R.string.lesson_type_lc)))
            vh.mTypeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_circle_purple));
        else if (type.equals(mContext.getString(R.string.lesson_type_pr)))
            vh.mTypeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_circle_yellow));
        else if (type.equals(mContext.getString(R.string.lesson_type_of)))
            vh.mTypeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_circle_green));
        else if (type.equals(mContext.getString(R.string.lesson_type_ex)))
            vh.mTypeImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_circle_orange));

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.timetable_item_auditory)
        TextView mAuditoryTextView;
        @BindView(R.id.timetable_item_discipline)
        TextView mDisciplineTextView;
        @BindView(R.id.timetable_item_teacher)
        TextView mTeacherTextView;
        @BindView(R.id.timetable_item_time)
        TextView mTimeTextView;
        @BindView(R.id.timetable_item_type_textview)
        TextView mLessonTypeTextView;
        @BindView(R.id.timetable_item_type_image)
        ImageView mTypeImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
