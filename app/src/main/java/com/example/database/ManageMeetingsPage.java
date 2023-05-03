package com.example.database;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ManageMeetingsPage extends AppCompatActivity {

    Button leave;
    Button join;
    EditText meeting;
    Button home;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_meetings_page);

        Intent intent = getIntent();
        final String type = intent.getStringExtra("type");
        final int id = intent.getIntExtra("userId", -1);

        leave = findViewById(R.id.leaveMeeting);
        join = findViewById(R.id.joinMeeting);
        meeting = findViewById(R.id.meeting_id);
        home = findViewById(R.id.home_manage);

        join.setOnClickListener(v -> {
            String meeting_id = meeting.getText().toString();

            Response.Listener<String> responseListener = response -> {
                try {
                    Log.d("SubmitQueryHelp", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ManageMeetingsPage.this);
                        String message = jsonResponse.getString("message");
                        builder.setMessage(message).setPositiveButton("Continue", null).create().show();
                    } else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(ManageMeetingsPage.this);
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
            params.put("meeting_id", meeting_id);


            QueryRequest queryRequest = new QueryRequest(params,getString(R.string.url) + "join_meeting.php", responseListener);
            RequestQueue queue = Volley.newRequestQueue(ManageMeetingsPage.this);
            queue.add(queryRequest);
        });

        leave.setOnClickListener(v -> {
            String meeting_id = meeting.getText().toString();

            Response.Listener<String> responseListener = response -> {
                try {
                    Log.d("SubmitQueryHelp", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ManageMeetingsPage.this);
                        String message = jsonResponse.getString("message");
                        builder.setMessage(message).setPositiveButton("Continue", null).create().show();
                    } else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(ManageMeetingsPage.this);
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
            params.put("meeting_id", meeting_id);


            QueryRequest queryRequest = new QueryRequest(params,getString(R.string.url) + "leave_meeting.php", responseListener);
            RequestQueue queue = Volley.newRequestQueue(ManageMeetingsPage.this);
            queue.add(queryRequest);
        });

        home.setOnClickListener(v-> {
            Intent intentBack = new Intent(ManageMeetingsPage.this, HomePage.class);
            intentBack.putExtra("type", type);
            intentBack.putExtra("userId", id);

            ManageMeetingsPage.this.startActivity(intentBack);
        });

    }
}
