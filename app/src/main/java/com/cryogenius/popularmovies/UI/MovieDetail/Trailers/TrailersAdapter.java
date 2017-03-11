package com.cryogenius.popularmovies.UI.MovieDetail.Trailers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cryogenius.popularmovies.API.Models.MovieTrailerList;
import com.cryogenius.popularmovies.R;

/**
 * Created by Ana Neto on 11/03/2017.
 */

class TrailersAdapter extends RecyclerView.Adapter<com.cryogenius.popularmovies.UI.MovieDetail.Trailers.TrailersAdapter.TrailersViewHolder> {

    private MovieTrailerList trailersList;

    public static class TrailersViewHolder extends RecyclerView.ViewHolder {
        TextView trailerName;

        TrailersViewHolder(View itemView) {
            super(itemView);
            trailerName = (TextView)itemView.findViewById(R.id.tv_trailer_name);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TrailersAdapter(MovieTrailerList trailersList) {
        this.trailersList = trailersList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public com.cryogenius.popularmovies.UI.MovieDetail.Trailers.TrailersAdapter.TrailersViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                                                  int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers_item_layout, parent, false);
        com.cryogenius.popularmovies.UI.MovieDetail.Trailers.TrailersAdapter.TrailersViewHolder pvh = new com.cryogenius.popularmovies.UI.MovieDetail.Trailers.TrailersAdapter.TrailersViewHolder(v);
        return pvh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(com.cryogenius.popularmovies.UI.MovieDetail.Trailers.TrailersAdapter.TrailersViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.trailerName.setText(trailersList.getTrailers().get(position).getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (trailersList != null && trailersList.getTrailers() != null){
            return trailersList.getTrailers().size();
        }
        else{
            return 0;
        }
    }
}


