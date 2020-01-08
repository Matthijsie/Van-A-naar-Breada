package com.example.vananaarbreda.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.vananaarbreda.R;
import com.example.vananaarbreda.Route.RouteDB;
import com.example.vananaarbreda.Route.Sight;

import java.util.Locale;

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
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.stores_language_key), Context.MODE_PRIVATE);

        int languageValue = sharedPref.getInt(getString(R.string.stores_language_key), 0);
        if (languageValue == 1) {
            textDescription.setText(sight.getDescription());
        }else {
            textDescription.setText(sight.getDescriptionEN());
        }

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
