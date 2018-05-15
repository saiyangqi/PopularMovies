package com.example.android.popularmovies.utils;

import android.content.Context;

import com.example.android.popularmovies.R;

/**
 * Created by Saiyang Qi on 5/14/18.
 */
public class DisplayUtils {
    public static final int NUM_OF_COLUMNS_PORT = 2;
    public static final int NUM_OF_COLUMNS_LAND = 4;

    public static int getNumOfColumns(Context context) {
        if (context.getResources().getBoolean(R.bool.is_portrait)) {
            return NUM_OF_COLUMNS_PORT;
        } else {
            return NUM_OF_COLUMNS_LAND;
        }
    }
}
