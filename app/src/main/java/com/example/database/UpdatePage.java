package com.example.database;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UpdatePage extends AppCompatActivity {

    Button home;
    Button update;
    EditText new_name;
    EditText new_email;
    EditText new_phone;
    EditText new_password;
    EditText child;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_page);

        Intent intent = getIntent();
        final String type = intent.getStringExtra("type");
        final int id = intent.getIntExtra("userId", -1);

        if(type.equals("student")) {
            findViewById(R.id.child_email).setVisibility(View.INVISIBLE);
            findViewById(R.id.email_note).setVisibility(View.INVISIBLE);
        }

        new_name = findViewById(R.id.new_name);
        new_email = findViewById(R.id.new_email);
        new_phone = findViewById(R.id.new_phone);
        new_password = findViewById(R.id.new_password);
        child = findViewById(R.id.child_email);

        update = findViewById(R.id.update);

        update.setOnClickListener(v -> {
            String name = new_name.getText().toString();
            String email = new_email.getText().toString();
            String phone = new_phone.getText().toString();
            String password = new_password.getText().toString();
            String student = child.getText().toString();

            Response.Listener<String> responseListener = response -> {
                try {
                    Log.d("SubmitQueryHelp", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePage.this);
                        String message = jsonResponse.getString("message");
                        builder.setMessage(message).setPositiveButton("Continue", null).create().show();
                    } else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePage.this);
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
            params.put("new_email", email);
            params.put("new_password", password);
            params.put("new_name", name);
            params.put("new_phone", phone);
            params.put("student", student);


            QueryRequest queryRequest = new QueryRequest(params,getString(R.string.url) + "update_account.php", responseListener);
            RequestQueue queue = Volley.newRequestQueue(UpdatePage.this);
            queue.add(queryRequest);
        });

        home = findViewById(R.id.home_update);

        home.setOnClickListener(v-> {
            Intent intentBack = new Intent(UpdatePage.this, HomePage.class);
            intentBack.putExtra("type", type);
            intentBack.putExtra("userId", id);

            UpdatePage.this.startActivity(intentBack);
        });

    }
}
