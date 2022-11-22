package com.example.badminton_app_admin.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badminton_app_admin.R;

public class RatingViewHolder extends RecyclerView.ViewHolder {

    public TextView ratingName;
    public TextView ratingComment;
    public TextView ratingCourt;
    public ImageView ratingStars;

    public RatingViewHolder(@NonNull View itemView) {
        super(itemView);
        ratingName = itemView.findViewById(R.id.rating_name);
        ratingComment = itemView.findViewById(R.id.rating_comment);
        ratingStars = itemView.findViewById(R.id.rating_image);
        ratingCourt = itemView.findViewById(R.id.rating_category);
    }
}
