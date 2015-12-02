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
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.acrylicgoat.scrumnotes.listener.DrawerItemClickListener;
import com.acrylicgoat.scrumnotes.provider.DatabaseHelper;
import com.acrylicgoat.scrumnotes.provider.Goals;
import com.acrylicgoat.scrumnotes.util.ScrumNotesUtil;

import java.util.HashMap;
import java.util.List;

/**
 * @author ed woodward
 * 
 * Activity for daily meeting notes
 */
public class DailyNotesActivity extends Activity
{
    private Cursor cursor;
    private EditText note;
    ActionBar aBar;
    private ActionBarDrawerToggle drawerToggle;
    String[] from = { "nav_icon","nav_item" };
    int[] to = { R.id.nav_icon , R.id.nav_item};

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailynotes);
        aBar = getActionBar();
        aBar.setTitle(getString(R.string.app_name));
        aBar.setDisplayHomeAsUpEnabled(true);
        aBar.setIcon(android.R.color.transparent);
        note = (EditText) findViewById(R.id.editDaily);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;
        if(isTabletDevice())
        {
            if(screenWidth < screenHeight)
            {
                //in portrait so give more lines
                note.setLines(200);
            }
            else
            {
                note.setLines(50);
            }
                
        }
        getNotes();

        List<HashMap<String,String>> navTitles = ScrumNotesUtil.setNavDrawer(this);
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView)findViewById(R.id.left_drawer);
        SimpleAdapter sAdapter = new SimpleAdapter(this,navTitles, R.layout.nav_drawer,from,to);

        // Set the list's click listener
        drawerList.setOnItemClickListener(new DrawerItemClickListener(this, drawerLayout));

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close)
        {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(getString(R.string.app_name));
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(getString(R.string.app_name));
                invalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
        aBar.setDisplayHomeAsUpEnabled(true);
        aBar.setHomeButtonEnabled(true);
        drawerList.setAdapter(sAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_dailynotes, menu);
        
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.clear();
        getMenuInflater().inflate(R.menu.activity_dailynotes, menu);
        
        return true;
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        if(item.getItemId() == R.id.save)
        {
            saveNote();
            return true;
        }
        else if(item.getItemId() == android.R.id.home)
        {
            Intent mainIntent = new Intent(getApplicationContext(), DailyNotesActivity.class);
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
    
    private boolean isTabletDevice()
    {
        if (android.os.Build.VERSION.SDK_INT >= 11) 
        { // honeycomb
            
            return true;

        }
        return false;
    }


}
