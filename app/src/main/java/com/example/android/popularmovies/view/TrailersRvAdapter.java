package com.example.android.popularmovies.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.MovieVideo;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saiyang Qi on 6/23/18.
 */
public class TrailersRvAdapter extends RecyclerView.Adapter<TrailersRvAdapter.ViewHolder> {
    private static final String THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/";
    private List<MovieVideo> trailerList;

    public TrailersRvAdapter(List<MovieVideo> trailerList) {
        this.trailerList = trailerList;
    }

    public void setTrailerList(List<MovieVideo> trailerList) {
        this.trailerList = trailerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.trailers_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieVideo video = trailerList.get(position);
        holder.videoTitleTextView.setText(video.getName());
        String thumbnailUrl = getThumbnailUrl(video);
        Picasso.get()
                .load(thumbnailUrl)
                .placeholder(R.color.colorPrimary)
                .into(holder.videoThumbnailImageView);
    }

    private String getThumbnailUrl(MovieVideo video) {
        return THUMBNAIL_BASE_URL + video.getKey() + "/default.jpg";
    }

    @Override
    public int getItemCount() {
        if (trailerList == null) {
            return 0;
        } else {
            return trailerList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_video_title)
        TextView videoTitleTextView;
        @BindView(R.id.image_view_video_thumbnail)
        ImageView videoThumbnailImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
