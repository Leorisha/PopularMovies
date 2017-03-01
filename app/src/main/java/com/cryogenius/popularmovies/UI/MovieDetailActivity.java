package com.cryogenius.popularmovies.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.cryogenius.popularmovies.API.Models.Movie;
import com.cryogenius.popularmovies.API.Models.MovieReviewsList;
import com.cryogenius.popularmovies.API.Models.MovieTrailerList;
import com.cryogenius.popularmovies.Bus.EventBus;
import com.cryogenius.popularmovies.Bus.Messages.Actions.GetMovieDetailAction;
import com.cryogenius.popularmovies.Bus.Messages.Actions.GetMovieDetailTrailerListAction;
import com.cryogenius.popularmovies.Bus.Messages.Actions.GetMoviewDetailReviewsListAction;
import com.cryogenius.popularmovies.Bus.Messages.Events.MovieDetailEvent;
import com.cryogenius.popularmovies.Bus.Messages.Events.MovieDetailReviewsEvent;
import com.cryogenius.popularmovies.Bus.Messages.Events.MovieDetailTrailersEvent;
import com.cryogenius.popularmovies.R;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

/**
 * Created by Ana Neto on 26/01/2017.
 */

public class MovieDetailActivity extends AppCompatActivity {

    private int selectedIndex;
    private Movie selectedMovie;
    private MovieReviewsList selectedMovieReviews;
    private MovieTrailerList selectedMovieTrailers;
    private int apiRequestCounter = 0;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    TextView moviePlotDetail;
    TextView userRating;
    TextView releaseDate;
    ImageView moviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        moviePlotDetail = (TextView) findViewById(R.id.tv_movie_detail_synopsis);
        userRating = (TextView) findViewById(R.id.tv_movie_detail_user_rating);
        releaseDate = (TextView) findViewById(R.id.tv_movie_detail_duration);
        moviePoster = (ImageView) findViewById(R.id.iv_movie_detail_poster);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            String selectedIndexInString = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            this.selectedIndex = Integer.parseInt(selectedIndexInString);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getInstance().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.selectedIndex >= 0) {
            apiRequestCounter++;
            EventBus.getInstance().post(new GetMovieDetailAction(this.selectedIndex));
        } else {
            //TODO: display error
        }
    }

    @Override
    public void onStop() {
        EventBus.getInstance().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onMovieDetailEvent(final MovieDetailEvent event) {
        apiRequestCounter--;

        if (event.getSelectedMovie() != null) {
            apiRequestCounter++;
            EventBus.getInstance().post(new GetMovieDetailTrailerListAction(event.getSelectedMovie().getId()));
            apiRequestCounter++;
            EventBus.getInstance().post(new GetMoviewDetailReviewsListAction(event.getSelectedMovie().getId()));

            this.selectedMovie = event.getSelectedMovie();

        } else {
            //TODO: error
        }

        this.populateTheActivity();
    }

    @Subscribe
    public void onMovieDetailReviewsEvent(final MovieDetailReviewsEvent event) {
        apiRequestCounter--;
        this.populateTheActivity();
    }

    @Subscribe
    public void onMovieDetailTrailersEvent(final MovieDetailTrailersEvent event) {
        apiRequestCounter--;
        this.populateTheActivity();
    }

    private void populateTheActivity(){
        if(this.apiRequestCounter == 0) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);

            collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
            collapsingToolbarLayout.setTitle(this.selectedMovie.getOriginalTitle());
            actionBar.setTitle(this.selectedMovie.getOriginalTitle());


            Context context = moviePoster.getContext();
            //url build
            String posterURL = context.getString(R.string.image_url_detail) + this.selectedMovie.getPosterPath();
            Picasso.with(context).load(posterURL).into(moviePoster);

            releaseDate.setText(this.selectedMovie.getReleaseDate().split("-")[0]);
            moviePlotDetail.setText(this.selectedMovie.getOverview());
            userRating.setText(this.selectedMovie.getVoteAverage() + "/10");
        }
    }
}
