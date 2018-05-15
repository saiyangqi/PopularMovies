package com.example.android.popularmovies.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Saiyang Qi on 5/13/18.
 */
public class OverviewRvAdapter extends RecyclerView.Adapter<OverviewRvAdapter.ViewHolder>{
    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";
    private List<Movie> movieList;

    public OverviewRvAdapter(List<Movie> movieList) {
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
        String fullPosterUrl = getPosterFullPath(movieList.get(position));
        Picasso.get()
                .load(fullPosterUrl)
                .into(holder.movieItemImageView);
    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        }
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView movieItemImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            movieItemImageView = itemView.findViewById(R.id.list_item_movie_poster);
        }
    }

    private String getPosterFullPath(Movie movie) {
        return POSTER_BASE_URL + movie.getPosterPath();
    }
}
