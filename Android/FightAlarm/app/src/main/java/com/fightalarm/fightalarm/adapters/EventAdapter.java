package com.fightalarm.fightalarm.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.ArrayAdapter;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.activity.EventDetailsActivity;
import com.fightalarm.fightalarm.models.Event;
import com.fightalarm.fightalarm.providers.BinderWrapperForEvent;
import com.fightalarm.fightalarm.providers.Database;
import com.fightalarm.fightalarm.providers.Extras;
import com.fightalarm.fightalarm.providers.Notification;
import com.fightalarm.fightalarm.providers.ScheduleLocalStore;
import com.fightalarm.fightalarm.providers.Setup;

import java.util.ArrayList;
import java.util.TreeSet;

public class EventAdapter extends ArrayAdapter<Event> {


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<Event> events = new ArrayList<Event>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    Extras extras = new Extras();
    Database database;

    public EventAdapter(Context context) {
        super(context, R.layout.cardlayout);
        database = new Database(context);
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

        int rowType = getItemViewType(position);

        final Event event = this.events.get(position);

        Extras extra = new Extras();
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


//        switch (rowType) {
//            case TYPE_ITEM:
        String title = event.getTitle();

//        holder.subscribers.setText(Integer.toString(event.getSubscribers()));
//        if (event.getNow_showing()) {
//            holder.days.setText(extra.countdown(event.getEventdate(), "number"));
//            holder.daystitle.setText(extra.countdown(event.getEventdate(), "label"));
//        } else {
//            holder.days.setText(extra.dateDifference(event.getEventdate()));
//        }
        holder.fname1.setText(event.getPlayer1fname());
        holder.lname1.setText(event.getPlayer1lname());
        holder.fname2.setText(event.getPlayer2fname());
        holder.lname2.setText(event.getPlayer2lname());
        holder.title.setText(title);
        if (event.getNow_showing() == Boolean.TRUE) {
            holder.button.setBackgroundColor(Color.parseColor("#999999"));
            holder.button.setText(R.string.showing);
            holder.button.setEnabled(Boolean.FALSE);
        } else if (event.getSubscribed() == Boolean.TRUE) {
            holder.button.setBackgroundColor(Color.parseColor("#e40004"));
            holder.button.setText(R.string.unsubscribe);
        } else {
            holder.button.setBackgroundColor(Color.parseColor("#537789"));
            holder.button.setText(R.string.subscribe);
        }


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event.getSubscribed() == Boolean.TRUE) {
                    updateEvent(event);
                } else {
                    updateEvent(event);
                }
            }
        });
        convertView.setTag(holder);
//                break;
//            case TYPE_SEPARATOR:
//                holder.header.setText(extra.formatHeader(event));
//                convertView.setTag(holder);
//                break;
//        }


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
        TextView title;
        TextView header;
        Button button;
        CardView cardView;
        RelativeLayout relativegrid;

        ViewHolder(View view) {
            subscribers = (TextView) view.findViewById(R.id.subscribers);
            days = (TextView) view.findViewById(R.id.days);
            daystitle = (TextView) view.findViewById(R.id.daystitle);
            fname1 = (TextView) view.findViewById(R.id.fname1);
            lname1 = (TextView) view.findViewById(R.id.lname1);
            fname2 = (TextView) view.findViewById(R.id.fname2);
            lname2 = (TextView) view.findViewById(R.id.lname2);
            title = (TextView) view.findViewById(R.id.title);
            header = (TextView) view.findViewById(R.id.textSeparator);
            cardView = (CardView) view.findViewById(R.id.cardView);
            relativegrid = (RelativeLayout) view.findViewById(R.id.relativegrid);


            button = (Button) view.findViewById(R.id.subscribebutton);
        }
    }

    public void updateEvent(Event event) {
        event.setSubscribed(!event.getSubscribed());
        Notification notification = new Notification(getContext());
        if (event.getSubscribed()) {
            notification.subscribeToTopic(event.getId());
        } else {
            notification.unsubscribeFromTopic(event.getId());
        }
        ScheduleLocalStore scheduleLocalStore = new ScheduleLocalStore(getContext());
        scheduleLocalStore.updateSingleEvent(event);
        notifyDataSetChanged();

    }

}
