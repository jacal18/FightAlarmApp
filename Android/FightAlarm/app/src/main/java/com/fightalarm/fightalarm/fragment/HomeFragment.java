package com.fightalarm.fightalarm.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.activity.EventDetailsActivity;
import com.fightalarm.fightalarm.adapters.CategoryAdapter;
import com.fightalarm.fightalarm.adapters.RecommendationAdapter;
import com.fightalarm.fightalarm.interfaces.CategoryCallback;
import com.fightalarm.fightalarm.interfaces.EventCallback;
import com.fightalarm.fightalarm.models.Category;
import com.fightalarm.fightalarm.models.Event;
import com.fightalarm.fightalarm.providers.Database;
import com.fightalarm.fightalarm.providers.Extras;
import com.fightalarm.fightalarm.providers.ScheduleLocalStore;
import com.fightalarm.fightalarm.providers.Setup;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;


public class HomeFragment extends Fragment implements View.OnClickListener {

    View view;

    Event event;
    ScheduleLocalStore scheduleLocalStore;
    TextView fname1;
    TextView fname2;
    TextView location;
    TextView countdown;
    TextView countdown2;
    TextView daydtitle;
    TextView daydtitle2;
    TextView hourvalue;
    TextView hourtitle;
    TextView minutevalue;
    TextView minutetitle;
    TextView secondvalue;
    TextView secondtitle;
    TextView subscribers;
    TextView no_data;
    Button button;

    RelativeLayout relativegrid;
    RelativeLayout relativecount;
    RelativeLayout relativenext;

    RecyclerView categoriesList;
    ArrayList<Category> categories = new ArrayList<Category>();
    RecyclerView recommendationList;

    ArrayList<Event> events = new ArrayList<Event>();
    Extras extras;

