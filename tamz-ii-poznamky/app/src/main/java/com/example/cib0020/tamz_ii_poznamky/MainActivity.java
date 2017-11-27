package com.example.cib0020.tamz_ii_poznamky;


import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final int NOTE_DETAIL_REQUEST = 0;

    List<Note> listNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHandler db = new DatabaseHandler(this);
        this.refreshList();

//      Toast.makeText(this, note.getTitle(), Toast.LENGTH_LONG).show();

        Button clickButton = (Button) findViewById(R.id.button_add_note);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                startActivityForResult(intent, NOTE_DETAIL_REQUEST);

            }
        });

        ListView listViewNotes = (ListView) findViewById(R.id.listView_notes);
        listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                intent.putExtra("note", (Serializable) listNotes.get(position));
                startActivityForResult(intent, NOTE_DETAIL_REQUEST);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == MainActivity.NOTE_DETAIL_REQUEST){
            Toast.makeText(this, "Poznámka byla uložena!", Toast.LENGTH_LONG).show();
            MainActivity.this.refreshList();
        }
    }

    private void refreshList(){
        ListView lv = (ListView) findViewById(R.id.listView_notes);
        DatabaseHandler db = new DatabaseHandler(this);

        listNotes = db.getAllNotes();
        List<String> your_array_list = new ArrayList<String>();
        for(int i = 0; i < listNotes.size();i++){
            your_array_list.add(listNotes.get(i).getTitle()+"");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, your_array_list );
        lv.setAdapter(arrayAdapter);
    }
}
