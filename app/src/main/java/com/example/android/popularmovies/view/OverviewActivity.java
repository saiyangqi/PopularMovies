package com.example.android.popularmovies.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.DisplayUtils;
import com.example.android.popularmovies.viewmodel.MovieListViewModel;

import java.util.List;

public class OverviewActivity extends AppCompatActivity {
    public static final String MODE_KEY = "selected_mode";
    public static final int MODE_POPULAR = 0;
    public static final int MODE_TOP_RATED = 1;

    private MovieListViewModel viewModel;
    private RecyclerView recyclerView;

    private String apiKey;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        apiKey = getString(R.string.api_key);
        if (savedInstanceState != null) {
            mode = savedInstanceState.getInt(MODE_KEY, MODE_POPULAR);
        }
        setUpRecyclerView();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(MODE_KEY, mode);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overview_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.overview_menu_popular:
                if (mode != MODE_POPULAR) {
                    mode = MODE_POPULAR;
                    showMovieList();
                }
                break;
            case R.id.overview_menu_top_rated:
                if (mode != MODE_TOP_RATED) {
                    mode = MODE_TOP_RATED;
                    showMovieList();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMovieList() {
        viewModel.getMovieList(apiKey, mode).observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movieList) {
                recyclerView.swapAdapter(new OverviewRvAdapter(movieList), true);
            }
        });
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_overview);
        recyclerView.setHasFixedSize(true);
        int numOfColumns = DisplayUtils.getNumOfColumns(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numOfColumns));

        setUpViewModel();
    }

    private void setUpViewModel() {
        viewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        showMovieList();
    }
}
