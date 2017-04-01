package com.cryogenius.popularmovies.UI.MovieDetail.Reviews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cryogenius.popularmovies.API.Models.MovieReviewsList;
import com.cryogenius.popularmovies.Bus.EventBus;
import com.cryogenius.popularmovies.Bus.Messages.Actions.GetMovieDetailTrailerListAction;
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

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView emptyReviewsMessage;

    private static final String LIFECYCLE_SELECTED_MOVIE_ID = "selected_movie_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_reviews_layout, container, false);

        emptyReviewsMessage = (TextView)rootView.findViewById(R.id.reviews_empty_list);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_reviews_card_layout);
// use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            selectedMovieId = savedInstanceState.getInt(LIFECYCLE_SELECTED_MOVIE_ID, 0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LIFECYCLE_SELECTED_MOVIE_ID, selectedMovieId);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getInstance().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.isOnline()){
            EventBus.getInstance().post(new GetMoviewDetailReviewsListAction(this.selectedMovieId));
        }
        else {
            mRecyclerView.setVisibility(View.GONE);
            emptyReviewsMessage.setVisibility(View.VISIBLE);
        }



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

        if (event.getReviewsList() != null && event.getReviewsList().getReviews().size() > 0) {
            this.selectedMovieReviews = event.getReviewsList();

            // specify an adapter (see also next example)
            mAdapter = new ReviewsAdapter(this.selectedMovieReviews);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
        else {
            mRecyclerView.setVisibility(View.GONE);
            emptyReviewsMessage.setVisibility(View.VISIBLE);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
