package in.ac.medicaps.medicapsstudentsapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import in.ac.medicaps.medicapsstudentsapp.R;
import in.ac.medicaps.medicapsstudentsapp.itemClickListener;

public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView courseNameTextView, courseCodeTextView;
    public CheckBox courseCheckBox;
    public itemClickListener listener;

    public CourseViewHolder(@NonNull View itemView) {
        super(itemView);
        courseNameTextView = (TextView)itemView.findViewById(R.id.course_name);
        courseCodeTextView = (TextView)itemView.findViewById(R.id.course_code);
        courseCheckBox = (CheckBox)itemView.findViewById(R.id.course_check_box);
    }

    public void setItemClickListener(itemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
