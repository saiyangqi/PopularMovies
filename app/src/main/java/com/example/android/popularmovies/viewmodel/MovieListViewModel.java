package com.example.android.popularmovies.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.PopularMovies;
import com.example.android.popularmovies.utils.MovieDbClient;

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

    private MutableLiveData<List<Movie>> liveMovieList;

    public MutableLiveData<List<Movie>> getMovieList(String apiKey) {
        if (liveMovieList == null) {
            liveMovieList = new MutableLiveData<>();
            loadMovieList(apiKey);
        }
        return liveMovieList;
    }

    private void loadMovieList(String apiKey) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDbClient client = retrofit.create(MovieDbClient.class);
        Call<PopularMovies> call = client.getPopularMovies(apiKey);
        call.enqueue(new Callback<PopularMovies>() {
            @Override
            public void onResponse(Call<PopularMovies> call, Response<PopularMovies> response) {
                PopularMovies popularMovies = response.body();
                List<Movie> movieList = popularMovies.getMovieList();
                liveMovieList.setValue(movieList);
            }

            @Override
            public void onFailure(Call<PopularMovies> call, Throwable t) {
            }
        });
    }
}
