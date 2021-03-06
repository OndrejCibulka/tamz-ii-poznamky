package com.example.cib0020.tamz_ii_poznamky;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    DatabaseHandler db = null;

    List<Note> listNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        this.refreshList();

        FloatingActionButton buttonAdd = (FloatingActionButton) findViewById(R.id.floatingActionButton_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                startActivityForResult(intent, 0);
//                startActivity(intent);
            }
        });

        ListView listViewNotes = (ListView) findViewById(R.id.listView_notes);
        listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                intent.putExtra("note", (Serializable) listNotes.get(position));
                startActivityForResult(intent, 0);
//                startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            MainActivity.this.refreshList();
        }
    }

    private void refreshList() {
        ListView lv = (ListView) findViewById(R.id.listView_notes);
        DatabaseHandler db = new DatabaseHandler(this);

        listNotes = db.getAllNotes();
        List<String> your_array_list = new ArrayList<String>();
        for (int i = 0; i < listNotes.size(); i++) {
            your_array_list.add(listNotes.get(i).getTitle() + "");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, your_array_list);
        lv.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete_all:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("Opravdu?");
                builder.setMessage("Opravdu chece smazat všechny poznámky?");
                builder.setPositiveButton("Ano, smazat",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteAllNotes();
                                MainActivity.this.refreshList();
                                Toast.makeText(MainActivity.this, "Poznámky byly smazány!", Toast.LENGTH_LONG).show();

                            }
                        });
                builder.setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            case R.id.show_graph:
                Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                startActivity(intent);
                return true;

            case R.id.show_settings:
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

