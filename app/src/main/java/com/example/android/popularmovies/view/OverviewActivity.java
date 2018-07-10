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
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.DisplayUtils;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.example.android.popularmovies.view.adapter.OverviewRvAdapter;
import com.example.android.popularmovies.viewmodel.MovieListViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OverviewActivity extends AppCompatActivity {
    public static final String MODE_KEY = "selected_mode";
    public static final String MOVIE_KEY = "selected_movie";

    public static final int MODE_POPULAR = 0;
    public static final int MODE_TOP_RATED = 1;
    public static final int MODE_FAVORITE = 2;

    private MovieListViewModel movieListViewModel;

    @BindView(R.id.recycler_view_overview)
    RecyclerView recyclerView;

    @BindView(R.id.text_view_overview_error)
    TextView errorTextView;

    private GridLayoutManager layoutManager;
    private OverviewRvAdapter adapter;

    private String apiKey;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        ButterKnife.bind(this);

        apiKey = getString(R.string.api_key);
        if (savedInstanceState != null) {
            mode = savedInstanceState.getInt(MODE_KEY, MODE_POPULAR);
        }
        setUpRecyclerView();
        if (NetworkUtils.hasInternetConnection(this) || mode == MODE_FAVORITE) {
            showMovieList();
        } else {
            errorTextView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(MODE_KEY, mode);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overview_menu, menu);
        switch (mode) {
            case MODE_POPULAR:
                menu.findItem(R.id.overview_menu_popular).setChecked(true);
                break;
            case MODE_TOP_RATED:
                menu.findItem(R.id.overview_menu_top_rated).setChecked(true);
                break;
            case MODE_FAVORITE:
                menu.findItem(R.id.overview_menu_favorite).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.overview_menu_favorite) {
            if (mode != MODE_FAVORITE) {
                mode = MODE_FAVORITE;
                showMovieList();
                layoutManager.scrollToPositionWithOffset(0, 0);
            }
            item.setChecked(true);
            setTitle(getString(R.string.title_favorite));
        } else if (NetworkUtils.hasInternetConnection(this)) {
            switch (item.getItemId()) {
                case R.id.overview_menu_popular:
                    if (mode != MODE_POPULAR) {
                        mode = MODE_POPULAR;
                        showMovieList();
                        layoutManager.scrollToPositionWithOffset(0, 0);
                    }
                    item.setChecked(true);
                    setTitle(getString(R.string.title_popular));
                    break;
                case R.id.overview_menu_top_rated:
                    if (mode != MODE_TOP_RATED) {
                        mode = MODE_TOP_RATED;
                        showMovieList();
                        layoutManager.scrollToPositionWithOffset(0, 0);
                    }
                    item.setChecked(true);
                    setTitle(getString(R.string.title_top_rated));
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        int numOfColumns = DisplayUtils.getNumOfColumns(this);
        layoutManager = new GridLayoutManager(this, numOfColumns);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new OverviewRvAdapter(null);
        recyclerView.setAdapter(adapter);
        setUpViewModel();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mode != MODE_FAVORITE && !recyclerView.canScrollVertically(1)) {
                    movieListViewModel.updateMovieList(apiKey, mode);
                }
            }
        });
    }

    private void setUpViewModel() {
        movieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
    }

    private void showMovieList() {
        final int localMode = mode;
        movieListViewModel.getMovieList(apiKey, localMode).observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movieList) {
                if (movieList != null && movieList.size() != 0) {
                    errorTextView.setVisibility(View.INVISIBLE);
                }
                if ((movieList == null || movieList.size() == 0) && localMode == MODE_FAVORITE) {
                    errorTextView.setText(getString(R.string.overview_favorite_empty));
                    errorTextView.setVisibility(View.VISIBLE);
                }
                if (localMode == mode) {
                    adapter.setMovieList(movieList);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
