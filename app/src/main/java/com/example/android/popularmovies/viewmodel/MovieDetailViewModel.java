package com.example.android.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MovieReview;
import com.example.android.popularmovies.model.api.MovieReviewsResponse;
import com.example.android.popularmovies.model.MovieVideo;
import com.example.android.popularmovies.model.api.MovieVideosResponse;
import com.example.android.popularmovies.model.api.MovieDbClient;
import com.example.android.popularmovies.model.database.FavoriteMovieDb;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saiyang Qi on 6/8/18.
 */
public class MovieDetailViewModel extends AndroidViewModel {
    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    private MutableLiveData<List<MovieVideo>> liveMovieVideoList;
    private MutableLiveData<List<MovieReview>> liveMovieReviewList;

    private static FavoriteMovieDb db;
    private static MovieDbClient client;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        db = FavoriteMovieDb.getDatabase(application.getApplicationContext());
    }

    public MutableLiveData<List<MovieVideo>> getMovieVideoList(String apiKey, int movieId) {
        initClient();
        if (liveMovieVideoList == null) {
            liveMovieVideoList = new MutableLiveData<>();
            loadMovieVideoList(apiKey, movieId);
        }
        return liveMovieVideoList;
    }

    public MutableLiveData<List<MovieReview>> getMovieReviewList(String apiKey, int movieId) {
        initClient();
        if (liveMovieReviewList == null) {
            liveMovieReviewList = new MutableLiveData<>();
            loadMovieReviewList(apiKey, movieId);
        }
        return liveMovieReviewList;
    }

    public void insertFavorite(final Movie movie) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.favoriteMovieDao().insertAll(movie);
            }
        }).start();
    }

    public void deleteFavorite(final Movie movie) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.favoriteMovieDao().delete(movie);
            }
        }).start();
    }

    public boolean isInDb (final int movieId) {
        boolean found = false;
        try {
            found = new QueryByIdAsync().execute(movieId).get() != null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return found;
    }

    private static class QueryByIdAsync extends AsyncTask<Integer, Void, Movie> {
        @Override
        protected Movie doInBackground(Integer... ids) {
            return db.favoriteMovieDao().findById(ids[0]);
        }
    }

    private void initClient() {
        if (client == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            client = retrofit.create(MovieDbClient.class);
        }
    }

    private void loadMovieVideoList(String apiKey, int movieId) {
        Call<MovieVideosResponse> call = client.getMovieVideos(movieId, apiKey);
        call.enqueue(new Callback<MovieVideosResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieVideosResponse> call, @NonNull Response<MovieVideosResponse> response) {
                MovieVideosResponse movieVideosResponse = response.body();
                if (movieVideosResponse != null) {
                    liveMovieVideoList.postValue(movieVideosResponse.getMovieVideoList());
                }
            }
            @Override
            public void onFailure(@NonNull Call<MovieVideosResponse> call, @NonNull Throwable t) {
            }
        });
    }

    private void loadMovieReviewList(String apiKey, int movieId) {
        Call<MovieReviewsResponse> call = client.getMovieReviews(movieId, apiKey);
        call.enqueue(new Callback<MovieReviewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieReviewsResponse> call, @NonNull Response<MovieReviewsResponse> response) {
                MovieReviewsResponse movieReviewsResponse = response.body();
                if (movieReviewsResponse != null) {
                    liveMovieReviewList.postValue(movieReviewsResponse.getMovieReviewlist());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieReviewsResponse> call, @NonNull Throwable t) {
            }
        });
    }

}
