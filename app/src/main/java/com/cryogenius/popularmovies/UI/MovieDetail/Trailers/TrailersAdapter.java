package com.cryogenius.popularmovies.UI.MovieDetail.Trailers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cryogenius.popularmovies.API.Models.MovieTrailerList;
import com.cryogenius.popularmovies.R;

/**
 * Created by Ana Neto on 11/03/2017.
 */

class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private MovieTrailerList trailersList;
    final private TrailerItemClickListener mOnClickListener;
    final private ShareClickListener mShareClickListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public TrailersAdapter(MovieTrailerList trailersList,TrailerItemClickListener listener,ShareClickListener shareListener) {

        this.trailersList = trailersList;
        mOnClickListener = listener;
        mShareClickListener = shareListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers_item_layout, parent, false);
        TrailersViewHolder pvh = new TrailersViewHolder(v);
        return pvh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {
        holder.trailerName.setText(trailersList.getTrailers().get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (trailersList != null && trailersList.getTrailers() != null){
            return trailersList.getTrailers().size();
        }
        else{
            return 0;
        }
    }

    class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView trailerName;
        ImageView shareButton;

        TrailersViewHolder(View itemView) {
            super(itemView);
            trailerName = (TextView)itemView.findViewById(R.id.tv_trailer_name);
            shareButton = (ImageView)itemView.findViewById(R.id.iv_share_button);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mShareClickListener.onShareItemClick(getAdapterPosition());
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}


