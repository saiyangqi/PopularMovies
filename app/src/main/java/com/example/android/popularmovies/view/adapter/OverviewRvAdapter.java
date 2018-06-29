package com.example.android.popularmovies.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.view.DetailsActivity;
import com.example.android.popularmovies.view.OverviewActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saiyang Qi on 5/13/18.
 */
public class OverviewRvAdapter extends RecyclerView.Adapter<OverviewRvAdapter.ViewHolder>{
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w342";
    private List<Movie> movieList;

    public OverviewRvAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.overview_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movie movie = movieList.get(position);
        String fullPosterUrl = getPosterFullPath(movie);
        Picasso.get()
                .load(fullPosterUrl)
                .placeholder(R.color.colorPrimary)
                .into(holder.movieItemImageView);
        holder.movieItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent openDetailsIntent = new Intent(context, DetailsActivity.class);
                openDetailsIntent.putExtra(OverviewActivity.MOVIE_KEY, movie);

                context.startActivity(openDetailsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        }
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_view_poster)
        ImageView movieItemImageView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private String getPosterFullPath(Movie movie) {
        return POSTER_BASE_URL + movie.getPosterPath();
    }
}
