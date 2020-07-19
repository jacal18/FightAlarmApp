package com.fightalarm.fightalarm.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.adapters.NotificationAdapter;
import com.fightalarm.fightalarm.interfaces.NotificationCallback;
import com.fightalarm.fightalarm.models.Notification;
import com.fightalarm.fightalarm.providers.Database;
import com.fightalarm.fightalarm.providers.NotificationLocalStore;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;


import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    SlideAndDragListView listView;

    NotificationLocalStore notificationLocalStore;

    ArrayList<Notification> notifications = new ArrayList<Notification>();
    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Notifications");
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        listView = (SlideAndDragListView) findViewById(R.id.notifications);
        notificationLocalStore = new NotificationLocalStore(this);
        database = new Database(this.getApplicationContext());
        this.setupMenu();
        this.setupList();
    }

    public void setupMenu() {
        Menu menu = new Menu(true, 0);//the first parameter is whether can slide over

        listView.setMenu(menu);
    }

    public void setupList(){
        database.getNotifications(new NotificationCallback() {
            @Override
            public void onCallback(ArrayList<Notification> notifications) {
                NotificationAdapter notificationAdapter = new NotificationAdapter(getApplicationContext(), notifications);

                listView.setAdapter(notificationAdapter);

                listView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        listView.removeOnLayoutChangeListener(this);
                        notificationLocalStore.updateNotifications();
                    }
                });

                notificationAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
