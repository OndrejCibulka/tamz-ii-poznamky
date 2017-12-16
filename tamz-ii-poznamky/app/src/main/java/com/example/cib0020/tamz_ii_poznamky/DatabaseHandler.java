package com.example.cib0020.tamz_ii_poznamky;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cib0020 on 26.11.2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tamzNotes";
    private static final String TABLE_NOTES = "notes";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TEXT = "text";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_TEXT + " TEXT)";
        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_TEXT, note.getText());

        db.insert(TABLE_NOTES, null, values);
        db.close();
    }

    public Note getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTES, new String[]{KEY_ID, KEY_TITLE, KEY_TEXT}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        return note;
    }

    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<Note>();
        String selectQuery = "SELECT * FROM " + TABLE_NOTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setID(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setText(cursor.getString(2));

                noteList.add(note);
            } while (cursor.moveToNext());
        }

        return noteList;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_TEXT, note.getText());

        return db.update(TABLE_NOTES, values, KEY_ID + " = ?", new String[]{String.valueOf(note.getID())});
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?", new String[]{String.valueOf(note.getID())});

        db.close();
    }

    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?", new String[]{id + ""});

        db.close();
    }

    public void deleteAllNotes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " > ?", new String[]{"-1"});

        db.close();
    }

    public Note getNextNote(Note note) {
        if (note != null) {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_NOTES + " WHERE id > ? LIMIT 1";
            Cursor cursor = db.rawQuery(selectQuery, new String[]{note.getID() + ""});

            if (((cursor != null) && (cursor.getCount() > 0))) {
                cursor.moveToFirst();

                Log.d("CURSOR", cursor.getString(0));

                Note nextNote = new Note();
                nextNote.setID(Integer.parseInt(cursor.getString(0)));
                nextNote.setTitle(cursor.getString(1));
                nextNote.setText(cursor.getString(2));
                db.close();
                return nextNote;

            } else {
                db.close();
                return null;
            }
        } else {
            return null;
        }
    }

    public int updateOrCreate(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        int create = 1; // 1 = vytvoření nové poznámky, 0 = upravení poznámky

        if(note.getID() > 0){
            String selectQuery = "SELECT * FROM " + TABLE_NOTES + " WHERE id = ?";
            Cursor cursor = db.rawQuery(selectQuery, new String[]{note.getID() + ""});

            if (((cursor != null) && (cursor.getCount() > 0))) {
                updateNote(note);
                create = 0;
            } else {
                addNote(note);
            }

        } else {
            addNote(note);
        }

        db.close();
        return create;
    }
}
