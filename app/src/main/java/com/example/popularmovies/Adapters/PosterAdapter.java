package com.example.popularmovies.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.Classes.MoviesResults;
import com.example.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.ImageViewHolder> {

    private static final String LOG_TAG = PosterAdapter.class.getSimpleName();

    private List<MoviesResults> mMoviesResultsList;

    private final PosterClickListener mPosterClickListener;

    public interface PosterClickListener {
        void onPosterClick(MoviesResults moviesResults);
    }

    public PosterAdapter(List<MoviesResults> moviesResultsList, PosterClickListener posterClickListener) {
        mMoviesResultsList = moviesResultsList;
        mPosterClickListener = posterClickListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poster_list_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        MoviesResults moviesResults = this.mMoviesResultsList.get(position);
        Picasso.get().load("https://image.tmdb.org/t/p/" + "w500" + moviesResults.getPosterPath())
                .into(holder.mImageView);

        Log.v(LOG_TAG, moviesResults.getPosterPath());
    }

    @Override
    public int getItemCount() {
        return mMoviesResultsList.size();
    }

    public void setFavorites(List<MoviesResults> moviesResults){
        mMoviesResultsList = moviesResults;
        notifyDataSetChanged();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.iv_posters);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            MoviesResults moviesResults = mMoviesResultsList.get(position);
            mPosterClickListener.onPosterClick(moviesResults);
        }
    }
}
