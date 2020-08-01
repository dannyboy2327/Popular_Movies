package com.example.popularmovies.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.popularmovies.Classes.MoviesResults;

import java.util.List;

@Dao
public interface FavoriteMoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MoviesResults moviesResults);

    @Delete
    void delete(MoviesResults moviesResults);

    @Query("SELECT * FROM favorite_movies ORDER BY Title")
    LiveData<List<MoviesResults>> getAllMovies();

    @Query("SELECT isFavorite FROM favorite_movies WHERE Movie_ID =:id")
    boolean isFavorite(Integer id);

    @Query("UPDATE favorite_movies SET isFavorite =:isFavorite WHERE Movie_ID =:id")
    void updateFavoriteMovie(Integer id, boolean isFavorite);

    @Query("SELECT * FROM favorite_movies WHERE Movie_ID =:id")
    MoviesResults getMovie(Integer id);
}
