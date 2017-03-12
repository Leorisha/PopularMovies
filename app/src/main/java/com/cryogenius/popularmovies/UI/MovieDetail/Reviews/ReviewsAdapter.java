package com.cryogenius.popularmovies.UI.MovieDetail.Reviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cryogenius.popularmovies.API.Models.MovieReviewsList;
import com.cryogenius.popularmovies.R;

class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private MovieReviewsList reviewsList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReviewsAdapter(MovieReviewsList reviewsList) {
        this.reviewsList = reviewsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_layout, parent, false);
        ReviewViewHolder pvh = new ReviewViewHolder(v);
        return pvh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.reviewContent.setText(reviewsList.getReviews().get(position).getContent());
        holder.authorName.setText(reviewsList.getReviews().get(position).getAuthor());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (reviewsList != null && reviewsList.getReviews() != null){
            return reviewsList.getReviews().size();
        }
        else{
            return 0;
        }
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewContent;
        TextView authorName;

        ReviewViewHolder(View itemView) {
            super(itemView);
            reviewContent = (TextView)itemView.findViewById(R.id.info_text);
            authorName = (TextView)itemView.findViewById(R.id.author_name);
        }
    }
}

