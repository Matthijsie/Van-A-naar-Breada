package com.example.vananaarbreda.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        LinearLayout ll = findViewById(R.id.linearLayout);
        for (int i = 0; i < sight.getStringImageNames().size(); i++) {
            if(!sight.getStringImageNames().get(i).equals("image_0")) {
                ImageView image = new ImageView(this);
                image.setId(i);
                image.setPadding(50, 2, 50, 2);

                int resID = getResources().getIdentifier(sight.getStringImageNames().get(i), "drawable", getPackageName());
                image.setImageResource(resID);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image.setAdjustViewBounds(true);
                ll.addView(image);
            }
        }


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
