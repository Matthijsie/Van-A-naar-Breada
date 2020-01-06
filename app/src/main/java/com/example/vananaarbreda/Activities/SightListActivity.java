package com.example.vananaarbreda.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.vananaarbreda.R;
import com.example.vananaarbreda.Route.RouteDB;

public class SightListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SightListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight_list);

        recyclerView = findViewById(R.id.recyclerViewSights);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SightListAdapter(RouteDB.getInstance(this).readValues());
        recyclerView.setAdapter(adapter);
    }
}
