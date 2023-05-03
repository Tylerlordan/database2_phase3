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
import com.example.database.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginQuery extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    Button submitQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_query);

        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        submitQuery = (Button) findViewById(R.id.submitQuery);

        submitQuery.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();


            Response.Listener<String> responseListener = response -> {
                try {
                    Log.d("SubmitQueryHelp", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        String type = jsonResponse.getString("type");
                        int userId = jsonResponse.getInt("id");

                        Intent intent = new Intent(LoginQuery.this, HomePage.class);

                        intent.putExtra("userId", userId);
                        intent.putExtra("type", type);

                        LoginQuery.this.startActivity(intent);
                    } else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginQuery.this);
                        String message = jsonResponse.getString("message");
                        builder.setMessage(message).setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email", email);
            params.put("password", password);

            QueryRequest queryRequest = new QueryRequest(params,getString(R.string.url) + "login.php", responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginQuery.this);
            queue.add(queryRequest);
        });
    }
}
