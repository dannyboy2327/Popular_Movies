package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.popularmovies.Adapters.PosterAdapter;
import com.example.popularmovies.Classes.Movies;
import com.example.popularmovies.Classes.MoviesResults;
import com.example.popularmovies.Network.PopularEndpoint;
import com.example.popularmovies.ViewModel.MainViewModel;
import com.example.popularmovies.databinding.ActivityMainBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private PopularEndpoint popularEndpoint;
    private List<MoviesResults> mMoviesResultsList;
    private Call<Movies> mCall;
    private static final String API_KEY = BuildConfig.API_KEY;
    private PosterAdapter mPosterAdapter;
    private Retrofit mRetrofit;
    private ActivityMainBinding mBinding;

    private boolean isPopular = true;
    private boolean isTopRated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        checkNetwork();

        if (savedInstanceState == null) {
            mCall = popularEndpoint.getPopularMovies(API_KEY);
            getMovies();
        } else if (savedInstanceState != null) {
            isPopular = savedInstanceState.getBoolean("Popular");
            isTopRated = savedInstanceState.getBoolean("TopRated");
            if (isPopular && !isTopRated) {
                mBinding.pbProgress.setVisibility(View.VISIBLE);
                mCall = popularEndpoint.getPopularMovies(API_KEY);
                getMovies();
            } else if (!isPopular && isTopRated) {
                mBinding.pbProgress.setVisibility(View.VISIBLE);
                mCall = popularEndpoint.getTopRatedMovies(API_KEY);
                getMovies();
            } else if (!isPopular && !isTopRated) {
                getFavoriteMovies();
            }
        }

    }

    private void checkNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null) {
            mBinding.pbProgress.setVisibility(View.VISIBLE);
            mBinding.tvNoInternet.setVisibility(View.INVISIBLE);

            setupRecyclerView();
            retrofitBuilder();
            popularEndpoint = mRetrofit.create(PopularEndpoint.class);
        } else {
            mBinding.tvNoInternet.setVisibility(View.VISIBLE);
        }
    }

    private void retrofitBuilder() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mBinding.rvLayout.setLayoutManager(gridLayoutManager);
        mBinding.rvLayout.setHasFixedSize(true);
    }

    private void getMovies() {

        mCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {

                assert response.body() != null;
                mMoviesResultsList = response.body().getResults();
                mPosterAdapter = new PosterAdapter(mMoviesResultsList, new PosterAdapter.PosterClickListener() {
                    @Override
                    public void onPosterClick(MoviesResults moviesResults) {
                        Intent detailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("movieResults", moviesResults);
                        detailsIntent.putExtras(bundle);
                        startActivity(detailsIntent);
                    }
                });

                mBinding.pbProgress.setVisibility(View.INVISIBLE);
                mBinding.rvLayout.setAdapter(mPosterAdapter);
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        "Failure to do network request",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int menu_id = item.getItemId();

        switch (menu_id) {
            case R.id.m_popular:
                isPopular = true;
                isTopRated = false;
                mBinding.pbProgress.setVisibility(View.VISIBLE);
                mCall = popularEndpoint.getPopularMovies(API_KEY);
                getMovies();
                break;
            case R.id.m_topRated:
                isPopular = false;
                isTopRated = true;
                mBinding.pbProgress.setVisibility(View.VISIBLE);
                mCall = popularEndpoint.getTopRatedMovies(API_KEY);
                getMovies();
                break;
            case R.id.m_favorite:
                isPopular = false;
                isTopRated = false;
                getFavoriteMovies();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getFavoriteMovies() {
        final MainViewModel mainViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MainViewModel.class);
        mainViewModel.getMovieResults().observe(this, new Observer<List<MoviesResults>>() {
            @Override
            public void onChanged(List<MoviesResults> moviesResultsList) {
                Log.v(LOG_TAG, "Was Queried");
                if (moviesResultsList != null) {
                    mPosterAdapter.setFavorites(moviesResultsList);
                    Log.v(LOG_TAG, "Ran2");

                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("Popular", isPopular);
        outState.putBoolean("TopRated", isTopRated);
    }
}