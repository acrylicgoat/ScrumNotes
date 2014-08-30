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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.acrylicgoat.scrumnotes.provider.DatabaseHelper;
import com.acrylicgoat.scrumnotes.provider.Goals;
import com.acrylicgoat.scrumnotes.util.ScrumNotesUtil;

import java.util.ArrayList;
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
    private List<HashMap<String,String>> navTitles;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
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

        String[] items = getResources().getStringArray(R.array.nav_list);
        setDrawer(items);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerList = (ListView)findViewById(R.id.left_drawer);
        SimpleAdapter sAdapter = new SimpleAdapter(this,navTitles, R.layout.nav_drawer,from,to);

        // Set the adapter for the list view
        //drawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, navTitles));
        // Set the list's click listener
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(getString(R.string.app_name));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(getString(R.string.app_name));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
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
    
    private boolean isTabletDevice(Display d, Activity context) 
    {
        if (android.os.Build.VERSION.SDK_INT >= 11) 
        { // honeycomb
            
            return true;

        }
        return false;
    }

    private void selectItem(int position)
    {
        switch (position)
        {
            case 0:
                drawerLayout.closeDrawers();
                break;
            case 1:
                Intent dailyIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(dailyIntent);
                break;
            case 2:
                Intent devIntent = new Intent(getApplicationContext(), DevActivity.class);
                startActivity(devIntent);
                break;
            case 3:
                Intent goalsIntent = new Intent(getApplicationContext(), GoalsActivity.class);
                startActivity(goalsIntent);
                break;
            case 4:
                Intent noteIntent = new Intent(getApplicationContext(), NotesActivity.class);
                startActivity(noteIntent);
                break;
            case 5:
                Intent eventIntent = new Intent(getApplicationContext(), EventActivity.class);
                startActivity(eventIntent);
                break;
            case 6:
                Intent reportIntent = new Intent(getApplicationContext(), DataTableActivity.class);
                startActivity(reportIntent);
                break;
            case 7:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.acrylicgoat.scrumnotes")));
                break;
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        //@Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {
            selectItem(position);
        }
    }

    private void setDrawer(String[] items)
    {
        HashMap<String,String> hm1 = new HashMap<String,String>();
        hm1.put("nav_icon",Integer.toString(R.drawable.home));
        hm1.put("nav_item",items[0]);

        HashMap<String,String> hm2 = new HashMap<String,String>();
        hm2.put("nav_icon",Integer.toString(R.drawable.edit));
        hm2.put("nav_item",items[1]);

        HashMap<String,String> hm3 = new HashMap<String,String>();
        hm3.put("nav_icon",Integer.toString(R.drawable.dev));
        hm3.put("nav_item",items[2]);

        HashMap<String,String> hm4 = new HashMap<String,String>();
        hm4.put("nav_icon",Integer.toString(R.drawable.ic_action_time));
        hm4.put("nav_item",items[3]);

        HashMap<String,String> hm5 = new HashMap<String,String>();
        hm5.put("nav_icon",Integer.toString(R.drawable.ic_action_chat));
        hm5.put("nav_item",items[4]);

        HashMap<String,String> hm6 = new HashMap<String,String>();
        hm6.put("nav_icon",Integer.toString(R.drawable.ic_action_new_event));
        hm6.put("nav_item",items[5]);

        HashMap<String,String> hm7 = new HashMap<String,String>();
        hm7.put("nav_icon",Integer.toString(R.drawable.ic_action_group));
        hm7.put("nav_item",items[6]);

        HashMap<String,String> hm8 = new HashMap<String,String>();
        hm8.put("nav_icon",Integer.toString(R.drawable.star));
        hm8.put("nav_item",items[7]);

        navTitles = new ArrayList<HashMap<String,String>>();

        navTitles.add(hm1);
        navTitles.add(hm2);
        navTitles.add(hm3);
        navTitles.add(hm4);
        navTitles.add(hm5);
        navTitles.add(hm6);
        navTitles.add(hm7);
        navTitles.add(hm8);
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
