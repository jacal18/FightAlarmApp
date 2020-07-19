package com.fightalarm.fightalarm.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.adapters.CategoryAdapter;
import com.fightalarm.fightalarm.adapters.EventAdapter;
import com.fightalarm.fightalarm.interfaces.CategoryCallback;
import com.fightalarm.fightalarm.interfaces.EventCallback;
import com.fightalarm.fightalarm.models.Category;
import com.fightalarm.fightalarm.models.Event;
import com.fightalarm.fightalarm.models.Subscriber;
import com.fightalarm.fightalarm.providers.Alarm;
import com.fightalarm.fightalarm.providers.AlarmReceiver;
import com.fightalarm.fightalarm.providers.BinderWrapperForEventList;
import com.fightalarm.fightalarm.providers.ConnectivityReceiver;
import com.fightalarm.fightalarm.providers.Database;
import com.fightalarm.fightalarm.providers.Extras;
import com.fightalarm.fightalarm.providers.Notification;
import com.fightalarm.fightalarm.providers.NotificationLocalStore;
import com.fightalarm.fightalarm.providers.OtherLocalStore;
import com.fightalarm.fightalarm.providers.ScheduleLocalStore;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.leolin.shortcutbadger.ShortcutBadger;

public class HomeActivity extends AppCompatActivity {

    private TextView mTextMessage;

    //Alarm Request Code
    private static final int ALARM_REQUEST_CODE = 133;
    private PendingIntent pendingIntent;
    private Alarm alarm;
    private Notification notification;
    private Integer count;
    private NotificationLocalStore notificationLocalStore;
    BottomNavigationView navigation;
    TextView countView;
    Intent homeintent;

    private OtherLocalStore otherLocalStore;
    private Database database;


    ArrayList<Event> events = new ArrayList<Event>();
    ArrayList<Event> oevents = new ArrayList<Event>();
    SearchView searchText;
    Extras extras = new Extras();
    ListView eventList;
    String category_id;
    String view_type;
    TextView no_data;
    Context context;

    RecyclerView categoriesList;
    ArrayList<Category> categories = new ArrayList<Category>();

    private ScheduleLocalStore scheduleLocalStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeintent = getIntent();
        context = getApplicationContext();

        FirebaseApp.initializeApp(this);


        eventList = (ListView) findViewById(R.id.eventListView);
        categoriesList = (RecyclerView) findViewById(R.id.categoryList);
        searchText = (SearchView) findViewById(R.id.searchText);
        no_data = (TextView) findViewById(R.id.no_data);

        notificationLocalStore = new NotificationLocalStore(this);
        otherLocalStore = new OtherLocalStore(this);
        database = new Database(this);

        scheduleLocalStore = new ScheduleLocalStore(this.getApplicationContext());

        this.setUpUser();

        category_id = homeintent.getStringExtra("category_id");
        view_type = homeintent.getStringExtra("viewtype");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        categoriesList.setLayoutManager(layoutManager);

        this.setupObjects();

        notification = new Notification(this.getApplicationContext());


        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(HomeActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, ALARM_REQUEST_CODE, alarmIntent, 0);
        alarm = new Alarm(pendingIntent, getApplicationContext());

        this.cancelNotifications();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        final View notification = menu.findItem(R.id.notification_nav).getActionView();

        countView = (TextView) notification.findViewById(R.id.txtCount);
        updateBadgeCount();

        countView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    TODO
                navigate();
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        noinspection SimplifiableIfStatement
        if (id == R.id.notification_nav) {
            if (new ConnectivityReceiver(getApplicationContext()).isConnectedToInternet()) {

                Intent i = new Intent(this, NotificationActivity.class);
                // sending data to new activity
                startActivity(i);


            } else {
                Toast.makeText(context, "You are not currently connected to the Internet, to view this page, please connect to the internet. ", Toast.LENGTH_LONG).show();

            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateBadgeCount() {
        count = notificationLocalStore.countUnread();
        if (count < 0) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (count == 0)
                    countView.setVisibility(View.GONE);
                else {
                    countView.setVisibility(View.VISIBLE);
                    countView.setText(Integer.toString(count));
                }
            }
        });


    }

    public void navigate() {
        Intent i = new Intent(this, NotificationActivity.class);
        // sending data to new activity
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Toast.makeText(getApplicationContext(),"onBackPressed",Toast.LENGTH_SHORT).show();
        updateBadgeCount();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.cancelNotifications();
    }

    public void cancelNotifications() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        otherLocalStore.setBadge(0);

        boolean applied = ShortcutBadger.applyCount(this, 0);
        alarm.stopAlarmManager();
        Log.d("Successful", Boolean.toString(applied));
    }

    public void setUpUser() {
        String user = otherLocalStore.getUserInfo();

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String idvalue = Long.toString(date.getTime());
        if (user.isEmpty()) {
            user = "User" + idvalue;
            otherLocalStore.setUserInfo(user);

            database.saveNotification(new com.fightalarm.fightalarm.models.Notification(idvalue, idvalue, "New User Added", "New User " + user + " joined", "all", "admin", date.getTime(), Boolean.FALSE));
            database.saveSubscribers(new Subscriber(idvalue, user, 0, date.getTime()));
        }

    }

    public void setupObjects() {
        // initialize it and attach a listener

        searchText.setOnQueryTextListener(searchTextWatcher);

        database.getEvents(new EventCallback() {
            @Override
            public void onCallback(ArrayList<Event> newevents) {
                events = newevents;
                oevents = events;

                setupAdapter();
            }
        });

        database.getCategories(new CategoryCallback() {
            @Override
            public void onCallback(ArrayList<Category> categories) {
                CategoryAdapter categoryAdapter = new CategoryAdapter(context, categories);
                categoriesList.setAdapter(categoryAdapter);
            }
        });

    }

    public void setupAdapter() {
        EventAdapter newevent = new EventAdapter(context);
        if (events.size() > 0) {
            no_data.setVisibility(View.INVISIBLE);
            if (view_type != null && !view_type.isEmpty()) {
                if (view_type.equalsIgnoreCase("category")) {
                    if (category_id != null && !category_id.isEmpty()) {
                        events = database.filterEventsByCategories(events, category_id);
                    }
                } else {
                    ArrayList<Event> newevents = ((BinderWrapperForEventList) homeintent.getExtras().getBinder("Events")).getData();

                    if (newevents != null) {
                        events = newevents;
                    }
                }
            }

            newevent.addAll(events);

            newevent = extras.formatEventList(newevent, events);


            eventList.setAdapter(newevent);
        } else {
            no_data.setVisibility(View.VISIBLE);
        }
    }

    public SearchView.OnQueryTextListener searchTextWatcher = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextChange(String search) {

            String text = search;

            if (text.isEmpty()) {
                events = oevents;
            } else {
                events = extras.searchList(oevents, text);
            }
            setupAdapter();
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };

}