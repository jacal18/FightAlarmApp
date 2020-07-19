package com.fightalarm.fightalarm.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.models.Message;
import com.fightalarm.fightalarm.providers.Database;
import com.fightalarm.fightalarm.providers.Extras;

import java.util.Calendar;
import java.util.Date;

public class AboutFragment extends Fragment implements View.OnClickListener {

    String url;
    Extras extras = new Extras();

    View view;
    Button instagram;
    Button twitter;
    Button submitbutton;

    EditText email;
    EditText name;
    EditText content;
    Database database;


    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_about, null, true);

        twitter = (Button) view.findViewById(R.id.twitter);
        instagram = (Button) view.findViewById(R.id.instagram);
        submitbutton = (Button) view.findViewById(R.id.submitbutton);
        content = (EditText) view.findViewById(R.id.message);
        name = (EditText) view.findViewById(R.id.name);
        email = (EditText) view.findViewById(R.id.email);

        database = new Database(this.getContext());

        twitter.setOnClickListener(this);
        instagram.setOnClickListener(this);
        submitbutton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.instagram:
                Uri uri = Uri.parse("http://instagram.com/_u/fightalarm");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");
                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    url = "https://www.instagram.com/fightalarm/";
                    extras.openURL(this.getContext(), url);
                }
                break;
            case R.id.twitter:
                url = "https://twitter.com/FightAlarm";
                extras.openURL(this.getContext(), url);
                break;
            case R.id.submitbutton:
                this.submitForm();
                break;
        }
    }

    public void submitForm() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String idvalue = Long.toString(date.getTime());
        String emailvalue = email.getText().toString().trim();
        String contentvalue = content.getText().toString().trim();
        String namevalue = name.getText().toString().trim();

        if (!contentvalue.isEmpty() && !namevalue.isEmpty()) {
            if (extras.isValidEmail(emailvalue)) {
                Message message = new Message(idvalue, namevalue, emailvalue, contentvalue, date.getTime());
                database.saveMessage(message);
                Toast.makeText(this.getContext(), "Successfully submitted your message", Toast.LENGTH_LONG).show();
                email.setText("");
                content.setText("");
                name.setText("");
            } else {
                Toast.makeText(this.getContext(), "Email is not in the correct format, please check and try again.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this.getContext(), "One or more fields are empty.", Toast.LENGTH_LONG).show();
        }

    }
}
