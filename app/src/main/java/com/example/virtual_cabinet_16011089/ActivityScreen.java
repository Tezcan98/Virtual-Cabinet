package com.example.virtual_cabinet_16011089;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ActivityScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterActivity aAdapter;

    private List<Activity> aList;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        db = new DatabaseHelper(this);
        aList = db.returnActivities();

        recyclerView = findViewById(R.id.recyclerView_activity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        aAdapter = new AdapterActivity(this, aList, db);
        recyclerView.setAdapter(aAdapter);


    }



}