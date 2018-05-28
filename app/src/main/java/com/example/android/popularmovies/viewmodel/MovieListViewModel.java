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
    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    private MutableLiveData<List<Movie>> livePopularMovieList;
    private MutableLiveData<List<Movie>> liveTopRatedMovieList;
    private int popularPageNum = 0;
    private int topRatedPageNum = 0;

    private MovieDbClient client;

    public MutableLiveData<List<Movie>> getMovieList(String apiKey, int mode) {
        if (client == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            client = retrofit.create(MovieDbClient.class);
        }
        if (mode == OverviewActivity.MODE_POPULAR) {
            return getPopularMovieList(apiKey, mode);
        } else {
            return getTopRatedMovieList(apiKey, mode);
        }
    }

    public void updateMovieList(String apiKey, int mode) {
        loadMovieList(apiKey, mode);
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
        Call<PopularMovies> call;
        if (mode == OverviewActivity.MODE_POPULAR) {
            popularPageNum++;
            call = client.getPopularMovies(apiKey, Integer.toString(popularPageNum));
        } else {
            topRatedPageNum++;
            call = client.getTopRatedMovies(apiKey, Integer.toString(topRatedPageNum));
        }

        call.enqueue(new Callback<PopularMovies>() {
            @Override
            public void onResponse(Call<PopularMovies> call, Response<PopularMovies> response) {
                PopularMovies popularMovies = response.body();
                List<Movie> movieList = null;
                if (popularMovies != null) {
                    movieList = popularMovies.getMovieList();
                    if (popularMovies.getTotalPages() < popularMovies.getPage()) {
                        return;
                    }
                }
                if (movieList != null) {
                    if (mode == OverviewActivity.MODE_POPULAR) {
                        if (livePopularMovieList.getValue() != null) {
                            livePopularMovieList.getValue().addAll(movieList);
                            livePopularMovieList.postValue(livePopularMovieList.getValue());
                        } else {
                            livePopularMovieList.postValue(movieList);
                        }
                    } else {
                        if (liveTopRatedMovieList.getValue() != null) {
                            liveTopRatedMovieList.getValue().addAll(movieList);
                            liveTopRatedMovieList.postValue(liveTopRatedMovieList.getValue());
                        } else {
                            liveTopRatedMovieList.postValue(movieList);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PopularMovies> call, Throwable t) {
            }
        });
    }
}
