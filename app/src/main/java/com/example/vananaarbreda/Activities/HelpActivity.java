package com.example.vananaarbreda.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.vananaarbreda.R;

public class HelpActivity extends AppCompatActivity {
    private static final String TAG = HelpActivity.class.getSimpleName();
    private int themevalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.stored_theme_key),Context.MODE_PRIVATE);
        themevalue = sharedPref.getInt(getString(R.string.stored_theme_key), getResources().getInteger(R.integer.MainTHeme));
        if (themevalue == getResources().getInteger(R.integer.MainTHeme)) {
            setTheme(R.style.AppTheme);
        } else if (themevalue == getResources().getInteger(R.integer.ColourBlindTheme)) {
            setTheme(R.style.ColourBlindTheme);
        }else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_help);

        //layour
        Button colourBlindButton = findViewById(R.id.buttonColourBLindMode);
        colourBlindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "colourBlindButton Pressed");
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.stored_theme_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                if (themevalue == getResources().getInteger(R.integer.MainTHeme)) {
                    setTheme(R.style.ColourBlindTheme);
                    editor.putInt(getString(R.string.stored_theme_key), getResources().getInteger(R.integer.ColourBlindTheme));
                    editor.apply();
                } else {
                    setTheme(R.style.AppTheme);
                    editor.putInt(getString(R.string.stored_theme_key), getResources().getInteger(R.integer.MainTHeme));
                    editor.apply();
                }

                Toast.makeText(v.getContext(), "Changes applied, restart application to see changes", Toast.LENGTH_LONG).show();
            }
        });

        ImageButton imageButtonNL = findViewById(R.id.imageButtonNL);
        imageButtonNL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ImageButton imageButtonUk = findViewById(R.id.imageButtonUK);
        imageButtonUk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }
}
