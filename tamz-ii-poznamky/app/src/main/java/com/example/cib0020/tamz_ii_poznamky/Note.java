package com.example.cib0020.tamz_ii_poznamky;

/**
 * Created by cib0020 on 26.11.2017.
 */

public class Note {
    private int _id;
    private String _title;
    private String _text;

    public Note(){}

    public Note(int id, String title, String text){
        this._id = id;
        this._title = title;
        this._text = text;
    }

    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getTitle(){
        return this._title;
    }

    public void setTitle(String title) { this._title = title; };

    public String getText(){
        return this._text;
    }

    public void setText(String text){
        this._text = text;
    }
}
