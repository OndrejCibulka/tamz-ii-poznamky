package com.example.cib0020.tamz_ii_poznamky;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NoteDetailActivity extends AppCompatActivity {

    boolean editing = false;
    Note note = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        Intent intent = getIntent();
        if (intent.hasExtra("note")) {
            note = (Note) intent.getSerializableExtra("note");

            TextView textViewTitle = (TextView) findViewById(R.id.editText_title);
            TextView textViewText = (TextView) findViewById(R.id.editText_text);
            textViewTitle.setText(note.getTitle());
            textViewText.setText(note.getText());

            editing = true;
        }


        Button buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewTitle = (TextView) findViewById(R.id.editText_title);
                TextView textViewText = (TextView) findViewById(R.id.editText_text);

                DatabaseHandler db = new DatabaseHandler(NoteDetailActivity.this);

                if (editing) {
                    note.setTitle(textViewTitle.getText().toString());
                    note.setText(textViewText.getText().toString());
                    db.updateNote(note);
                } else {
                    Note note = new Note(1, textViewTitle.getText().toString(), textViewText.getText().toString());
                    db.addNote(note);
                }

                setResult(0);
                finish();
            }
        });
    }
}
