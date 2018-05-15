package com.example.android.popularmovies.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.DisplayUtils;
import com.example.android.popularmovies.viewmodel.MovieListViewModel;

import java.util.List;

public class OverviewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        recyclerView = findViewById(R.id.recycler_view_overview);
        recyclerView.setHasFixedSize(true);
        int numOfColumns = DisplayUtils.getNumOfColumns(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numOfColumns));

        final String API_KEY = getString(R.string.api_key);
        MovieListViewModel viewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        viewModel.getMovieList(API_KEY).observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movieList) {
                Log.d("CHECKING SIZE", "onChanged: " + movieList.size());
                recyclerView.swapAdapter(new OverviewRvAdapter(movieList), true);
            }
        });
    }
}
