package com.example.vananaarbreda.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vananaarbreda.R;
import com.example.vananaarbreda.Route.RouteDB;
import com.example.vananaarbreda.Route.Sight;

import java.io.IOException;

public class SightActivity extends AppCompatActivity {

    private Sight sight;
    private RouteDB database;
    private RouteDB.DatabaseHelper dbt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight);

        try {
            database = new RouteDB(this);
            dbt = database.getDatabaseHelper();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t = (TextView) findViewById(R.id.textView);
                Sight sight = dbt.getSight(1);
                t.setText(sight.toString());
            }
        });
    }
}