    Database database;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null, true);
        extras = new Extras();
        scheduleLocalStore = new ScheduleLocalStore(this.getContext());

        relativegrid = (RelativeLayout) view.findViewById(R.id.relativegrid);
        relativenext = (RelativeLayout) view.findViewById(R.id.relativenext);
        relativecount = (RelativeLayout) view.findViewById(R.id.relativecount);
        fname1 = (TextView) view.findViewById(R.id.fname1);
        fname2 = (TextView) view.findViewById(R.id.fname2);
        location = (TextView) view.findViewById(R.id.location);
        countdown = (TextView) view.findViewById(R.id.countdown);
        countdown2 = (TextView) view.findViewById(R.id.countdown2);
        daydtitle = (TextView) view.findViewById(R.id.daydtitle);
        daydtitle2 = (TextView) view.findViewById(R.id.daydtitle2);
        hourvalue = (TextView) view.findViewById(R.id.hourvalue);
        hourtitle = (TextView) view.findViewById(R.id.hourtitle);
        minutevalue = (TextView) view.findViewById(R.id.minutevalue);
        minutetitle = (TextView) view.findViewById(R.id.minutetitle);
        secondvalue = (TextView) view.findViewById(R.id.secondvalue);
        secondtitle = (TextView) view.findViewById(R.id.secondtitle);
        subscribers = (TextView) view.findViewById(R.id.subscribers);
        no_data = (TextView) view.findViewById(R.id.no_data);
        button = (Button) view.findViewById(R.id.subscribebutton);
        categoriesList = (RecyclerView) view.findViewById(R.id.categoryList);
        recommendationList = (RecyclerView) view.findViewById(R.id.recList);
        database = new Database(this.getContext());
        button.setOnClickListener(this);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoriesList.setLayoutManager(layoutManager);
        LinearLayoutManager redLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recommendationList.setLayoutManager(redLayoutManager);

        this.setup();


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        this.updateEvent();
    }

    public void setup() {
        final Context context = this.getContext();
        database.getCategories(new CategoryCallback() {
            @Override
            public void onCallback(ArrayList<Category> categories) {
                CategoryAdapter categoryAdapter = new CategoryAdapter(context, categories);
                categoriesList.setAdapter(categoryAdapter);
            }
        });

        database.getEvents(new EventCallback() {
            @Override
            public void onCallback(ArrayList<Event> events) {

                scheduleLocalStore.updateStorage(events);
                ArrayList<Event> events1 = database.filterEventsBySubscription(events);
                events1 = database.filterEventsByNowShowing(events1);
                Integer end = 4;
                if (events1.size() < 4) {
                    end = events1.size();
                }
                events = new ArrayList<Event>(events1.subList(0, end));

                RecommendationAdapter recommendationAdapter = new RecommendationAdapter(context, events);
                recommendationList.setAdapter(recommendationAdapter);

                setupShow();
            }
        });

    }

    public void setupShow(){
        ArrayList<Event> events = scheduleLocalStore.getScheduleDataGreaterThanToday();
        if (events.size() > 0) {
            event = Collections.min(events, new Comparator<Event>() {
                public int compare(Event event1, Event event2) {
                    return Long.valueOf(event1.getEventdate()).compareTo(event2.getEventdate());
                }
            });

            if (this.extras.checkGreaterThanToday(event.getEventdate())) {
                no_data.setVisibility(View.INVISIBLE);
                relativegrid.setVisibility(View.VISIBLE);
                fname1.setText(event.getPlayer1fname());
                fname2.setText(event.getPlayer2fname());
                String event_location = event.getEventstate() + ", " + event.getEventcountry();
                location.setText(event_location);
                subscribers.setText(Integer.toString(event.getSubscribers()));
                if (event.getNow_showing()) {
                    button.setText("Directions");
                    relativecount.setVisibility(View.VISIBLE);
                    relativenext.setVisibility(View.INVISIBLE);
                    countDownStart();
                } else {
                    relativecount.setVisibility(View.INVISIBLE);
                    relativenext.setVisibility(View.VISIBLE);
                    button.setText("Unsubscribe");

                    countDown2Start();
                }
            } else {
                relativegrid.setVisibility(View.INVISIBLE);
                no_data.setVisibility(View.VISIBLE);
            }

        } else {
            relativegrid.setVisibility(View.INVISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }
    }

    public void countDownStart() {
        final Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            private long time = 0;

            @Override
            public void run()
            {
                // do stuff then
                // can call h again after work!
                time += 1000;

                countdown.setText(extras.getCountdownNumbers(event.getEventdate(), "days"));
                hourvalue.setText(extras.getCountdownNumbers(event.getEventdate(), "hours"));
                minutevalue.setText(extras.getCountdownNumbers(event.getEventdate(), "minutes"));
                secondvalue.setText(extras.getCountdownNumbers(event.getEventdate(), "seconds"));
                h.postDelayed(this, 1000);
            }
        }, 1000); // 1 second delay (takes millis)
    }

    public void countDown2Start() {
        final Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            private long time = 0;

            @Override
            public void run()
            {
                // do stuff then
                // can call h again after work!
                time += 86400000;
                countdown2.setText(extras.dateDifference(event.getEventdate()));
                h.postDelayed(this, 86400000);
            }
        }, 86400000); // 1 second delay (takes millis)
    }

    public void updateEvent() {
        if (this.event.getNow_showing() == Boolean.TRUE) {
            String locationdetails = this.event.getEventaddress() + ", " + this.event.getEventcity() + ", " + this.event.getEventstate() + ". " + this.event.getEventcountry();

            String url = "https://maps.google.co.in/maps?q=" + locationdetails;
            extras.openURL(getContext(), url);
        } else {
            this.event.setSubscribed(false);
            ScheduleLocalStore scheduleLocalStore = new ScheduleLocalStore(getContext());
            scheduleLocalStore.updateSingleEvent(this.event);
            this.extras.openDialog(this.event, this.getContext());
            this.setup();
        }
    }


}
