package com.example.android.popularmovies.model.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.popularmovies.model.Movie;

/**
 * Created by Saiyang Qi on 6/30/18.
 */
@Database(entities = {Movie.class}, version = 1)
public abstract class FavoriteMovieDb extends RoomDatabase{
    private static FavoriteMovieDb INSTANCE;

    public static FavoriteMovieDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, FavoriteMovieDb.class, "favorite-movie-db")
                    .build();
        }
        return INSTANCE;
    }

    public abstract FavoriteMovieDao favoriteMovieDao();
}
