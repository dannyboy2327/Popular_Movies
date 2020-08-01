package com.example.popularmovies.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.Classes.ReviewsResults;
import com.example.popularmovies.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewsResults> mReviewsResults;
    private ReviewClickListener mReviewClickListener;

    public interface ReviewClickListener{
        void onReviewClick(int clickedReviewPosition);
    }

    public ReviewAdapter(List<ReviewsResults> reviewsResults, ReviewClickListener reviewClickListener) {
        mReviewsResults = reviewsResults;
        mReviewClickListener = reviewClickListener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        String content = mReviewsResults.get(position).getAuthor();
        holder.mReviewTextView.setText(content);
    }

    @Override
    public int getItemCount() {
        return mReviewsResults.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mReviewTextView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            mReviewTextView = itemView.findViewById(R.id.tv_review);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mReviewClickListener.onReviewClick(position);
        }
    }
}
