package com.axis.helloastropartner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.userText.setText("User: " + review.getUserId());
        holder.commentText.setText(review.getComment());
        holder.ratingBar.setRating((float) review.getRating());

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView userText, commentText;
        RatingBar ratingBar;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            userText = itemView.findViewById(R.id.userName);
            commentText = itemView.findViewById(R.id.comment);
            ratingBar = itemView.findViewById(R.id.ratingBarSmall);
        }
    }
}
