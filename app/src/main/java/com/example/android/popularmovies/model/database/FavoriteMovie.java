package com.example.android.popularmovies.model.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Saiyang Qi on 6/30/18.
 */
@Entity
public class FavoriteMovie {
    @PrimaryKey
    private int id;
    private String title;
    private String overview;
    @ColumnInfo(name = "vote_average")
    private double voteAverage;
    @ColumnInfo(name = "vote_count")
    private int voteCount;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] poster;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] backdrop;
}
