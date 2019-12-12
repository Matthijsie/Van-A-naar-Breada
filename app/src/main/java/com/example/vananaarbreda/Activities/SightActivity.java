package com.example.vananaarbreda.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.vananaarbreda.R;
import com.example.vananaarbreda.Route.Sight;

public class SightActivity extends AppCompatActivity {

    private Sight sight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight);

        sight = (Sight)getIntent().getSerializableExtra("SIGHT");

        TextView textView = findViewById(R.id.textViewSight);
        textView.setText(sight.getName());
    }
}
