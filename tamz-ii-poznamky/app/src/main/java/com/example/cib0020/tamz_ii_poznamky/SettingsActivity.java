package com.example.cib0020.tamz_ii_poznamky;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button saveButton = (Button) findViewById(R.id.button_save);
        final Switch switchAllowAccelerometer = (Switch) findViewById(R.id.switch_allow_accelerometer);
        final SeekBar seekBarAccelerometerValue = (SeekBar) findViewById(R.id.seekBar_accelerometer_value);
        final TextView textViewAccelerometerValue = (TextView) findViewById(R.id.textView_accelerometer_value);

        switchAllowAccelerometer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    seekBarAccelerometerValue.setEnabled(true);
                } else {
                    seekBarAccelerometerValue.setEnabled(false);
                }
            }
        });

        seekBarAccelerometerValue.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                        textViewAccelerometerValue.setText(progresValue + " g");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });

        SharedPreferences sharedPref = this.getSharedPreferences("settings", Context.MODE_PRIVATE);

        switchAllowAccelerometer.setChecked(sharedPref.getBoolean("acc-allow", true));
        seekBarAccelerometerValue.setProgress(sharedPref.getInt("acc-value", 15));
        textViewAccelerometerValue.setText(sharedPref.getInt("acc-value", 15) + " g");
        seekBarAccelerometerValue.setEnabled(sharedPref.getBoolean("acc-allow", true));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref =  SettingsActivity.this.getSharedPreferences("settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putBoolean("acc-allow", switchAllowAccelerometer.isChecked());
                editor.putInt("acc-value", seekBarAccelerometerValue.getProgress());

                editor.commit();

                finish();
            }
        });
    }
}
