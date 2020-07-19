package com.fightalarm.fightalarm.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.fragment.EventFragment;
import com.fightalarm.fightalarm.models.Category;
import com.fightalarm.fightalarm.models.Event;
import com.fightalarm.fightalarm.models.Notification;
import com.fightalarm.fightalarm.providers.Extras;
import com.fightalarm.fightalarm.providers.Setup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NotificationAdapter extends ArrayAdapter<Notification> {


    private ArrayList<Notification> notifications = new ArrayList<Notification>();

    public NotificationAdapter(Context context, ArrayList<Notification> notifications) {
        super(context, R.layout.notificationlayout);
        this.notifications = notifications;
    }

    @Override
    public int getCount() {
        return this.notifications.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;


        Extras extra = new Extras();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.notificationlayout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final Notification notification = this.notifications.get(position);
        holder.title.setText(notification.getTitle());
        holder.description.setText(notification.getDescription());

        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTimeInMillis(notification.getCreationdate());

        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month = month_date.format(notification.getCreationdate());

        Integer year = timeToCheck.get(Calendar.YEAR);
        Integer day = timeToCheck.get(Calendar.DAY_OF_MONTH);

        holder.description.setText(notification.getDescription());
        String dayinfo = month + " " + Integer.toString(day);
        holder.month.setText(dayinfo);
        holder.year.setText(Integer.toString(year));


//        if (notification.getRead()){
//        holder.read.setVisibility(View.INVISIBLE);
//        }

        convertView.setTag(holder);

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView month;
        TextView year;
        TextView description;

        ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.title);
            month = (TextView) view.findViewById(R.id.month);
            year = (TextView) view.findViewById(R.id.year);
            description = (TextView) view.findViewById(R.id.description);
        }
    }


}
