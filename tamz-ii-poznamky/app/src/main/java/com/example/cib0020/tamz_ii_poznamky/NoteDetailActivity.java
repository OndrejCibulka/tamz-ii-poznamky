package com.example.cib0020.tamz_ii_poznamky;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.GestureOverlayView;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import org.w3c.dom.Text;

public class NoteDetailActivity extends AppCompatActivity implements  SensorEventListener {

    boolean editing = false;
    Note note = null;
    DatabaseHandler db = null;

    EditText editTextTitle = null;
    EditText editTextText = null;

    private GestureDetector mGesture;

    private Sensor mySensor;
    private SensorManager SM;

    private boolean accAllow = true;
    private int accValue = 15;

    long last = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        editTextTitle = (EditText) findViewById(R.id.editText_title);
        editTextText = (EditText) findViewById(R.id.editText_text);

        db = new DatabaseHandler(this);

        mGesture = new GestureDetector(this, mOnGesture);

        Intent intent = getIntent();
        if (intent.hasExtra("note")) {
            note = (Note) intent.getSerializableExtra("note");
            this.displayNote(note);
            editing = true;
        } else {
            note = new Note();
        }

        Button buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewTitle = (TextView) findViewById(R.id.editText_title);
                TextView textViewText = (TextView) findViewById(R.id.editText_text);

                if(textViewTitle.getText().length() > 0 && textViewText.getText().length() > 0)
                {
                    DatabaseHandler db = new DatabaseHandler(NoteDetailActivity.this);

                    if (editing) {
                        note.setTitle(textViewTitle.getText().toString());
                        note.setText(textViewText.getText().toString());
                        db.updateNote(note);
                    } else {
                        Note note = new Note(1, textViewTitle.getText().toString(), textViewText.getText().toString());
                        db.addNote(note);
                    }

                    Toast.makeText(NoteDetailActivity.this, "Poznámka byla uložena!", Toast.LENGTH_LONG).show();
                    setResult(0);
                    finish();
                } else {
                    Toast.makeText(NoteDetailActivity.this, "Prosím, vyplňte všechny údaje", Toast.LENGTH_LONG).show();
                }
            }
        });

        new Thread(new Runnable() {
            public void run() {
                SharedPreferences sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE);

                accAllow = sharedPref.getBoolean("acc-allow", false);
                accValue = sharedPref.getInt("acc-value", 15);
                Log.d("moje", accAllow + "");
            }
        }).start();


//      nastavení senzorů
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handled = super.dispatchTouchEvent(ev);
        handled = mGesture.onTouchEvent(ev);
        return handled;
    }

    private GestureDetector.OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (velocityX < -5000 ) { // potáhnutí doleva
                dispayNextNote();
            } else if (velocityX > 5000) { // potáhnutí doprava
                displayPreviousNote();
            }

            return true;
        }
    };

    private void displayNote(Note note) {
        TextView textViewTitle = (TextView) findViewById(R.id.editText_title);
        TextView textViewText = (TextView) findViewById(R.id.editText_text);
        textViewTitle.setText(note.getTitle());
        textViewText.setText(note.getText());
    }

    private void dispayNextNote(){ // ta, co je více vpravo
        String textTitle = editTextTitle.getText().toString();
        String textText = editTextText.getText().toString();

        if(textTitle.length() > 0 && textText.length() > 0){
            note.setTitle(editTextTitle.getText().toString());
            note.setText(editTextText.getText().toString());
            int created = db.updateOrCreate(note);

            if (created == 1){ // pokud se vytvořila nová ponzámka
                note = new Note();
                Toast.makeText(NoteDetailActivity.this, "Nová poznámka byla vytvořena!", Toast.LENGTH_LONG).show();
            } else { // pokud se poznámka upravila
                note = db.getNextNote(note);
            }

            if (note != null) {
                NoteDetailActivity.this.displayNote(note);
            } else {
                note = new Note();
                NoteDetailActivity.this.displayNote(note);
            }
        } else {
            Toast.makeText(NoteDetailActivity.this, "Prosím, vyplňte všechny údaje", Toast.LENGTH_LONG).show();
        }
    }

    private void displayPreviousNote(){ // ta, co je více vlevo
        String textTitle = editTextTitle.getText().toString();
        String textText = editTextText.getText().toString();

        if(textTitle.length() > 0 && textText.length() > 0) {
            Log.d("onMoveRight", "Moved to right");
            note.setTitle(editTextTitle.getText().toString());
            note.setText(editTextText.getText().toString());

            Note oldNote = note;

            if (db.getPreviousNote(note) != null) { // pokud tato poznámka NENÍ první v databázi
                note = db.getPreviousNote(note);
                Log.d("onMoveRight", "44, id: " + note.getID());
                NoteDetailActivity.this.displayNote(note);
            } else {
                Toast.makeText(NoteDetailActivity.this, "Jste na první poznámce", Toast.LENGTH_LONG).show();
            }

            int created = db.updateOrCreate(oldNote);
            if(created == 1){
                Toast.makeText(NoteDetailActivity.this, "Nová poznámka byla vytvořena!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(NoteDetailActivity.this, "Prosím, vyplňte všechny údaje", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(accAllow == true)
        {
            long now = System.currentTimeMillis();

            if(sensorEvent.values[0] < accValue*(-1)) // pohnutí doprava
            {
                if ((now - last) > 1000) {
                    displayPreviousNote();
                    last = now;
                }
            } else if (sensorEvent.values[0] > accValue) // pohnutí doleva
            {
                if ((now - last) > 1000) {
                    dispayNextNote();
                    last = now;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
