package com.example.android.popularmovies.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.MovieReview;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saiyang Qi on 6/27/18.
 */
public class ReviewsRvAdapter extends RecyclerView.Adapter<ReviewsRvAdapter.ViewHolder>{
    private List<MovieReview> movieReviewList;

    public ReviewsRvAdapter(List<MovieReview> movieReviewList) {
        this.movieReviewList = movieReviewList;
    }

    public void setMovieReviewList(List<MovieReview> movieReviewList) {
        this.movieReviewList = movieReviewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.reviews_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        MovieReview movieReview = movieReviewList.get(position);
        holder.reviewContentTextView.setText(movieReview.getContent());
        holder.reviewAuthorTextView.setText(
                holder.reviewAuthorTextView.getContext()
                        .getString(R.string.reviews_author, movieReview.getAuthor()));
    }

    @Override
    public int getItemCount() {
        if (movieReviewList == null) {
            return 0;
        } else {
            return movieReviewList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_review_content)
        TextView reviewContentTextView;
        @BindView(R.id.text_view_review_author)
        TextView reviewAuthorTextView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
