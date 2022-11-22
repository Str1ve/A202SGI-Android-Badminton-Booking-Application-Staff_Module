package com.example.badminton_app_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.badminton_app_admin.Model.Rating;
import com.example.badminton_app_admin.ViewHolder.RatingViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RatingList extends AppCompatActivity {
    TextView ratingName;
    TextView ratingComment;
    TextView ratingCourt;
    ImageView ratingStars;

    RecyclerView recyclerView;

    FirebaseDatabase database;
    DatabaseReference ratings;

    FirebaseRecyclerAdapter<Rating, RatingViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_list);
        getSupportActionBar().hide();

        ratingName = findViewById(R.id.rating_name);
        ratingComment = findViewById(R.id.rating_comment);
        ratingStars = findViewById(R.id.rating_image);
        ratingCourt = findViewById(R.id.rating_category);

        database = FirebaseDatabase.getInstance();
        ratings = database.getReference("Rating");

        recyclerView = findViewById(R.id.listRatings);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadRatings();
    }

    private void loadRatings() {
        adapter = new FirebaseRecyclerAdapter<Rating, RatingViewHolder>(Rating.class,R.layout.rating_layout,RatingViewHolder.class,ratings) {
            @Override
            protected void populateViewHolder(RatingViewHolder ratingViewHolder, Rating rating, int i) {
                ratingViewHolder.ratingName.setText(rating.getUserName());
                ratingViewHolder.ratingComment.setText(rating.getComment());
                ratingViewHolder.ratingCourt.setText(rating.getMenuId());

                int rateStarValue = Integer.parseInt(rating.getRateValue());

                if(rateStarValue == 1){
                    ratingViewHolder.ratingStars.setImageResource(R.drawable.onestar);
                }
                else if(rateStarValue == 2){
                    ratingViewHolder.ratingStars.setImageResource(R.drawable.twostars);
                }
                else if(rateStarValue == 3){
                    ratingViewHolder.ratingStars.setImageResource(R.drawable.threestars);
                }
                else if(rateStarValue == 4){
                    ratingViewHolder.ratingStars.setImageResource(R.drawable.fourstars);
                }
                else{
                    ratingViewHolder.ratingStars.setImageResource(R.drawable.fivestars);
                }
            }
        };
        recyclerView.setAdapter(adapter);
    }
}