package com.example.vananaarbreda.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.vananaarbreda.R;
import com.example.vananaarbreda.Route.RouteDB;
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

//        ImageView imageViewSight = findViewById(R.id.sightImage);
//        imageViewSight.setImageBitmap(sight.getImages());

        TextView textDescription = findViewById(R.id.sightDescription);
        textDescription.setText(sight.getDescription());

        Switch visited = findViewById(R.id.isSeen);
        visited.setChecked(sight.isVisited());
        visited.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sight.setVisited(b);
                RouteDB.getInstance(getApplicationContext()).updateSight(sight);
            }
        });
    }
}
