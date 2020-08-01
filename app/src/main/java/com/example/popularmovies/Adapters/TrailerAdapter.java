package com.example.popularmovies.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.Classes.TrailersResults;
import com.example.popularmovies.R;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<TrailersResults> mTrailersResultsList;
    private final TrailerClickListener mTrailerClickListener;

    public interface TrailerClickListener{
        void onTrailerClick(int clickedTrailerPosition);
    }

    public TrailerAdapter(List<TrailersResults> trailersResults, TrailerClickListener trailerClickListener){
        mTrailersResultsList = trailersResults;
        mTrailerClickListener = trailerClickListener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        String name = mTrailersResultsList.get(position).getName();
        holder.mTextView.setText(name);
    }

    @Override
    public int getItemCount() {
        return mTrailersResultsList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.tv_trailerName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mTrailerClickListener.onTrailerClick(position);
        }
    }
}
