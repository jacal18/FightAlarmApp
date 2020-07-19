package com.fightalarm.fightalarm.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.adapters.CategoryAdapter;
import com.fightalarm.fightalarm.adapters.TVAdapter;
import com.fightalarm.fightalarm.models.Event;
import com.fightalarm.fightalarm.providers.BinderWrapperForEvent;
import com.fightalarm.fightalarm.providers.Extras;
import com.fightalarm.fightalarm.providers.ScheduleLocalStore;
import com.fightalarm.fightalarm.providers.Setup;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

public class EventDetailsActivity extends AppCompatActivity {

    Extras extras = new Extras();
    TextView date;
    TextView location;
    TextView subtitle;
    TextView subscribers;
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
    TextView days;
    TextView daystitle;
    TextView timetitle;
    TextView departure;
    TextView duration;
    TextView arrival;
    RecyclerView tvListView;
    Button directionButton;
    Button subscribeButton;
    MapView mapView;
    GoogleMap map;

    RelativeLayout relativecount;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();

        event = ((BinderWrapperForEvent) intent.getExtras().getBinder("Event")).getData();

        this.setupWidgets();
    }

    public void setupWidgets() {
        date = (TextView) findViewById(R.id.title);
        location = (TextView) findViewById(R.id.location);
        subscribers = (TextView) findViewById(R.id.subscribers);
        subtitle = (TextView) findViewById(R.id.subtitle);
        countdown = (TextView) findViewById(R.id.countdown);
        countdown2 = (TextView) findViewById(R.id.countdown2);
        daydtitle = (TextView) findViewById(R.id.daydtitle);
        daydtitle2 = (TextView) findViewById(R.id.daydtitle2);
        hourvalue = (TextView) findViewById(R.id.hourvalue);
        hourtitle = (TextView) findViewById(R.id.hourtitle);
        minutevalue = (TextView) findViewById(R.id.minutevalue);
        minutetitle = (TextView) findViewById(R.id.minutetitle);
        secondvalue = (TextView) findViewById(R.id.secondvalue);
        secondtitle = (TextView) findViewById(R.id.secondtitle);
        days = (TextView) findViewById(R.id.days);
        daystitle = (TextView) findViewById(R.id.daystitle);
        timetitle = (TextView) findViewById(R.id.timetitle);
        departure = (TextView) findViewById(R.id.departure);
        duration = (TextView) findViewById(R.id.timing);
        arrival = (TextView) findViewById(R.id.arrival);
        directionButton = (Button) findViewById(R.id.directions);
        subscribeButton = (Button) findViewById(R.id.subscribe);
        tvListView = (RecyclerView) findViewById(R.id.tvlist);
        mapView = (MapView) findViewById(R.id.mapView);
        relativecount = (RelativeLayout) findViewById(R.id.relativecount);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tvListView.setLayoutManager(layoutManager);

        this.setupView();
    }

    public void setupView() {

        if (event != null) {
            String title = event.getPlayer1fname() + " vs " + event.getPlayer2fname();

            try {
                getSupportActionBar().setTitle(title);
            } catch (Exception e) {

            }
            date.setText(extras.extractDateFormat(event.getEventdate()));
            String locationdetails = event.getEventaddress() + ", " + event.getEventcity() + ", " + event.getEventstate() + ". " + event.getEventcountry();
            location.setText(locationdetails);
            if (event.getNow_showing()) {
                subtitle.setVisibility(View.INVISIBLE);
                subscribers.setVisibility(View.INVISIBLE);
                subscribeButton.setVisibility(View.INVISIBLE);
                relativecount.setVisibility(View.VISIBLE);
                days.setVisibility(View.INVISIBLE);
                daystitle.setVisibility(View.INVISIBLE);
                timetitle.setVisibility(View.VISIBLE);

                countDownStart();
            } else {
                subtitle.setVisibility(View.VISIBLE);
                subscribers.setVisibility(View.VISIBLE);
                subscribeButton.setVisibility(View.VISIBLE);
                relativecount.setVisibility(View.INVISIBLE);
                subscribers.setText(Integer.toString(event.getSubscribers()));
                days.setVisibility(View.VISIBLE);
                daystitle.setVisibility(View.VISIBLE);
                timetitle.setVisibility(View.INVISIBLE);
                countDown2Start();
                setSubscribeButton(event);

                subscribeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateEvent(event);
                    }
                });
            }

            TVAdapter tvAdapter = new TVAdapter(this, event.getTvstations());
            tvListView.setAdapter(tvAdapter);


            directionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigate(event);
                }
            });
            this.setupMap();
        } else {
            Toast.makeText(this, "We could not show this information at the moment..", Toast.LENGTH_SHORT).show();
        }


    }

    public void setSubscribeButton(Event event) {
        if (event.getSubscribed()) {
            subscribeButton.setText(R.string.unsubscribe);
        } else {
            subscribeButton.setText(R.string.subscribe);
        }
    }

    public void navigate(Event event) {

        String locationdetails = event.getEventaddress() + ", " + event.getEventcity() + ", " + event.getEventstate() + ". " + event.getEventcountry();

        String url = "https://maps.google.co.in/maps?q=" + locationdetails;
        extras.openURL(this.getApplicationContext(), url);
    }

    public void updateEvent(Event event) {
        event.setSubscribed(!event.getSubscribed());
        ScheduleLocalStore scheduleLocalStore = new ScheduleLocalStore(this);
        scheduleLocalStore.updateSingleEvent(event);
        if (event.getSubscribed()) {
            Toast.makeText(this, "Successfully subscribed to fight between " + event.getPlayer1fname() + " vs " + event.getPlayer2fname() + ".", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Successfully unsubscribed from fight between " + event.getPlayer1fname() + " vs " + event.getPlayer2fname() + ".", Toast.LENGTH_SHORT).show();
        }
        setSubscribeButton(event);
    }

    public void setupMap() {
        if (mapView != null) {

            try {
                mapView.onCreate(null);  //Don't forget to call onCreate after get the mapView!


                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        map = googleMap;
                        //Do what you want with the map!!
                    }
                });

                MapsInitializer.initialize(this);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to initialize map correctly, please contact admin.", Toast.LENGTH_LONG).show();
            }
        } else {
            System.out.println("onCreateView: mapView is null");
        }
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void countDownStart() {
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            private long time = 0;

            @Override
            public void run() {
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
        h.postDelayed(new Runnable() {
            private long time = 0;

            @Override
            public void run() {
                // do stuff then
                // can call h again after work!
                time += 86400000;
                days.setText(extras.dateDifference(event.getEventdate()));
                h.postDelayed(this, 86400000);
            }
        }, 86400000); // 1 second delay (takes millis)
    }
}


