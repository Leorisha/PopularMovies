package com.cryogenius.popularmovies.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cryogenius.popularmovies.Bus.EventBus;
import com.cryogenius.popularmovies.Bus.Messages.Actions.GetMovieDetailAction;
import com.cryogenius.popularmovies.Bus.Messages.Events.MovieDetailEvent;
import com.cryogenius.popularmovies.R;
import com.squareup.otto.Subscribe;

/**
 * Created by Ana Neto on 05/03/2017.
 */

public class InfoFragment extends Fragment {

    int selectedIndex;
    private TextView moviePlotDetail;
    private TextView userRating;
    private TextView releaseDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_info_layout, container, false);

        moviePlotDetail = (TextView) rootView.findViewById(R.id.tv_movie_detail_synopsis);
        userRating = (TextView) rootView.findViewById(R.id.tv_movie_detail_user_rating);
        releaseDate = (TextView) rootView.findViewById(R.id.tv_movie_detail_duration);

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
        EventBus.getInstance().post(new GetMovieDetailAction(this.selectedIndex));
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getInstance().unregister(this);
    }

    public int getSelectedIndex() {

        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;


    }

    @Subscribe
    public void onMovieDetailEvent(final MovieDetailEvent event) {
        if (event.getSelectedMovie() != null) {
            releaseDate.setText(event.getSelectedMovie().getReleaseDate().split("-")[0]);
            moviePlotDetail.setText(event.getSelectedMovie().getOverview());
            userRating.setText(event.getSelectedMovie().getVoteAverage() + "/10");
        } else {
            //TODO: error
        }
    }
}
