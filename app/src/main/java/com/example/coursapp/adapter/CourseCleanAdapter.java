package com.example.coursapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursapp.R;
import com.example.coursapp.model.Course;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CourseCleanAdapter extends RecyclerView.Adapter<CourseCleanAdapter.ViewHolder> {

    private final List<Course> courses;
    private OnAddClickListener onAddClickListener;

    public CourseCleanAdapter(List<Course> courses) {
        this.courses = courses;
    }

    public void setOnAddClickListener(OnAddClickListener listener) {
        this.onAddClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.tvTitle.setText(course.getTitle());
        holder.tvDescription.setText(course.getDescription());
        holder.btnLevel.setText("Уровень: " + course.getLevel());

        holder.btnAdd.setOnClickListener(v -> {
            if (onAddClickListener != null) {
                onAddClickListener.onAddClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;
        MaterialButton btnLevel, btnAdd;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvCourseTitle);
            tvDescription = itemView.findViewById(R.id.tvCourseDescription);
            btnLevel = itemView.findViewById(R.id.btnLevel);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }

    public interface OnAddClickListener {
        void onAddClick(int position);
    }
}