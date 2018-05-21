package com.example.android.popularmovies.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    public static final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w500";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(OverviewActivity.MOVIE_KEY)) {
            Movie movie = intent.getParcelableExtra(OverviewActivity.MOVIE_KEY);
            displayMovieDetails(movie);
        }
    }

    private void displayMovieDetails(Movie movie) {
        String fullBackdropUrl = getBackdropFullPath(movie);
        Picasso.get()
                .load(fullBackdropUrl)
                .into(backDropImageView);
        titleTextView.setText(movie.getTitle());
        ratingBar.setRating((float) (movie.getVoteAverage() / 2.0));
        ratingCountTextView.setText(String.format(Locale.ENGLISH, "(%d ratings)", movie.getVoteCount()));
        releaseDateTextView.setText(movie.getReleaseDate());
        plotTextView.setText(movie.getOverview());
    }

    private String getBackdropFullPath(Movie movie) {
        return BACKDROP_BASE_URL + movie.getBackdropPath();
    }
}
