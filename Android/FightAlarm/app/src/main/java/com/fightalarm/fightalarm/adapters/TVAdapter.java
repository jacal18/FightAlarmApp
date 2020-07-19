package com.fightalarm.fightalarm.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.activity.HomeActivity;
import com.fightalarm.fightalarm.helpers.DownloadImageHelper;
import com.fightalarm.fightalarm.models.TVStations;
import com.fightalarm.fightalarm.providers.Extras;
import com.fightalarm.fightalarm.providers.Setup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TVAdapter extends  RecyclerView.Adapter<TVAdapter.ViewHolder>{

    private ArrayList<TVStations> tvStations = new ArrayList<TVStations>();
    Context context;
    Extras extras = new Extras();

    public TVAdapter(Context context, ArrayList<TVStations> categories) {
        this.tvStations = categories;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        ImageView image;
        TextView title;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card);
            image = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
        }
    }


    @Override
    public TVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categorieslayout, parent, false);

        return new TVAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TVAdapter.ViewHolder holder, int position) {
        final TVStations category = this.tvStations.get(position);

        holder.title.setText(category.getTitle());
        if(!category.getImageURL().isEmpty()){
            Picasso.with(context).load(category.getImageURL()).placeholder(R.drawable.ic_live_tv_black_24dp).resize(50,50).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        if(tvStations != null){
            return tvStations.size();
        }
        return 0;
    }



}
