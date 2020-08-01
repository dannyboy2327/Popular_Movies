package com.example.popularmovies.Network;

import com.example.popularmovies.Classes.Trailers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrailersEndpoint {

    @GET("{movie_id}/videos")
    Call<Trailers> getTrailers(
            @Path("movie_id") Integer ID,
            @Query("api_key") String key);
}
