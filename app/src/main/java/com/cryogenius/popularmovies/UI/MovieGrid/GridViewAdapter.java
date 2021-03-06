package com.cryogenius.popularmovies.UI.MovieGrid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cryogenius.popularmovies.API.Models.MovieList;
import com.cryogenius.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Ana Neto on 30/01/2017.
 */

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.GridItemHolder> {

    final private GridItemClickListener mOnClickListener;
    private int mNumberItems;
    private MovieList moviesInGrid;

    public MovieList getMoviesInGrid() {
        return moviesInGrid;
    }

    public void setMoviesInGrid(MovieList moviesInGrid) {
        this.moviesInGrid = moviesInGrid;
        this.mNumberItems = this.moviesInGrid.getMovies().size();
    }

    @Override
    public GridItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        GridItemHolder viewHolder = new GridItemHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GridItemHolder holder, int position) {
        Context context = holder.posterImage.getContext();
        //url build
        String posterURL = context.getString(R.string.image_url)+moviesInGrid.getMovies().get(position).getPosterPath();
        Picasso.with(context).load(posterURL).into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public GridViewAdapter( GridItemClickListener listener) {
        mOnClickListener = listener;
    }

    class GridItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView posterImage;

        public GridItemHolder(View itemView) {
            super(itemView);

            this.posterImage = (ImageView)itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}

