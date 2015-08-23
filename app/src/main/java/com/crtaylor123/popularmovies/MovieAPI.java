package com.crtaylor123.popularmovies;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface MovieAPI {
    @GET("/discover/movie")
    void getMovie(@QueryMap Map<String, String> filters, Callback<ResultsMovie> callback);

    @GET("/movie/{id}/videos")
    void getTrailer(@Path("id") int id, @Query("api_key") String key, Callback<ResultsTrailer> response);

    @GET("/movie/{id}/reviews")
    void getReview(@Path("id") int id, @Query("api_key") String key, Callback<ResultsReview> response);
}
