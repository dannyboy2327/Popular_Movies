package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.popularmovies.Adapters.ReviewAdapter;
import com.example.popularmovies.Adapters.TrailerAdapter;
import com.example.popularmovies.Classes.MoviesResults;
import com.example.popularmovies.Classes.Reviews;
import com.example.popularmovies.Classes.ReviewsResults;
import com.example.popularmovies.Classes.Trailers;
import com.example.popularmovies.Classes.TrailersResults;
import com.example.popularmovies.Database.AppExecutors;
import com.example.popularmovies.Database.FavoriteMoviesDatabase;
import com.example.popularmovies.Network.ReviewsEndpoint;
import com.example.popularmovies.Network.TrailersEndpoint;
import com.example.popularmovies.databinding.ActivityDetailsBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {

    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();
    private ActivityDetailsBinding mBinding;
    private Intent infoIntent;
    private Integer mId;
    private Retrofit mRetrofit;
    private TrailersEndpoint mTrailersEndpoint;
    private Call<Trailers> mCall;
    private List<TrailersResults> mTrailersResultsList;
    private ReviewsEndpoint mReviewsEndpoint;
    private Call<Reviews> mReviewsCall;
    private List<ReviewsResults> mReviewsResults;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private MoviesResults mMoviesResults;
    private FavoriteMoviesDatabase mDb;
    private boolean isFavorite;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        mDb = FavoriteMoviesDatabase.getInstance(getApplicationContext());

        infoIntent = getIntent();

        bundle = infoIntent.getExtras();

        if (bundle != null) {
            mMoviesResults = (MoviesResults) bundle.getSerializable("movieResults");
        }
            if (infoIntent != null) {
                setDetailsContent();
                mId = mMoviesResults.getId();
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        isFavorite = mDb.FavoriteMoviesDao().isFavorite(mId);
                        if (isFavorite) {
                            mBinding.ibFavoriteButton.setImageResource(R.drawable.ic_favorite_movie);
                            mBinding.tvFavoriteLabelTextView.setText(R.string.unfavorite);
                        } else {
                            mBinding.ibFavoriteButton.setImageResource(R.drawable.ic_unfavorite_movie);
                            mBinding.tvFavoriteLabelTextView.setText(R.string.favorite);
                        }
                    }
                });
            }

        setupTrailerRecyclerView();
        setupReviewRecyclerView();
        retrofitBuilder();

        getTrailers();
        getReviews();
        setUpButton();
    }

    private void setUpButton() {
        mBinding.ibFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ImagePath = mMoviesResults.getPosterPath();
                Integer ID = mMoviesResults.getId();
                String Title = mMoviesResults.getOriginalTitle();
                Double VoteAverage = mMoviesResults.getVoteAverage();
                String OverView = mMoviesResults.getOverview();
                String DateReleased = mMoviesResults.getReleaseDate();
                boolean favorite = mMoviesResults.isFavorite();
                final MoviesResults moviesResults = new MoviesResults(ImagePath, ID, Title, VoteAverage, OverView, DateReleased, favorite);

                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (isFavorite) {
                            mDb.FavoriteMoviesDao().delete(moviesResults);
                            Log.v(LOG_TAG, "Delete Queried");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mBinding.ibFavoriteButton.setImageResource(R.drawable.ic_unfavorite_movie);
                                    mBinding.tvFavoriteLabelTextView.setText(R.string.favorite);
                                }
                            });
                        } else {
                            mDb.FavoriteMoviesDao().insert(moviesResults);
                            Log.v(LOG_TAG, "Insert Queried");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mBinding.ibFavoriteButton.setImageResource(R.drawable.ic_favorite_movie);
                                    mBinding.tvFavoriteLabelTextView.setText(R.string.unfavorite);
                                }
                            });
                        }
                        Log.v(LOG_TAG, "Delete Queried");
                        mDb.FavoriteMoviesDao().updateFavoriteMovie(mId, !isFavorite);
                    }
                });
            }
        });
    }

    private void setDetailsContent() {

        mBinding.tvTitle.setText(mMoviesResults.getOriginalTitle());

        String rate = String.valueOf(mMoviesResults.getVoteAverage()) + "/10";
        mBinding.tvRating.setText(rate);

        mBinding.tvDate.setText(mMoviesResults.getReleaseDate());
        mBinding.tvSynopsis.setText(mMoviesResults.getOverview());

        Picasso.get().load("https://image.tmdb.org/t/p/" + "w500" + mMoviesResults.getPosterPath()).into(mBinding.ivPosterImage);
    }

    private void setupTrailerRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mBinding.rvTrailerRecycler.setLayoutManager(linearLayoutManager);
        mBinding.rvTrailerRecycler.setHasFixedSize(true);
    }

    private void setupReviewRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mBinding.rvReviewRecycler.setLayoutManager(linearLayoutManager);
        mBinding.rvReviewRecycler.setHasFixedSize(true);
    }

    private void retrofitBuilder() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void getTrailers() {
        mTrailersEndpoint = mRetrofit.create(TrailersEndpoint.class);
        mCall = mTrailersEndpoint.getTrailers(mId, API_KEY);

        mCall.enqueue(new Callback<Trailers>() {
            @Override
            public void onResponse(Call<Trailers> call, Response<Trailers> response) {

                assert response.body() != null;
                mTrailersResultsList = response.body().getResults();

                if (mTrailersResultsList.size()  > 0) {
                    mBinding.rvTrailerRecycler.setVisibility(View.VISIBLE);
                    mBinding.tvLabelTrailers.setVisibility(View.VISIBLE);
                    mTrailerAdapter = new TrailerAdapter(mTrailersResultsList, new TrailerAdapter.TrailerClickListener() {
                        @Override
                        public void onTrailerClick(int clickedTrailerPosition) {
                            String trailerKey = mTrailersResultsList.get(clickedTrailerPosition).getKey();
                            Intent trailersIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://www.youtube.com/watch?v=" + trailerKey));
                            startActivity(trailersIntent);
                        }
                    });
                    mBinding.rvTrailerRecycler.setAdapter(mTrailerAdapter);
                }
            }

            @Override
            public void onFailure(Call<Trailers> call, Throwable t) {
                Toast.makeText(DetailsActivity.this,
                        "Failure to do network request",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getReviews() {
        mReviewsEndpoint = mRetrofit.create(ReviewsEndpoint.class);
        mReviewsCall = mReviewsEndpoint.getReviews(mId, API_KEY);

        mReviewsCall.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                assert response.body() != null;
                mReviewsResults = response.body().getResults();

                if (mReviewsResults.size() > 0) {
                    mBinding.tvLabelReviews.setVisibility(View.VISIBLE);
                    mBinding.rvReviewRecycler.setVisibility(View.VISIBLE);
                    mReviewAdapter = new ReviewAdapter(mReviewsResults, new ReviewAdapter.ReviewClickListener() {
                        @Override
                        public void onReviewClick(int clickedReviewPosition) {
                            Intent reviewDetailsIntent = new Intent(DetailsActivity.this, ReviewDetails.class);
                            reviewDetailsIntent.putExtra("Author", mReviewsResults.get(clickedReviewPosition).getAuthor());
                            reviewDetailsIntent.putExtra("Content", mReviewsResults.get(clickedReviewPosition).getContent());
                            reviewDetailsIntent.putExtra("Url", mReviewsResults.get(clickedReviewPosition).getUrl());
                            startActivity(reviewDetailsIntent);
                        }
                    });

                    mBinding.rvReviewRecycler.setAdapter(mReviewAdapter);
                }
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                Toast.makeText(DetailsActivity.this,
                        "Failure to do network request",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}