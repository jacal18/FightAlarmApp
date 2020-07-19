package com.fightalarm.fightalarm.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.activity.EventDetailsActivity;
import com.fightalarm.fightalarm.activity.HomeActivity;
import com.fightalarm.fightalarm.models.Event;
import com.fightalarm.fightalarm.providers.Extras;
import com.fightalarm.fightalarm.providers.Notification;
import com.fightalarm.fightalarm.providers.ScheduleLocalStore;
import com.fightalarm.fightalarm.providers.Setup;

import java.util.ArrayList;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {


    private ArrayList<Event> events = new ArrayList<Event>();
    Context context;
    Extras extras = new Extras();

    public RecommendationAdapter(Context context, ArrayList<Event> eventList) {
        this.events = eventList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView days, fname1,  fname2, state, country;
        public Button button;
        public CardView recCard;

        public ViewHolder(View view) {
            super(view);
            days = (TextView) view.findViewById(R.id.days);
            fname1 = (TextView) view.findViewById(R.id.fname1);
            fname2 = (TextView) view.findViewById(R.id.fname2);
            state = (TextView) view.findViewById(R.id.state);
            country = (TextView) view.findViewById(R.id.country);

            button = (Button) view.findViewById(R.id.schedule);
            recCard = (CardView) view.findViewById(R.id.recCard);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reclayout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Event event = this.events.get(position);
        if (event.getSubscribed()) {
            holder.recCard.setVisibility(View.GONE);
        } else {

            String days = "In " + this.extras.dateDifference(event.getEventdate()) + " day(s)";

            holder.days.setText(days);
            holder.fname1.setText(event.getPlayer1fname());
            holder.fname2.setText(event.getPlayer2fname());
            holder.state.setText(event.getEventstate());
            holder.country.setText(event.getEventcountry());

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateEvent(event);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void updateEvent(Event event) {
        events.remove(event);
        event.setSubscribed(true);
        Notification notification = new Notification(this.context);
        if(event.getSubscribed()){
            notification.subscribeToTopic(event.getTitle());
        } else {
            notification.unsubscribeFromTopic(event.getTitle());
        }
        ScheduleLocalStore scheduleLocalStore = new ScheduleLocalStore(this.context);
        scheduleLocalStore.updateSingleEvent(event);
        notifyDataSetChanged();

        Intent i = new Intent(this.context, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.context.getApplicationContext().startActivity(i);
    }


}
