package com.example.android.popularmovies.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.PopularMovies;
import com.example.android.popularmovies.utils.MovieDbClient;
import com.example.android.popularmovies.view.OverviewActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saiyang Qi on 5/13/18.
 */
public class MovieListViewModel extends ViewModel{
    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    private MutableLiveData<List<Movie>> livePopularMovieList;
    private MutableLiveData<List<Movie>> liveTopRatedMovieList;

    public MutableLiveData<List<Movie>> getMovieList(String apiKey, int mode) {
        if (mode == OverviewActivity.MODE_POPULAR) {
            return getPopularMovieList(apiKey, mode);
        } else {
            return getTopRatedMovieList(apiKey, mode);
        }
    }

    private MutableLiveData<List<Movie>> getPopularMovieList(String apiKey, int mode) {
        if (livePopularMovieList == null) {
            livePopularMovieList = new MutableLiveData<>();
            loadMovieList(apiKey, mode);
        }
        return livePopularMovieList;
    }

    private MutableLiveData<List<Movie>> getTopRatedMovieList(String apiKey, int mode) {
        if (liveTopRatedMovieList == null) {
            liveTopRatedMovieList = new MutableLiveData<>();
            loadMovieList(apiKey, mode);
        }
        return liveTopRatedMovieList;
    }

    private void loadMovieList(String apiKey, final int mode) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDbClient client = retrofit.create(MovieDbClient.class);
        Call<PopularMovies> call;
        if (mode == OverviewActivity.MODE_POPULAR) {
            call = client.getPopularMovies(apiKey);
        } else {
            call = client.getTopRatedMovies(apiKey);
        }

        call.enqueue(new Callback<PopularMovies>() {
            @Override
            public void onResponse(Call<PopularMovies> call, Response<PopularMovies> response) {
                PopularMovies popularMovies = response.body();
                List<Movie> movieList = popularMovies.getMovieList();
                if (mode == OverviewActivity.MODE_POPULAR) {
                    livePopularMovieList.setValue(movieList);
                } else {
                    liveTopRatedMovieList.setValue(movieList);
                }
            }

            @Override
            public void onFailure(Call<PopularMovies> call, Throwable t) {
            }
        });
    }
}
