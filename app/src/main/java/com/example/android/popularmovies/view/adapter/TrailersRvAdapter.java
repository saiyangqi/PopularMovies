package com.example.android.popularmovies.view.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
        final MovieVideo video = trailerList.get(position);
        String thumbnailUrl = getThumbnailUrl(video);
        Picasso.get()
                .load(thumbnailUrl)
                .placeholder(R.color.colorPrimary)
                .into(holder.videoThumbnailImageView);
        holder.videoTitleTextView.setText(video.getName());
        holder.listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = video.getKey();
                watchYoutubeVideo(view.getContext(), key);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (trailerList == null) {
            return 0;
        } else {
            return trailerList.size();
        }
    }

    private String getThumbnailUrl(MovieVideo video) {
        return THUMBNAIL_BASE_URL + video.getKey() + "/default.jpg";
    }

    private void watchYoutubeVideo(Context context, String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_video_title)
        TextView videoTitleTextView;
        @BindView(R.id.image_view_video_thumbnail)
        ImageView videoThumbnailImageView;
        @BindView(R.id.view_trailers_list_item)
        View listItemView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
