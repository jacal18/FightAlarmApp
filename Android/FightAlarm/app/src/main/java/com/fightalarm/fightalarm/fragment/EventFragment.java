package com.fightalarm.fightalarm.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.activity.FilterActivity;
import com.fightalarm.fightalarm.adapters.EventAdapter;
import com.fightalarm.fightalarm.interfaces.EventCallback;
import com.fightalarm.fightalarm.models.Event;
import com.fightalarm.fightalarm.providers.BinderWrapperForEventList;
import com.fightalarm.fightalarm.providers.Database;
import com.fightalarm.fightalarm.providers.Extras;
import com.fightalarm.fightalarm.providers.Notification;
import com.fightalarm.fightalarm.providers.ScheduleLocalStore;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;

import org.joda.time.DateTime;

import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;


public class EventFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, DatePickerListener, View.OnClickListener {

    // TODO: Rename and change types of parameters

    private Notification notification;

    private HorizontalPicker dayScrollDatePicker;
    private SegmentedGroup selectionSegment;


    private ScheduleLocalStore scheduleLocalStore;

    ListView eventList;
    RelativeLayout relativeLayout;

    Extras extras = new Extras();
    Database database;

    RelativeLayout.LayoutParams params;
    View view;
    ArrayList<Event> events = new ArrayList<Event>();
    ArrayList<Event> oevents = new ArrayList<Event>();
    EditText searchText;
    Button cancel;
    Button filter;
    String category_id;
    String view_type;
    String sortby;
    Bundle homeintent;


    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_event, null, true);
        if (getArguments() != null) {
            homeintent = getArguments();
            category_id = getArguments().getString("category_id");
            view_type = getArguments().getString("viewtype");
            sortby = homeintent.getString("sortby");
        }

        dayScrollDatePicker = (HorizontalPicker) view.findViewById(R.id.day_date_picker);
        selectionSegment = (SegmentedGroup) view.findViewById(R.id.selection_segment);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativegrid);
        params = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
        eventList = (ListView) view.findViewById(R.id.eventListView);
        searchText = (EditText) view.findViewById(R.id.searchText);
        cancel = (Button) view.findViewById(R.id.clearfield);
        filter = (Button) view.findViewById(R.id.filterbutton);
        cancel.setOnClickListener(this);
        filter.setOnClickListener(this);

        notification = new Notification(this.getContext());
        scheduleLocalStore = new ScheduleLocalStore(this.getContext());
        database = new Database(this.getContext());

        this.setupObjects();

        return view;
    }

    public void setupHiddenDatePicker() {
        dayScrollDatePicker = (HorizontalPicker) view.findViewById(R.id.day_date_picker);
        dayScrollDatePicker.setListener(this).setDays(100)
                .setOffset(10)
                .setDateSelectedColor(Color.DKGRAY)
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(Color.DKGRAY)
                .setTodayButtonTextColor(getResources().getColor(R.color.colorPrimary))
                .setTodayDateTextColor(getResources().getColor(R.color.colorPrimary))
                .setTodayDateBackgroundColor(Color.GRAY)
                .setUnselectedDayTextColor(Color.DKGRAY)
                .setDayOfWeekTextColor(Color.DKGRAY)
                .setUnselectedDayTextColor(getResources().getColor(R.color.primaryTextColor))
                .showTodayButton(true)
                .init();

        dayScrollDatePicker.setDate(new DateTime());
        dayScrollDatePicker.setBackgroundColor(Color.LTGRAY);
    }

    public void setupObjects() {
        // initialize it and attach a listener

        this.setupHiddenDatePicker();

        selectionSegment.setOnCheckedChangeListener(this);

        searchText.addTextChangedListener(searchTextWatcher);

        database.getEvents(new EventCallback() {
            @Override
            public void onCallback(ArrayList<Event> newevents) {
                events = newevents;
                oevents = events;

                setupAdapter();
            }
        });

    }

    public void setupAdapter() {
        EventAdapter newevent = new EventAdapter(getContext());

        if (view_type != null && !view_type.isEmpty()) {
            if (view_type.equalsIgnoreCase("category")) {
                if (category_id != null && !category_id.isEmpty()) {
                    events = database.filterEventsByCategories(events, category_id);
                }
            } else {
                ArrayList<Event> newevents = ((BinderWrapperForEventList) homeintent.getBinder("Events")).getData();

                if (newevents != null) {
                    events = newevents;
                }
            }
        }

        if (sortby != null) {
            events = database.sortEvents(events, sortby);
        } else {
            events = database.sortEvents(events, "date");
        }
        newevent.addAll(events);

//        newevent = extras.formatEventList(newevent, events);


        eventList.setAdapter(newevent);
    }

    public void showViews(String type) {
        if (type.equalsIgnoreCase("date")) {
            dayScrollDatePicker.setClickable(true);
        } else {
            dayScrollDatePicker.setClickable(false);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.all_button:
                showViews("all");
                events = oevents;
                this.setupAdapter();
                break;
            case R.id.date_button:
                showViews("date");
                break;
            default:
                Log.d("All", "Selected");
        }
    }

    @Override
    public void onDateSelected(@NonNull final DateTime dateSelected) {
        // log it for demo
        Long date = dateSelected.getMillis();
        if (selectionSegment.getCheckedRadioButtonId() == R.id.date_button) {
            category_id = null;
            events = this.extras.queryList(oevents, date);
            this.setupAdapter();
        }
    }

    public TextWatcher searchTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence search, int start, int before, int count) {

            String text = search.toString();
            Log.d("output", text);
            if (text.isEmpty()) {
                events = oevents;
            } else {
                events = extras.searchList(oevents, text);
            }
            setupAdapter();
        }

        @Override
        public void afterTextChanged(Editable search) {
        }


        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };

    public void beginFilter() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clearfield:
                searchText.setText("");
                break;
            case R.id.filterbutton:
                Intent intent = new Intent(this.getContext(), FilterActivity.class);
                final Bundle bundle = new Bundle();
                bundle.putBinder("Events", new BinderWrapperForEventList(oevents));
                intent.putExtras(bundle);
                intent.putExtra("tabnumber", 1);
                intent.putExtra("viewtype", "events");
                startActivity(intent);
                break;

        }
    }

}
