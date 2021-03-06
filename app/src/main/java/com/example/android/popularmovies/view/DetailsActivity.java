package com.example.android.popularmovies.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MovieReview;
import com.example.android.popularmovies.model.MovieVideo;
import com.example.android.popularmovies.view.adapter.ReviewsRvAdapter;
import com.example.android.popularmovies.view.adapter.TrailersRvAdapter;
import com.example.android.popularmovies.viewmodel.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    public static final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private MovieDetailViewModel viewModel;

    @BindView(R.id.image_view_backdrop)
    ImageView backDropImageView;
    @BindView(R.id.text_view_title)
    TextView titleTextView;
    @BindView(R.id.rating_bar_movie)
    RatingBar ratingBar;
    @BindView(R.id.text_view_rating_count)
    TextView ratingCountTextView;
    @BindView(R.id.text_view_release_date)
    TextView releaseDateTextView;
    @BindView(R.id.text_view_plot)
    TextView plotTextView;
    @BindView(R.id.recycler_view_details_trailers)
    RecyclerView trailersRecyclerView;
    @BindView(R.id.recycler_view_details_reviews)
    RecyclerView reviewsRecyclerView;

    private String apiKey;
    private TrailersRvAdapter trailersRvAdapter;
    private ReviewsRvAdapter reviewsRvAdapter;

    private Movie movie;

    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        apiKey = getString(R.string.api_key);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(OverviewActivity.MOVIE_KEY)) {
            movie = intent.getParcelableExtra(OverviewActivity.MOVIE_KEY);
            viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
            displayMovieDetails();
            displayMovieVideos();
            displayMovieReviews();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        if (viewModel.isInDb(movie.getId())) {
            isFavorite = true;
            menu.findItem(R.id.details_menu_favorite).setIcon(R.drawable.ic_favorite_white_full_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.details_menu_favorite:
                if (isFavorite) {
                    item.setIcon(R.drawable.ic_favorite_white_24dp);
                    viewModel.deleteFavorite(movie);
                } else {
                    item.setIcon(R.drawable.ic_favorite_white_full_24dp);
                    viewModel.insertFavorite(movie);
                }
                isFavorite = !isFavorite;
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayMovieReviews() {
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        reviewsRvAdapter = new ReviewsRvAdapter(null);
        reviewsRecyclerView.setAdapter(reviewsRvAdapter);
        viewModel.getMovieReviewList(apiKey, movie.getId()).observe(this, new Observer<List<MovieReview>>() {
            @Override
            public void onChanged(@Nullable List<MovieReview> movieReviews) {
                reviewsRvAdapter.setMovieReviewList(movieReviews);
                reviewsRvAdapter.notifyDataSetChanged();
            }
        });
    }

    private void displayMovieVideos() {
        trailersRecyclerView.setHasFixedSize(true);
        trailersRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        trailersRvAdapter = new TrailersRvAdapter(null);
        trailersRecyclerView.setAdapter(trailersRvAdapter);
        viewModel.getMovieVideoList(apiKey, movie.getId()).observe(this, new Observer<List<MovieVideo>>() {
            @Override
            public void onChanged(@Nullable List<MovieVideo> movieVideos) {
                trailersRvAdapter.setTrailerList(movieVideos);
                trailersRvAdapter.notifyDataSetChanged();
            }
        });
    }

    private void displayMovieDetails() {
        boolean isPortrait = getResources().getBoolean(R.bool.is_portrait);
        setTitle(movie.getTitle());
        displayImage(movie, isPortrait);
        titleTextView.setText(movie.getTitle());
        ratingBar.setRating((float) (movie.getVoteAverage() / 2.0));
        ratingCountTextView.setText(getString(R.string.rating_count, movie.getVoteCount()));
        releaseDateTextView.setText(movie.getReleaseDate());
        plotTextView.setText(movie.getOverview());
    }

    private void displayImage(Movie movie, boolean isPortrait) {
        String fullImagePath;
        if (isPortrait) {
            fullImagePath = getBackdropFullPath(movie);
        } else {
            fullImagePath = getPosterFullPath(movie);
        }
        Picasso.get()
                .load(fullImagePath)
                .placeholder(R.color.colorPrimary)
                .into(backDropImageView);
    }

    private String getBackdropFullPath(Movie movie) {
        return BACKDROP_BASE_URL + movie.getBackdropPath();
    }

    private String getPosterFullPath(Movie movie) {
        return BACKDROP_BASE_URL + movie.getPosterPath();
    }
}
