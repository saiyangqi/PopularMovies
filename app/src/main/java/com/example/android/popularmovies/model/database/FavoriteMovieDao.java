package com.example.android.popularmovies.model.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Saiyang Qi on 6/30/18.
 */
@Dao
public interface FavoriteMovieDao {
    @Insert
    void insertAll(FavoriteMovie... favoriteMovie);

    @Delete
    void delete(FavoriteMovie favoriteMovie);

    @Query("SELECT * FROM FavoriteMovie WHERE id = :id;")
    FavoriteMovie findById(int id);

    @Query("SELECT * FROM FavoriteMovie;")
    LiveData<List<FavoriteMovie>> getAll();
}
