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
import com.fightalarm.fightalarm.activity.HomeActivity;
import com.fightalarm.fightalarm.adapters.ScheduleAdapter;

import com.fightalarm.fightalarm.models.Event;

import com.fightalarm.fightalarm.providers.BinderWrapperForEventList;
import com.fightalarm.fightalarm.providers.Database;
import com.fightalarm.fightalarm.providers.Extras;

import com.fightalarm.fightalarm.providers.ScheduleLocalStore;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;

import org.joda.time.DateTime;

import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;

public class ScheduleFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, DatePickerListener, View.OnClickListener {


    private HorizontalPicker dayScrollDatePicker;
    private SegmentedGroup selectionSegment;

    private ScheduleLocalStore scheduleLocalStore;

    ListView scheduleList;
    RelativeLayout relativeLayout;

    Extras extras = new Extras();
    Database database;

    RelativeLayout.LayoutParams params;
    View view;

    ArrayList<Event> schedules = new ArrayList<Event>();
    ArrayList<Event> oschedules = new ArrayList<Event>();;
    EditText searchText;
    Button cancel;
    Button filter;
    String view_type;
    String sortby;
    Bundle homeintent;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_schedule, null, true);
        dayScrollDatePicker = (HorizontalPicker) view.findViewById(R.id.day_date_picker);
        selectionSegment = (SegmentedGroup) view.findViewById(R.id.selection_segment);
        scheduleList = (ListView) view.findViewById(R.id.scheduleList);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativegrid);
        params = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
        searchText = (EditText) view.findViewById(R.id.searchText);
        cancel = (Button) view.findViewById(R.id.clearfield);
        filter = (Button) view.findViewById(R.id.filterbutton);
        cancel.setOnClickListener(this);
        filter.setOnClickListener(this);

        scheduleLocalStore = new ScheduleLocalStore(this.getContext());
        if (getArguments() != null) {
            homeintent = getArguments();
            view_type = homeintent.getString("viewtype");
            sortby = homeintent.getString("sortby");
        }
        database = new Database(this.getContext());


        this.setupHiddenDatePicker();

        selectionSegment.setOnCheckedChangeListener(this);

        this.setupObjects();

        return view;
    }

    @Override
    //Pressed return button - returns to the results menu
    public void onResume() {
        super.onResume();
        this.setupObjects();
    }

    public void setupHiddenDatePicker() {
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

        schedules = scheduleLocalStore.getScheduleDataGreaterThanToday();

        if (view_type != null && !view_type.isEmpty()){
            ArrayList<Event> newschedules = ((BinderWrapperForEventList) homeintent.getBinder("Schedules")).getData();
            if (newschedules != null){
                schedules = newschedules;
            }

            if(sortby != null){
                schedules = database.sortEvents(schedules, sortby);
            }
        }

        oschedules = schedules;
        this.setupAdapter();

    }

    public void setupAdapter(){
        ScheduleAdapter newevent = new ScheduleAdapter(getContext(), schedules);
        scheduleList.setAdapter(newevent);
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
                schedules = oschedules;
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
        if (selectionSegment.getCheckedRadioButtonId() == R.id.date_button){
            schedules = this.extras.queryList(oschedules, date);
            this.setupAdapter();
        }

    }

    public TextWatcher searchTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence search, int start, int before, int count) {

            String text = search.toString();
            if (text.isEmpty()){
                schedules = oschedules;
                cancel.setVisibility(View.INVISIBLE);
            } else {
                schedules = extras.searchList(oschedules, text);
                cancel.setVisibility(View.VISIBLE);
            }
            setupAdapter();
        }

        @Override
        public void afterTextChanged(Editable search) {
        }


        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clearfield:
                searchText.setText("");
                break;
            case R.id.filterbutton:
                Intent intent = new Intent(this.getContext(), FilterActivity.class);
                final Bundle bundle = new Bundle();
                bundle.putBinder("Events", new BinderWrapperForEventList(oschedules));
                intent.putExtras(bundle);
                intent.putExtra("tabnumber", 2);
                intent.putExtra("viewtype", "schedules");
                startActivity(intent);
                break;

        }
    }

}
