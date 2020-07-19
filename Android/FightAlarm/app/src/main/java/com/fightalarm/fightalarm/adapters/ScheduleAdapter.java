package com.fightalarm.fightalarm.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.activity.EventDetailsActivity;
import com.fightalarm.fightalarm.activity.HomeActivity;
import com.fightalarm.fightalarm.models.Event;
import com.fightalarm.fightalarm.providers.BinderWrapperForEvent;
import com.fightalarm.fightalarm.providers.Extras;
import com.fightalarm.fightalarm.providers.Notification;
import com.fightalarm.fightalarm.providers.ScheduleLocalStore;
import com.fightalarm.fightalarm.providers.Setup;

import java.util.ArrayList;
import java.util.TreeSet;

public class ScheduleAdapter extends ArrayAdapter<Event> {


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<Event> events = new ArrayList<Event>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    Extras extras = new Extras();

    public ScheduleAdapter(Context context, ArrayList<Event> events) {
        super(context, R.layout.cardlayout);
        this.events = events;
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    public void addItem(final Event event) {
        events.add(event);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final Event event) {
        events.add(event);
        sectionHeader.add(events.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        final Event event = this.events.get(position);

        int rowType = getItemViewType(position);

        Extras extra = new Extras();
        if(event.getSubscribed()) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                switch (rowType) {
                    case TYPE_SEPARATOR:
                        convertView = inflater.inflate(R.layout.list_header, null);
                        holder = new ViewHolder(convertView);
                        convertView.setTag(holder);
                        break;
                    default:
                        convertView = inflater.inflate(R.layout.cardlayout, null);
                        holder = new ViewHolder(convertView);
                        convertView.setTag(holder);
                        break;
                }

            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            switch (rowType) {
                case TYPE_ITEM:
                    if (!event.getSubscribed()) {
                        holder.cardView.setVisibility(View.GONE);
                    } else {
                        String location = event.getEventstate() + ", " + event.getEventcountry();

                        holder.subscribers.setText(Integer.toString(event.getSubscribers()));
                        if(event.getNow_showing()){
                            holder.days.setText(extra.countdown(event.getEventdate(), "number"));
                            holder.daystitle.setText(extra.countdown(event.getEventdate(), "label"));
                        } else {
                            holder.days.setText(extra.dateDifference(event.getEventdate()));
                        }
                        holder.fname1.setText(event.getPlayer1fname());
                        holder.lname1.setText(event.getPlayer1lname());
                        holder.fname2.setText(event.getPlayer2fname());
                        holder.lname2.setText(event.getPlayer2lname());
                        holder.location.setText(location);
                        if (event.getNow_showing() == Boolean.TRUE) {
                            holder.button.setText(R.string.directions);
                        } else if (event.getSubscribed() == Boolean.TRUE) {
                            holder.button.setText(R.string.unsubscribe);
                        } else {
                            holder.button.setText(R.string.subscribe);
                        }

                        holder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                navigate(event);
                            }
                        });


                        holder.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (event.getNow_showing() == Boolean.TRUE) {
                                    String locationdetails = event.getEventaddress() + ", " + event.getEventcity() + ", " + event.getEventstate() + ". " + event.getEventcountry();

                                    String url = "https://maps.google.co.in/maps?q=" + locationdetails;
                                    extras.openURL(getContext(), url);
                                } else if (event.getSubscribed() == Boolean.TRUE) {
                                    updateEvent(event);
                                } else {
                                    updateEvent(event);
                                }
                            }
                        });
                    }
                    convertView.setTag(holder);
                    break;
                case TYPE_SEPARATOR:
                    if (!event.getSubscribed()) {
                        holder.header.setVisibility(View.GONE);
                    } else {
                        holder.header.setText(extra.formatHeader(event));
                    }
                    convertView.setTag(holder);
                    break;
            }
        }

        return convertView;
    }

    static class ViewHolder {
        TextView subscribers;
        TextView days;
        TextView daystitle;
        TextView fname1;
        TextView lname1;
        TextView fname2;
        TextView lname2;
        TextView location;
        TextView header;
        Button button;
        CardView cardView;

        ViewHolder(View view) {
            subscribers = (TextView) view.findViewById(R.id.subscribers);
            days = (TextView) view.findViewById(R.id.days);
            daystitle = (TextView) view.findViewById(R.id.daystitle);
            fname1 = (TextView) view.findViewById(R.id.fname1);
            lname1 = (TextView) view.findViewById(R.id.lname1);
            fname2 = (TextView) view.findViewById(R.id.fname2);
            lname2 = (TextView) view.findViewById(R.id.lname2);
            location = (TextView) view.findViewById(R.id.location);
            header = (TextView) view.findViewById(R.id.textSeparator);
            cardView = (CardView) view.findViewById(R.id.cardView);

            button = (Button) view.findViewById(R.id.subscribebutton);
        }
    }

    public void updateEvent(Event event) {
        event.setSubscribed(!event.getSubscribed());
        Notification notification = new Notification(getContext());
        if (event.getSubscribed()) {
            notification.subscribeToTopic(event.getTitle());
        } else {
            notification.unsubscribeFromTopic(event.getTitle());
        }
        ScheduleLocalStore scheduleLocalStore = new ScheduleLocalStore(getContext());
        scheduleLocalStore.updateSingleEvent(event);
        events = scheduleLocalStore.getScheduleData();
        notifyDataSetChanged();
    }

    public void navigate(Event event) {
        Intent i = new Intent(getContext(), EventDetailsActivity.class);
        final Bundle bundle = new Bundle();
        bundle.putBinder("Event", new BinderWrapperForEvent(event));
        i.putExtras(bundle);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().getApplicationContext().startActivity(i);
    }


}
