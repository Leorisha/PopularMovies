package com.cryogenius.popularmovies.UI.MovieDetail.Trailers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class TrailersFragment extends Fragment implements TrailerItemClickListener, ShareClickListener {

    private int selectedMovieId;
    private MovieTrailerList selectedMovieTrailers;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView emptyTrailersMessage;

    private static final String LIFECYCLE_SELECTED_MOVIE_ID = "selected_movie_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_trailers_layout, container, false);
        emptyTrailersMessage = (TextView) rootView.findViewById(R.id.trailers_empty_list);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_trailers_layout);
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

        loadSelectedMovieIdInSharedPreferences();

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
            if (this.selectedMovieId > 0){
                saveSelectedMovieIdInSharedPreferences();
                EventBus.getInstance().post(new GetMovieDetailTrailerListAction(this.selectedMovieId));
            }
        }
        else {
            mRecyclerView.setVisibility(View.GONE);
            emptyTrailersMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getInstance().unregister(this);
    }

    public void setSelectedMovieId(int selectedMovieId) {

        this.selectedMovieId = selectedMovieId;
    }

    private void saveSelectedMovieIdInSharedPreferences() {
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = pref.edit();

        prefsEditor.putInt("SELECTED_MOVIE_ID",this.selectedMovieId);
        prefsEditor.commit();
    }

    private void loadSelectedMovieIdInSharedPreferences() {
        SharedPreferences mPrefs = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        this.selectedMovieId = mPrefs.getInt("SELECTED_MOVIE_ID",0);
    }

    @Subscribe
    public void onMovieDetailTrailersEvent(final MovieDetailTrailersEvent event) {
        if (event.getTrailersList() != null){
            this.selectedMovieTrailers = event.getTrailersList();

            // specify an adapter (see also next example)
            mAdapter = new TrailersAdapter(this.selectedMovieTrailers,this,this);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
        else {
            mRecyclerView.setVisibility(View.GONE);
            emptyTrailersMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        MovieTrailer trailer = this.selectedMovieTrailers.getTrailers().get(clickedItemIndex);

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+trailer.getKey())));
        Log.i("Video", "Video Playing....");
    }

    @Override
    public void onShareItemClick(int clickedItemIndex) {

        MovieTrailer trailer = this.selectedMovieTrailers.getTrailers().get(clickedItemIndex);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v="+trailer.getKey());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
