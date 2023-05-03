package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Vector;

public class MemberPage extends AppCompatActivity {

    Button home;
    JSONArray members;
    int i;
    Vector<TextView> nameViews;
    Vector<TextView> emailViews;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_page);

        Vector<TextView> nameViews = new Vector<TextView>(6);
        Vector<TextView> emailViews = new Vector<TextView>(6);

        // Maybe there's a cleaner way to do this, idk
        // Get the name views
        nameViews.add(findViewById(R.id.student_name));
        nameViews.add(findViewById(R.id.student_name2));
        nameViews.add(findViewById(R.id.student_name3));
        nameViews.add(findViewById(R.id.student_name4));
        nameViews.add(findViewById(R.id.student_name5));
        nameViews.add(findViewById(R.id.student_name6));

        // Get the email views
        emailViews.add(findViewById(R.id.student_email));
        emailViews.add(findViewById(R.id.student_email2));
        emailViews.add(findViewById(R.id.student_email3));
        emailViews.add(findViewById(R.id.student_email4));
        emailViews.add(findViewById(R.id.student_email5));
        emailViews.add(findViewById(R.id.student_email6));

        Intent intent = getIntent();
        final String type = intent.getStringExtra("type");
        final int id = intent.getIntExtra("userId", -1);

        home = findViewById(R.id.home_member);

        try {
            members = new JSONArray(intent.getStringExtra("members"));
            int member_count = members.length();

            for(; i < member_count; i++) {
                nameViews.get(i).setText(members.getJSONObject(i).getString("name"));
                emailViews.get(i).setText(members.getJSONObject(i).getString("email"));
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        home.setOnClickListener(v-> {
            Intent intentBack = new Intent(MemberPage.this, HomePage.class);
            intentBack.putExtra("type", type);
            intentBack.putExtra("userId", id);

            MemberPage.this.startActivity(intentBack);
        });
    }
}
