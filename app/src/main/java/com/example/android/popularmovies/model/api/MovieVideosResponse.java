package com.example.android.popularmovies.model.api;

import com.example.android.popularmovies.model.MovieVideo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Saiyang Qi on 6/8/18.
 */
public class MovieVideosResponse {
    private int id;
    @SerializedName("results")
    private List<MovieVideo> movieVideoList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieVideo> getMovieVideoList() {
        return movieVideoList;
    }

    public void setMovieVideoList(List<MovieVideo> movieVideoList) {
        this.movieVideoList = movieVideoList;
    }
}
