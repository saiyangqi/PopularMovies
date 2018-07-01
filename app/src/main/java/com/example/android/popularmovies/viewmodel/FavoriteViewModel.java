package com.example.android.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.model.database.FavoriteMovie;
import com.example.android.popularmovies.model.database.FavoriteMovieDb;

import java.util.List;

/**
 * Created by Saiyang Qi on 7/1/18.
 */
public class FavoriteViewModel extends AndroidViewModel {
    private FavoriteMovieDb db;
    private LiveData<List<FavoriteMovie>> liveFavoriteMovieList;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        db = FavoriteMovieDb.getDatabase(application.getApplicationContext());
        liveFavoriteMovieList = db.favoriteMovieDao().getAll();
    }

    public LiveData<List<FavoriteMovie>> getFavoriteMovieList() {
        return liveFavoriteMovieList;
    }

    public void insertAll(final FavoriteMovie... favoriteMovies) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.favoriteMovieDao().insertAll(favoriteMovies);
            }
        }).start();
    }
}
