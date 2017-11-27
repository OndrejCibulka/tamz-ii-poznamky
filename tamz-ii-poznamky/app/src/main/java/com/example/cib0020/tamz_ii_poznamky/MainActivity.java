package com.example.cib0020.tamz_ii_poznamky;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv = (ListView) findViewById(R.id.listView_notes);
        DatabaseHandler db = new DatabaseHandler(this);

        List<Note> notes = db.getAllNotes();
        List<String> your_array_list = new ArrayList<String>();
        for(int i = 0; i<notes.size();i++){
            your_array_list.add(notes.get(i).getID()+"");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, your_array_list );
        lv.setAdapter(arrayAdapter);

//        Toast.makeText(this, note.getTitle(), Toast.LENGTH_LONG).show();

//        Intent intent = new Intent(this, NoteDetailActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);

        Button clickButton = (Button) findViewById(R.id.button_add_note);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                startActivity(intent);
            }
        });

    }
}
