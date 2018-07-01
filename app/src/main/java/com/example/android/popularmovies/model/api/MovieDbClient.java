package com.example.android.popularmovies.model.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Saiyang Qi on 5/6/18.
 */
public interface MovieDbClient {
    @GET("movie/popular")
    Call<PopularMoviesResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") String pageNum);

    @GET("movie/top_rated")
    Call<PopularMoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") String pageNum);

    @GET("movie/{movie_id}/videos")
    Call<MovieVideosResponse> getMovieVideos(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<MovieReviewsResponse> getMovieReviews(@Path("movie_id") int movieId, @Query("api_key") String apiKey);
}
