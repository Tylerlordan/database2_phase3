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

public class HomePage extends AppCompatActivity {
    Button logout;
    Button viewEnrolled;
    Button updateAccount;
    Button manageMeetings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        TextView idtextview = (TextView) findViewById(R.id.userId);


        Intent intent = getIntent();
        final String type = intent.getStringExtra("type");
        final int id = intent.getIntExtra("userId", -1);

        idtextview.setText("User ID: "+id);

        logout = (Button) findViewById(R.id.logout);

        logout.setOnClickListener(v -> {
            Intent intentBack = new Intent(HomePage.this, LoginQuery.class);

            HomePage.this.startActivity(intentBack);
        });

        viewEnrolled = (Button) findViewById(R.id.viewEnrolled);

        if(type.equals("parent")) {
            viewEnrolled.setEnabled(false);
        }

        viewEnrolled.setOnClickListener(v -> {
            Response.Listener<String> responseListener = response -> {
                try {
                    Log.d("SubmitQueryHelp", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        JSONArray meetings = jsonResponse.getJSONArray("meetings");

                        Intent intentMeetings = new Intent(HomePage.this, MeetingPage.class);

                        intentMeetings.putExtra("meetings", meetings.toString());
                        intentMeetings.putExtra("userId", id);
                        intentMeetings.putExtra("type", type);

                        HomePage.this.startActivity(intentMeetings);
                    } else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
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

            QueryRequest queryRequest = new QueryRequest(params,getString(R.string.url) + "view_meetings.php", responseListener);
            RequestQueue queue = Volley.newRequestQueue(HomePage.this);
            queue.add(queryRequest);
        });

        updateAccount = findViewById(R.id.updateAccount);

        updateAccount.setOnClickListener(v -> {
            Intent intentUpdate = new Intent(HomePage.this, UpdatePage.class);

            intentUpdate.putExtra("userId", id);
            intentUpdate.putExtra("type", type);

            HomePage.this.startActivity(intentUpdate);
        });

        manageMeetings = findViewById(R.id.manageMeetings);
        if(type.equals("parent")) {
            manageMeetings.setEnabled(false);
        }

        manageMeetings.setOnClickListener(v -> {
            Intent intentUpdate = new Intent(HomePage.this, ManageMeetingsPage.class);

            intentUpdate.putExtra("userId", id);
            intentUpdate.putExtra("type", type);

            HomePage.this.startActivity(intentUpdate);
        });

    }
}
