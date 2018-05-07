package com.example.android.popularmovies.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.PopularMovies;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saiyang Qi on 5/6/18.
 */
public class NetworkingUtils {
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String SORT_BY = "popularity.desc";

    public static void fetchPopularMovies(final Context context) {
        final String API_KEY = context.getString(R.string.api_key);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDbClient client = retrofit.create(MovieDbClient.class);
        Call<PopularMovies> call = client.getPopularMovies(API_KEY, SORT_BY);
        call.enqueue(new Callback<PopularMovies>() {
            @Override
            public void onResponse(Call<PopularMovies> call, Response<PopularMovies> response) {
                PopularMovies popularMovies = response.body();
                List<Movie> movieList = popularMovies.getMovieList();
                Toast.makeText(context, Integer.toString(movieList.size()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PopularMovies> call, Throwable t) {
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
