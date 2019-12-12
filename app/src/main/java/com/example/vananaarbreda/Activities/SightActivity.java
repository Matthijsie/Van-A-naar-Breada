package com.example.vananaarbreda.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.example.vananaarbreda.R;
import com.example.vananaarbreda.Route.RouteDB;
import com.example.vananaarbreda.Route.Sight;

import java.io.IOException;

public class SightActivity extends AppCompatActivity {

    private Sight sight;
    private RouteDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight);

        try {
            database = new RouteDB(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
