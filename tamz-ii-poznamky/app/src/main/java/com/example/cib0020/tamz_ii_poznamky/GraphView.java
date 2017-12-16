package com.example.cib0020.tamz_ii_poznamky;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.List;

/**
 * Created by cib0020 on 16.12.2017.
 */

public class GraphView extends View {
    public GraphView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        DatabaseHandler db = new DatabaseHandler(this.getContext());
        List<Note> notes = db.getAllNotes();

        int maxLength = 0;
        long countLetters = 0;
        for (Integer i = 0; i < notes.size(); i++) {
            if(notes.get(i).getText().length() > maxLength){
                maxLength = notes.get(i).getText().length();
                countLetters += notes.get(i).getText().length();
            }
        }

        float average = (countLetters / notes.size());

        Paint paint = new Paint();
        // paint.setStyle(Paint.Style.STROKE);
        Integer height = canvas.getHeight();
        Integer width = canvas.getWidth();

        /* nastavení*/

            Integer graphWidth = 50;
            Integer graphHeight = 80;
            Float secondGraphHeight = (average / maxLength)*100;

        /* konec */

        Integer borderWidth = (100-graphWidth)/2;
        Integer borderHeight = (100-graphHeight)/2;

        Float left = ((float)width/100)*borderWidth;
        Float top = ((float)height/100)*borderHeight;
        Float right = ((float)width/100)*(100-borderWidth);
        Float bottom = ((float)height/100)*(100-borderHeight);

        paint.setColor(Color.rgb(0,112,149));
        canvas.drawRect(left, top, right, bottom, paint);

        paint.setColor(Color.rgb(97,186,228));
        Float secondTop = top+(((bottom-top)/100)*(100-secondGraphHeight));
        canvas.drawRect(left, secondTop, right, bottom, paint);

        paint.setTextSize(((float)height/100)*5);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(maxLength + "", ((float)width/100)*50,((float)height/100)*8, paint);
        canvas.drawText("0", ((float)width/100)*50, bottom+(((float)height/100)*5), paint);
        canvas.drawText( average + "", ((float)width/100)*50, secondTop-(((float)height/100) * 1), paint);
    }
}
