package com.example.android.popularmovies.utils;

import com.example.android.popularmovies.model.PopularMovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Saiyang Qi on 5/6/18.
 */
public interface MovieDbClient {
    @GET("movie/popular")
    Call<PopularMovies> getPopularMovies(@Query("api_key") String apiKey);
}
