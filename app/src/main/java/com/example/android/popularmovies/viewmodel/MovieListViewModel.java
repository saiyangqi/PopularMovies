package com.example.android.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.api.PopularMoviesResponse;
import com.example.android.popularmovies.model.api.MovieDbClient;
import com.example.android.popularmovies.model.database.FavoriteMovieDb;
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
public class MovieListViewModel extends AndroidViewModel {
    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    private MutableLiveData<List<Movie>> livePopularMovieList;
    private MutableLiveData<List<Movie>> liveTopRatedMovieList;
    private LiveData<List<Movie>> liveFavoriteMovieList;

    private int popularPageNum = 0;
    private int topRatedPageNum = 0;

    private FavoriteMovieDb db;
    private MovieDbClient client;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        db = FavoriteMovieDb.getDatabase(application.getApplicationContext());
    }

    public LiveData<List<Movie>> getMovieList(String apiKey, int mode) {
        if (mode == OverviewActivity.MODE_FAVORITE) {
            return getFavoriteMovieList();
        }
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

    public void insertAllToFavorite(final Movie... favoriteMovies) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.favoriteMovieDao().insertAll(favoriteMovies);
            }
        }).start();
    }

    private LiveData<List<Movie>> getFavoriteMovieList() {
        if (liveFavoriteMovieList == null) {
            liveFavoriteMovieList = db.favoriteMovieDao().getAll();
        }
        return liveFavoriteMovieList;
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
        Call<PopularMoviesResponse> call;
        if (mode == OverviewActivity.MODE_POPULAR) {
            popularPageNum++;
            call = client.getPopularMovies(apiKey, Integer.toString(popularPageNum));
        } else {
            topRatedPageNum++;
            call = client.getTopRatedMovies(apiKey, Integer.toString(topRatedPageNum));
        }

        call.enqueue(new Callback<PopularMoviesResponse>() {
            @Override
            public void onResponse(Call<PopularMoviesResponse> call, Response<PopularMoviesResponse> response) {
                PopularMoviesResponse popularMoviesResponse = response.body();
                List<Movie> movieList = null;
                if (popularMoviesResponse != null) {
                    movieList = popularMoviesResponse.getMovieList();
                    if (popularMoviesResponse.getTotalPages() < popularMoviesResponse.getPage()) {
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
                    } else if (mode == OverviewActivity.MODE_TOP_RATED){
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
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {
            }
        });
    }
}
