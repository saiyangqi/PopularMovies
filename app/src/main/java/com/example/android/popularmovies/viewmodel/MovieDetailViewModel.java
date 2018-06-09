package com.example.android.popularmovies.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.popularmovies.model.MovieVideo;
import com.example.android.popularmovies.model.MovieVideosResponse;
import com.example.android.popularmovies.utils.MovieDbClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saiyang Qi on 6/8/18.
 */
public class MovieDetailViewModel extends ViewModel{
    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private MutableLiveData<List<MovieVideo>> liveMovieVideoList;
    private MovieDbClient client;


    public MutableLiveData<List<MovieVideo>> getMovieVideoList(String apiKey, int movieId) {
        if (client == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            client = retrofit.create(MovieDbClient.class);
        }
        if (liveMovieVideoList == null) {
            liveMovieVideoList = new MutableLiveData<>();
            loadMovieVideoList(apiKey, movieId);
        }
        return liveMovieVideoList;
    }

    private void loadMovieVideoList(String apiKey, int movieId) {
        Call<MovieVideosResponse> call = client.getMovieVideos(movieId, apiKey);
        call.enqueue(new Callback<MovieVideosResponse>() {
            @Override
            public void onResponse(Call<MovieVideosResponse> call, Response<MovieVideosResponse> response) {
                MovieVideosResponse movieVideosResponse = response.body();
                if (movieVideosResponse != null) {
                    liveMovieVideoList.postValue(movieVideosResponse.getMovieVideoList());
                }
            }
            @Override
            public void onFailure(Call<MovieVideosResponse> call, Throwable t) {
            }
        });
    }

}
