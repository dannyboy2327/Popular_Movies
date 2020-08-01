package com.example.popularmovies.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.popularmovies.Classes.MoviesResults;
import com.example.popularmovies.Database.FavoriteMoviesDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<MoviesResults>> movieResults;



    public MainViewModel(@NonNull Application application) {
        super(application);

        FavoriteMoviesDatabase mDb = FavoriteMoviesDatabase.getInstance(this.getApplication());
        movieResults = mDb.FavoriteMoviesDao().getAllMovies();
    }

    public LiveData<List<MoviesResults>> getMovieResults() {
        return movieResults;
    }
}
