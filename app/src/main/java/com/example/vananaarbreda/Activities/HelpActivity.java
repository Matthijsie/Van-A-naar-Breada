package com.example.vananaarbreda.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.vananaarbreda.R;

public class HelpActivity extends AppCompatActivity {

    private Switch colourBlindSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        colourBlindSwitch = findViewById(R.id.switchColourBlindMode);
        colourBlindSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    getApplication().setTheme(R.style.ColourBlindTheme);
                }else {
                    getApplication().setTheme(R.style.AppTheme);
                }
            }
        });
    }
}
