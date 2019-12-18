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
import com.example.vananaarbreda.Route.Coordinate;
import com.example.vananaarbreda.Route.JsonHandler;
import com.example.vananaarbreda.Route.RouteDB;
import com.example.vananaarbreda.Route.Sight;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class SightActivity extends AppCompatActivity {

    private Sight sight;
    private RouteDB database;
    private JsonHandler jsonHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight);


        database = new RouteDB(this);
        jsonHandler = new JsonHandler(this, database);
        ArrayList<Coordinate> coordList = database.readValues();

        TextView t = (TextView) findViewById(R.id.textView);

        StringBuilder sb = new StringBuilder();

        for(Coordinate c : coordList) {
            sb.append(c.toString());
        }

        t.setText(sb.toString());

//        Button button = (Button)findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TextView t = (TextView) findViewById(R.id.textView);
//                t.setText(sight.toString());
//            }
//        });
    }
}
