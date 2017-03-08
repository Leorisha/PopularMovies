package com.cryogenius.popularmovies.UI.MovieDetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cryogenius.popularmovies.API.Models.MovieReview;
import com.cryogenius.popularmovies.API.Models.MovieReviewsList;
import com.cryogenius.popularmovies.Bus.EventBus;
import com.cryogenius.popularmovies.Bus.Messages.Actions.GetMoviewDetailReviewsListAction;
import com.cryogenius.popularmovies.Bus.Messages.Events.MovieDetailReviewsEvent;
import com.cryogenius.popularmovies.R;
import com.squareup.otto.Subscribe;

/**
 * Created by Ana Neto on 05/03/2017.
 */

public class ReviewsFragment extends Fragment {

    private int selectedMovieId;
    private MovieReviewsList selectedMovieReviews;

    TextView reviewList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_reviews_layout, container, false);
        reviewList = (TextView) rootView.findViewById(R.id.reviews_list);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getInstance().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getInstance().post(new GetMoviewDetailReviewsListAction(this.selectedMovieId));

    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getInstance().unregister(this);
    }

    public int getSelectedMovieId() {
        return selectedMovieId;
    }

    public void setSelectedMovieId(int selectedMovieId) {
        this.selectedMovieId = selectedMovieId;
    }

    @Subscribe
    public void onMovieDetailReviewsEvent(final MovieDetailReviewsEvent event) {

        if (event.getReviewsList() != null) {
            this.selectedMovieReviews = event.getReviewsList();
            String textResults = "";
            for (MovieReview review : this.selectedMovieReviews.getReviews()) {
                textResults += review.getAuthor()+" :\n" + review.getContent() + "\n\n";
            }

            this.reviewList.setText(textResults);

        }
    }
}
