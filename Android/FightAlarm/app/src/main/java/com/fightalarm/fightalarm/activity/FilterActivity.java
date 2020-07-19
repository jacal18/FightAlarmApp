package com.fightalarm.fightalarm.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edmodo.rangebar.RangeBar;
import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.adapters.CategoryFilterAdapter;
import com.fightalarm.fightalarm.fragment.EventFragment;
import com.fightalarm.fightalarm.fragment.HomeFragment;
import com.fightalarm.fightalarm.fragment.ScheduleFragment;
import com.fightalarm.fightalarm.helpers.FilterSeekBar;
import com.fightalarm.fightalarm.interfaces.CategoryCallback;
import com.fightalarm.fightalarm.interfaces.CategoryClick;
import com.fightalarm.fightalarm.models.Category;
import com.fightalarm.fightalarm.models.Event;
import com.fightalarm.fightalarm.models.Filter;
import com.fightalarm.fightalarm.providers.BinderWrapperForEvent;
import com.fightalarm.fightalarm.providers.BinderWrapperForEventList;
import com.fightalarm.fightalarm.providers.ConnectivityReceiver;
import com.fightalarm.fightalarm.providers.Database;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, CategoryClick, RangeBar.OnRangeBarChangeListener {

    Spinner sort;
    EditText startdate;
    EditText enddate;
    TextView subscribersleft;
    TextView subscribersright;
    TextView dayslefttitle;
    TextView daysrighttitle;
    RangeBar subscribers;
    RangeBar days;
    Button apply;
    Button cancel;
    Filter filter;
    RecyclerView categoriesList;

    Integer selected;
    Intent intent;
    ArrayList<Event> events;
    ArrayList<Category> categories;

    private DatePickerDialog datePickerDialog;
    private DatePickerDialog datePickerDialog2;
    private SimpleDateFormat simpleDateFormat;

    Long startdatelong;
    Long enddatelong;

    Integer subscribersno;
    Integer subscribersrightno;
    Integer daysno;
    Integer daysrightno;
    String sortvalue = "fname";
    String selectedcategory = "";

    Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


        intent = getIntent();

        events = ((BinderWrapperForEventList) intent.getExtras().getBinder("Events")).getData();

        sort = (Spinner) findViewById(R.id.sortdropdown);
        startdate = (EditText) findViewById(R.id.startdate);
        enddate = (EditText) findViewById(R.id.enddate);
        subscribersleft = (TextView) findViewById(R.id.subscribersleft);
        subscribersright = (TextView) findViewById(R.id.subscribersright);
        dayslefttitle = (TextView) findViewById(R.id.dayslefttitle);
        daysrighttitle = (TextView) findViewById(R.id.daysrighttitle);
        subscribers = (RangeBar) findViewById(R.id.subscribersrange);
        days = (RangeBar) findViewById(R.id.daysrange);
        apply = (Button) findViewById(R.id.applybutton);
        cancel = (Button) findViewById(R.id.cancelbutton);
        categoriesList = (RecyclerView) findViewById(R.id.categoryList);
        database = new Database(this);

        apply.setOnClickListener(this);
        cancel.setOnClickListener(this);
        startdate.setOnClickListener(this);
        enddate.setOnClickListener(this);
        subscribers.setOnRangeBarChangeListener(this);
        days.setOnRangeBarChangeListener(this);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        categoriesList.setLayoutManager(layoutManager);

        selected = intent.getIntExtra("tabnumber", 0);
        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        setFilter();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cancel, menu);

        final View cancel = menu.findItem(R.id.cancel_nav).getActionView();

        cancel.setOnClickListener(this);

        return true;
    }


    public void setFilter() {
        String[] items = new String[]{"First Name", "Last Name", "Address", "State", "Country", "Event Date"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sort.setAdapter(adapter);
        sort.setOnItemSelectedListener(this);
        setStartDatePicker();
        setEndDatePicker();
        setSeekBars();

        final Context context = this;
        final CategoryClick listener = this;

        database.getCategories(new CategoryCallback() {
            @Override
            public void onCallback(ArrayList<Category> categories) {
                CategoryFilterAdapter categoryAdapter = new CategoryFilterAdapter(context, categories, listener);
                categoriesList.setAdapter(categoryAdapter);
            }
        });

    }

    public void setSeekBars() {
        subscribers.setTickCount(100);
        subscribers.setTickHeight(10);
        subscribers.setConnectingLineColor(R.color.backgroundText);
        subscribers.setBarColor(R.color.backgroundText);
        subscribers.setThumbColorNormal(R.color.backgroundText);
        subscribers.setThumbColorPressed(R.color.backgroundDarkerText);

        days.setTickCount(100);
        days.setTickHeight(10);
        days.setConnectingLineColor(R.color.backgroundText);
        days.setBarColor(R.color.backgroundText);
        days.setThumbColorNormal(R.color.backgroundText);
        days.setThumbColorPressed(R.color.backgroundDarkerText);

    }

    public void setStartDatePicker() {
        Calendar newCalendar = Calendar.getInstance();
        startdate.setFocusable(false);
        startdate.setClickable(true);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startdatelong = newDate.getTimeInMillis();
                startdate.setText(simpleDateFormat.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    public void setEndDatePicker() {
        Calendar newCalendar = Calendar.getInstance();
        enddate.setFocusable(false);
        enddate.setClickable(true);
        datePickerDialog2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                enddatelong = newDate.getTimeInMillis();
                enddate.setText(simpleDateFormat.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }


    public Boolean submitFilter() {
        if(startdatelong != null && enddatelong != null){
            if (startdatelong > enddatelong) {
                Toast.makeText(this, "Start Date must be less than enddate", Toast.LENGTH_LONG).show();
                return false;
            } else {
                events = database.filterEventsByDateRange(events, startdatelong, enddatelong);
            }
        }
        if (!selectedcategory.isEmpty()){
            events = database.filterEventsByCategories(events, selectedcategory);
        }
        System.out.println(daysno);

        if(daysno != null && daysno > 0 && daysrightno > 0 && daysrightno > daysno){
            events = database.filterEventsByDaysLeft(events, daysno, daysrightno);
        }

        if(subscribersno != null && subscribersno > 0 && subscribersrightno > 0 && subscribersrightno > subscribersno){
            events = database.filterEventsBySubscribers(events, subscribersno, subscribersrightno);
        }

        return true;

    }


    public void applyFilter() {
        Boolean moveon = this.submitFilter();
        if(moveon){
            Intent i = new Intent(this, HomeActivity.class);
            i.putExtra("tabnumber", selected);
            i.putExtra("viewtype", "filters");
            i.putExtra("sortby", sortvalue);
            final Bundle bundle = new Bundle();
            if (selected == 1) {
                bundle.putBinder("Events", new BinderWrapperForEventList(events));
            } else if (selected == 2) {
                bundle.putBinder("Schedules", new BinderWrapperForEventList(events));
            }
            i.putExtras(bundle);
            startActivity(i);
        }
    }


    public void cancelFilter() {
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra("tabnumber", selected);
        startActivity(i);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startdate:
                datePickerDialog.show();
                break;
            case R.id.enddate:
                datePickerDialog2.show();
                break;
            case R.id.applybutton:
                this.applyFilter();
                break;
            default:
                this.cancelFilter();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                sortvalue = "fname";
                break;
            case 1:
                sortvalue = "lname";
                break;
            case 2:
                sortvalue = "address";
                break;
            case 3:
                sortvalue = "state";
                break;
            case 4:
                sortvalue = "country";
                break;
            case 5:
                sortvalue = "date";
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        sortvalue = "fname";
    }

    @Override
    public void onCategoryClick(String value) {
        selectedcategory = value;
    }

    @Override
    public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {
        switch (rangeBar.getId()) {
            case R.id.daysrange:
                daysno = leftThumbIndex;
                dayslefttitle.setText(String.valueOf(daysno));
                daysrightno = rightThumbIndex;
                daysrighttitle.setText(String.valueOf(daysrightno));
                break;
            case R.id.subscribersrange:
                subscribersno = leftThumbIndex;
                subscribersleft.setText(String.valueOf(subscribersno));
                subscribersrightno = rightThumbIndex;
                subscribersright.setText(String.valueOf(subscribersrightno));
                break;

        }
    }
}
