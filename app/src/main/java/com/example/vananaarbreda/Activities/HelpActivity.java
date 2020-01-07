package com.example.vananaarbreda.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.vananaarbreda.R;

public class HelpActivity extends AppCompatActivity {
    private static final String TAG = HelpActivity.class.getSimpleName();
    private Button colourBlindButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        colourBlindButton = findViewById(R.id.buttonColourBLindMode);
        colourBlindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "colourBlindButton Pressed");
                getApplication().setTheme(R.style.ColourBlindTheme);
                recreate();
            }
        });
    }
}
