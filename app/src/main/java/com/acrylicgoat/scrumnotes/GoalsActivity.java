/**
 * Copyright (c) 2013 Acrylic Goat Software
 * 
 * This software is subject to the provisions of the GNU Lesser General
 * Public License Version 3 (LGPL).  See LICENSE.txt for details.
 */
package com.acrylicgoat.scrumnotes;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.acrylicgoat.scrumnotes.provider.DatabaseHelper;
import com.acrylicgoat.scrumnotes.provider.Goals;
import com.acrylicgoat.scrumnotes.util.ScrumNotesUtil;

/**
 * @author ed woodward
 * 
 * Activity for capturing team goals
 *
 */
public class GoalsActivity extends Activity
{
    private Cursor cursor;
    private EditText goals;
    ActionBar aBar;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        aBar = getActionBar();
        aBar.setTitle(getString(R.string.goal_title));
        aBar.setDisplayHomeAsUpEnabled(true);
        goals = (EditText) findViewById(R.id.editGoals);
        Display d = getWindowManager().getDefaultDisplay();
        if(isTabletDevice(d, this))
        {
            if(d.getWidth() < d.getHeight())
            {
                //in portrait so give more lines
                goals.setLines(20);
            }
            else
            {
                goals.setLines(11);
            }
                
        }
        getGoals();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_goals, menu);
        
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.clear();
        getMenuInflater().inflate(R.menu.activity_goals, menu);
        
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
        
        if(item.getItemId() == R.id.share)
        {
            String goalStr = goals.getText().toString();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType(getString(R.string.mimetype_text));

            if(goalStr != null && (!goalStr.equals("")))
            {
                String today = ScrumNotesUtil.getTodaysDate();
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.this_week) + " " + today);
                intent.putExtra(Intent.EXTRA_TEXT, goalStr + getString(R.string.two_new_lines) + getString(R.string.shared_via));

                Intent chooser = Intent.createChooser(intent, getString(R.string.tell_friend) + getString(R.string.team_goals));
                startActivity(chooser);
            }
            else
            {
                Toast.makeText(GoalsActivity.this, getString(R.string.no_data_msg),  Toast.LENGTH_LONG).show();
            }
            
        }
        else if(item.getItemId() == android.R.id.home)
        {
            Intent mainIntent = new Intent(getApplicationContext(), DailyNotesActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        }
        

        return true;
    }
    
    private void getGoals()
    {
        //Log.d("MainActivity", "getToday() called: " + owner);
        getCursor();
        if(cursor.getCount()>0)
        {
            cursor.moveToNext();
            int goalsColumn = cursor.getColumnIndex(Goals.GOAL);
            goals.setText(cursor.getString(goalsColumn));
            
        }
        else
        {
            goals.setText("");
        }
        cursor.close();
    }
    
    private void saveNote()
    {
        ContentValues values = new ContentValues();
        
        String goal = goals.getText().toString();
        //Log.d("NoteEditorActivity", "note: " + text);
        int glength = goal.length();
        
        if (glength == 0) 
        {
            Toast.makeText(this, getString(R.string.nothing_to_save), Toast.LENGTH_SHORT).show();
            return;
        }
        
        values.put(Goals.GOAL, goal);
        
        //check if a note already exists for today
        DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());
        getCursor();
        if(cursor.getCount()>0)
        { 
            //Log.d("MainActivity", "saveNote(): doing update ");
            StringBuilder sb = new StringBuilder();
            sb.append("update goals set goals_goal = '");
            sb.append(escape(goal));
            sb.append("'");
            dbHelper.getReadableDatabase().execSQL(sb.toString());
            getContentResolver().update(Goals.CONTENT_URI, values, null,null);
        }
        else
        {
            getContentResolver().insert(Goals.CONTENT_URI, values);
        }
        cursor.close();
        
    }
    
    private void getCursor()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("select goals_goal from goals");
        DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("select goals_goal from goals", null);
    }
    
    private boolean isTabletDevice(Display d, Activity context) 
    {
        if (android.os.Build.VERSION.SDK_INT >= 11) 
        { // honeycomb
            
            return true;

        }
        return false;
    }
    
    private String escape(String text)
    {
        String returnVal = "";
        
        returnVal = text.replace("'", "''");
        
        return returnVal;
    }
    


}
