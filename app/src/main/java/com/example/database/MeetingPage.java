package com.example.database;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MeetingPage extends AppCompatActivity {

    Button meeting_materials;
    Button meeting_members;
    Button home;
    Button previous;
    Button next;
    int meeting_id;
    JSONArray meetings;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_page);

        TextView indexView = findViewById(R.id.index_meeting);
        TextView nameView = findViewById(R.id.meetingName);
        TextView groupView = findViewById(R.id.groupName);
        TextView time = findViewById(R.id.time);
        TextView date = findViewById(R.id.date_meeting);
        TextView announcement = findViewById(R.id.announcement);

        Intent intent = getIntent();
        final String type = intent.getStringExtra("type");
        final int id = intent.getIntExtra("userId", -1);

        previous = findViewById(R.id.prev_meeting);
        next = findViewById(R.id.next_meeting);
        home = findViewById(R.id.home_meeting);
        meeting_materials = findViewById(R.id.meeting_materials);
        meeting_members = findViewById(R.id.meeting_members);
        i = 0;

        try {
            meetings = new JSONArray(intent.getStringExtra("meetings"));
            int meeting_count = meetings.length();
            previous.setEnabled(false);
            if(i + 1 >= meeting_count) {
                next.setEnabled(false);
            }

            JSONObject currMeeting = meetings.getJSONObject(i);
            meeting_id = currMeeting.getInt("meeting_id");
            indexView.setText("" + (i+1) + "/" + meeting_count);
            nameView.setText("Name: " + currMeeting.getString("meeting_name"));
            groupView.setText("Group: " + currMeeting.getString("group"));
            time.setText("Time: " + currMeeting.getString("start_time") + " to " + currMeeting.getString("end_time"));
            date.setText("Date: " + currMeeting.getString("date"));
            announcement.setText(currMeeting.getString("announcement"));
        } catch(JSONException e) {
            e.printStackTrace();
        }

        previous.setOnClickListener(v-> {
            try {
                int meeting_count = meetings.length();
                i -= 1;
                if(i - 1 <= 0) {
                    previous.setEnabled(false);
                }
                if(!next.isEnabled()) {
                    next.setEnabled(true);
                }

                JSONObject currMeeting = meetings.getJSONObject(i);
                meeting_id = currMeeting.getInt("meeting_id");
                indexView.setText("" + (i+1) + "/" + meeting_count);
                nameView.setText("Name: " + currMeeting.getString("meeting_name"));
                groupView.setText("Group: " + currMeeting.getString("group"));
                time.setText("Time: " + currMeeting.getString("start_time") + " to " + currMeeting.getString("end_time"));
                date.setText("Date: " + currMeeting.getString("date"));
                announcement.setText(currMeeting.getString("announcement"));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        });

        next.setOnClickListener(v-> {
            try {
                int meeting_count = meetings.length();
                i += 1;
                if(i + 1 >= meeting_count) {
                    next.setEnabled(false);
                }
                if(!previous.isEnabled()) {
                    previous.setEnabled(true);
                }

                JSONObject currMeeting = meetings.getJSONObject(i);
                meeting_id = currMeeting.getInt("meeting_id");
                indexView.setText("" + (i+1) + "/" + meeting_count);
                nameView.setText("Name: " + currMeeting.getString("meeting_name"));
                groupView.setText("Group: " + currMeeting.getString("group"));
                time.setText("Time: " + currMeeting.getString("start_time") + " to " + currMeeting.getString("end_time"));
                date.setText("Date: " + currMeeting.getString("date"));
                announcement.setText(currMeeting.getString("announcement"));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        });

        home.setOnClickListener(v-> {
            Intent intentBack = new Intent(MeetingPage.this, HomePage.class);
            intentBack.putExtra("type", type);
            intentBack.putExtra("userId", id);

            MeetingPage.this.startActivity(intentBack);
        });

        meeting_materials.setOnClickListener(v-> {
            Response.Listener<String> responseListener = response -> {
                try {
                    Log.d("SubmitQueryHelp", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        JSONArray materials = jsonResponse.getJSONArray("materials");

                        Intent intentMaterials = new Intent(MeetingPage.this, MaterialPage.class);

                        intentMaterials.putExtra("materials", materials.toString());
                        intentMaterials.putExtra("userId", id);
                        intentMaterials.putExtra("type", type);

                        MeetingPage.this.startActivity(intentMaterials);
                    } else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MeetingPage.this);
                        String message = jsonResponse.getString("message");
                        builder.setMessage(message).setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("user_id", String.valueOf(id));
            params.put("user_type", type);
            params.put("meeting_id", String.valueOf(meeting_id));

            QueryRequest queryRequest = new QueryRequest(params,getString(R.string.url) + "view_materials.php", responseListener);
            RequestQueue queue = Volley.newRequestQueue(MeetingPage.this);
            queue.add(queryRequest);
        });

        meeting_members.setOnClickListener(v-> {
            Response.Listener<String> responseListener = response -> {
                try {
                    Log.d("SubmitQueryHelp", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        JSONArray members = jsonResponse.getJSONArray("members");

                        Intent intentMembers = new Intent(MeetingPage.this, MemberPage.class);

                        intentMembers.putExtra("members", members.toString());
                        intentMembers.putExtra("userId", id);
                        intentMembers.putExtra("type", type);

                        MeetingPage.this.startActivity(intentMembers);
                    } else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MeetingPage.this);
                        String message = jsonResponse.getString("message");
                        builder.setMessage(message).setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("user_id", String.valueOf(id));
            params.put("user_type", type);
            params.put("meeting_id", String.valueOf(meeting_id));

            QueryRequest queryRequest = new QueryRequest(params,getString(R.string.url) + "view_meeting_members.php", responseListener);
            RequestQueue queue = Volley.newRequestQueue(MeetingPage.this);
            queue.add(queryRequest);
        });
    }

}
