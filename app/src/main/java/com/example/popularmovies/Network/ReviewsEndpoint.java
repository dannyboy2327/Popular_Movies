package com.example.popularmovies.Network;

import com.example.popularmovies.Classes.Reviews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewsEndpoint {

    @GET("{movie_id}/reviews")
    Call<Reviews> getReviews(
            @Path("movie_id") Integer Id,
            @Query("api_key") String key);
}
