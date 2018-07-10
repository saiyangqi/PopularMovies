package com.example.android.popularmovies.model.api;

import com.example.android.popularmovies.model.MovieReview;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Saiyang Qi on 6/25/18.
 */
public class MovieReviewsResponse {
    private int id;
    @SerializedName("results")
    private List<MovieReview> movieReviewList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieReview> getMovieReviewlist() {
        return movieReviewList;
    }

    public void setMovieReviewlist(List<MovieReview> movieReviewList) {
        this.movieReviewList = movieReviewList;
    }
}
