package com.example.popularmovies.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.popularmovies.Classes.MoviesResults;

@Database(entities = {MoviesResults.class}, version = 1, exportSchema = false)
public abstract class FavoriteMoviesDatabase extends RoomDatabase {

    public abstract FavoriteMoviesDao FavoriteMoviesDao();
    private static FavoriteMoviesDatabase sInstance;

    public static  FavoriteMoviesDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (FavoriteMoviesDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            FavoriteMoviesDatabase.class,
                            "Favorite_Movies_Database")
                            .build();
                }
            }
        }

        return sInstance;
    }
}
