package com.example.popularmovies.Network;

import com.example.popularmovies.Classes.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PopularEndpoint {

    @GET("popular")
    Call<Movies> getPopularMovies(
            @Query("api_key") String key);

    @GET("top_rated")
    Call<Movies> getTopRatedMovies(
            @Query("api_key") String key);
}
