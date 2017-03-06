package com.cryogenius.popularmovies.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cryogenius.popularmovies.API.Models.MovieTrailer;
import com.cryogenius.popularmovies.API.Models.MovieTrailerList;
import com.cryogenius.popularmovies.Bus.EventBus;
import com.cryogenius.popularmovies.Bus.Messages.Actions.GetMovieDetailTrailerListAction;
import com.cryogenius.popularmovies.Bus.Messages.Events.MovieDetailTrailersEvent;
import com.cryogenius.popularmovies.R;
import com.squareup.otto.Subscribe;

/**
 * Created by Ana Neto on 05/03/2017.
 */

public class TrailersFragment extends Fragment {

    private int selectedMovieId;
    private MovieTrailerList selectedMovieTrailers;

    TextView trailerList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_trailers_layout, container, false);
        trailerList = (TextView) rootView.findViewById(R.id.trailers_list);

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
        EventBus.getInstance().post(new GetMovieDetailTrailerListAction(this.selectedMovieId));
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getInstance().unregister(this);
    }

    public void setSelectedMovieId(int selectedMovieId) {
        this.selectedMovieId = selectedMovieId;
    }

    @Subscribe
    public void onMovieDetailTrailersEvent(final MovieDetailTrailersEvent event) {
        if (event.getTrailersList() != null){
            this.selectedMovieTrailers = event.getTrailersList();

            String textResults = "";
            for(MovieTrailer trailer: this.selectedMovieTrailers.getTrailers()){
                textResults += trailer.getName()+"\n";
            }

            this.trailerList.setText(textResults);
        }
    }
}
