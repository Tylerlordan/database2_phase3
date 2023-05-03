package com.example.database;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;

public class MaterialPage extends AppCompatActivity {
    Button home;
    Button previous;
    Button next;
    JSONArray materials;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_page);

        TextView indexView = findViewById(R.id.index_material);
        TextView nameView = findViewById(R.id.meetingName2);
        TextView materialView = findViewById(R.id.materialName);
        TextView author = findViewById(R.id.author);
        TextView mat_type = findViewById(R.id.type);
        TextView url = findViewById(R.id.url);
        TextView date = findViewById(R.id.date_material);
        TextView notes = findViewById(R.id.notes);

        Intent intent = getIntent();
        final String type = intent.getStringExtra("type");
        final int id = intent.getIntExtra("userId", -1);

        previous = findViewById(R.id.prev_material);
        next = findViewById(R.id.next_material);
        home = findViewById(R.id.home_material);
        i = 0;

        try {
            materials = new JSONArray(intent.getStringExtra("materials"));
            int material_count = materials.length();
            previous.setEnabled(false);
            if(i + 1 >= material_count) {
                next.setEnabled(false);
            }

            JSONObject currMaterial = materials.getJSONObject(i);

            indexView.setText("" + (i+1) + "/" + material_count);
            nameView.setText("Meeting: " + currMaterial.getString("meeting"));
            materialView.setText("Title: " + currMaterial.getString("title"));
            mat_type.setText("Type: " + currMaterial.getString("type"));
            author.setText("Author: " + currMaterial.getString("author"));
            date.setText("Date: " + currMaterial.getString("date"));
            url.setText(currMaterial.getString("url"));
            notes.setText(currMaterial.getString("notes"));
        } catch(JSONException e) {
            e.printStackTrace();
        }

        previous.setOnClickListener(v-> {
            try {
                int material_count = materials.length();
                i -= 1;
                if(i - 1 <= 0) {
                    previous.setEnabled(false);
                }
                if(!next.isEnabled()) {
                    next.setEnabled(true);
                }

                JSONObject currMaterial = materials.getJSONObject(i);

                indexView.setText("" + (i+1) + "/" + material_count);
                nameView.setText("Meeting: " + currMaterial.getString("meeting"));
                materialView.setText("Title: " + currMaterial.getString("title"));
                mat_type.setText("Type: " + currMaterial.getString("type"));
                author.setText("Author: " + currMaterial.getString("author"));
                date.setText("Date: " + currMaterial.getString("date"));
                url.setText(currMaterial.getString("url"));
                notes.setText(currMaterial.getString("notes"));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        });

        next.setOnClickListener(v-> {
            try {
                int material_count = materials.length();
                i += 1;
                if(i + 1 >= material_count) {
                    next.setEnabled(false);
                }
                if(!previous.isEnabled()) {
                    previous.setEnabled(true);
                }

                JSONObject currMaterial = materials.getJSONObject(i);
                indexView.setText("" + (i+1) + "/" + material_count);
                nameView.setText("Meeting: " + currMaterial.getString("meeting"));
                materialView.setText("Title: " + currMaterial.getString("title"));
                mat_type.setText("Type: " + currMaterial.getString("type"));
                author.setText("Author: " + currMaterial.getString("author"));
                date.setText("Date: " + currMaterial.getString("date"));
                url.setText(currMaterial.getString("url"));
                notes.setText(currMaterial.getString("notes"));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        });

        home.setOnClickListener(v-> {
            Intent intentBack = new Intent(MaterialPage.this, HomePage.class);
            intentBack.putExtra("type", type);
            intentBack.putExtra("userId", id);

            MaterialPage.this.startActivity(intentBack);
        });
    }

}
