/**
 * Copyright (c) 2013 Acrylic Goat Software
 * 
 * This software is subject to the provisions of the GNU Lesser General
 * Public License Version 3 (LGPL).  See LICENSE.txt for details.
 */
package com.acrylicgoat.scrumnotes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Display;
import android.widget.EditText;
import android.widget.Toast;

import com.acrylicgoat.scrumnotes.provider.DatabaseHelper;
import com.acrylicgoat.scrumnotes.provider.Goals;
import com.acrylicgoat.scrumnotes.util.ScrumNotesUtil;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * @author ed woodward
 * 
 * Activity for daily meeting notes
 */
public class DailyNotesActivity extends SherlockActivity
{
    private Cursor cursor;
    private EditText note;
    ActionBar aBar;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailynotes);
        aBar = getSupportActionBar();
        aBar.setTitle(getString(R.string.dailynotes_title));
        aBar.setDisplayHomeAsUpEnabled(true);
        //goals = (EditText) findViewById(R.id.editGoals);
        note = (EditText) findViewById(R.id.editDaily);
        Display d = getWindowManager().getDefaultDisplay();
        if(isTabletDevice(d, this))
        {
            if(d.getWidth() < d.getHeight())
            {
                //in portrait so give more lines
                note.setLines(20);
            }
            else
            {
                note.setLines(11);
            }
                
        }
        getNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getSupportMenuInflater().inflate(R.menu.activity_dailynotes, menu);
        
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.clear();
        getSupportMenuInflater().inflate(R.menu.activity_dailynotes, menu);
        
        return true;
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        if(item.getItemId() == R.id.save)
        {
            saveNote();
            return true;
        }
        else if(item.getItemId() == android.R.id.home)
        {
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        }
        

        return true;
    }
    
    
    private void getNotes()
    {
        //Log.d("MainActivity", "getYesterday() called: " + owner);
        
        DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("select goals_note from goals", null);
        if(cursor.getCount()>0)
        {
            cursor.moveToNext();
            int notesColumn = cursor.getColumnIndex(Goals.NOTE);
            //Log.d("MainActivity.getYesterday()", "notesColumn: " + notesColumn);
            note.setText(cursor.getString(notesColumn));
            
        }
        else
        {
            note.setText("");
        }
        cursor.close();
        db.close();
    }
    
    private void saveNote()
    {
        ContentValues values = new ContentValues();
        
        String noteStr = note.getText().toString();
        //Log.d("NoteEditorActivity", "note: " + text);
        int nlength = noteStr.length();
        
        if (nlength == 0) 
        {
            Toast.makeText(this, getString(R.string.nothing_to_save), Toast.LENGTH_SHORT).show();
            return;
        }
        
        values.put(Goals.NOTE, noteStr);
        
        //check if a note already exists for today
        DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("select goals_goal from goals", null);
        if(cursor.getCount()>0)
        { 
            //Log.d("MainActivity", "saveNote(): doing update ");
            StringBuilder sb = new StringBuilder();
            sb.append("update goals set goals_note = '");
            sb.append(ScrumNotesUtil.escape(noteStr));
            sb.append("'");
            dbHelper.getReadableDatabase().execSQL(sb.toString());
            getContentResolver().update(Goals.CONTENT_URI, values, null,null);
        }
        else
        {
            getContentResolver().insert(Goals.CONTENT_URI, values);
        }
        cursor.close();
        db.close();
        
    }
    
    private boolean isTabletDevice(Display d, Activity context) 
    {
        if (android.os.Build.VERSION.SDK_INT >= 11) 
        { // honeycomb
            
            return true;

        }
        return false;
    }
    
//    private String escape(String text)
//    {
//        String returnVal = "";
//        
//        returnVal = text.replace("'", "''");
//        
//        return returnVal;
//    }
    


}
