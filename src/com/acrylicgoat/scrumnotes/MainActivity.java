/**
 * Copyright (c) 2013 Acrylic Goat Software
 * 
 * This software is subject to the provisions of the GNU Lesser General
 * Public License Version 3 (LGPL).  See LICENSE.txt for details.
 */
package com.acrylicgoat.scrumnotes;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import com.acrylicgoat.scrumnotes.beans.Developer;
import com.acrylicgoat.scrumnotes.provider.DBUtils;
import com.acrylicgoat.scrumnotes.provider.DatabaseHelper;
import com.acrylicgoat.scrumnotes.provider.Developers;
import com.acrylicgoat.scrumnotes.provider.Notes;
import com.acrylicgoat.scrumnotes.util.ScrumNotesUtil;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * @author ed woodward
 * 
 * Opening activity for app
 *
 */
public class MainActivity extends SherlockActivity 
{
    private Cursor cursor;
    private EditText today;
    private String currentOwner;
    private ImageButton yesterday;
    ActionBar aBar;
    ArrayList<Developer> devs;
    private TextView devName;
    private static final int MENUITEM = Menu.FIRST;
    SharedPreferences sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        sharedPref = getSharedPreferences("com.acrylicgoat.scrumnotes",MODE_PRIVATE);
        if(savedInstanceState != null)
        {
        	currentOwner = savedInstanceState.getString("currentOwner");
        }
        if(currentOwner == null || currentOwner.equals(""))
        {
        	currentOwner = sharedPref.getString("currentOwner", "");
        }
        aBar = getSupportActionBar();
        aBar.setTitle(getString(R.string.app_name));
        aBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        aBar.setDisplayHomeAsUpEnabled(false);
        today = (EditText) findViewById(R.id.editToday);
        today.setAutoLinkMask(Linkify.ALL);
        today.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	Linkify.addLinks(today, Linkify.ALL);
            	
            }
        });
        devName = (TextView) findViewById(R.id.devName);
        yesterday = (ImageButton)findViewById(R.id.calendarButton);
        yesterday.setOnClickListener(new OnClickListener() 
        {
                  
              public void onClick(View v) 
              {
                  
                  displayPopup();
                  
              }
          });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        readDB();
        if(devs != null && devs.size() > 0)
        {
            for (int i = 0; i < devs.size(); i++)
            {
                Developer dev = (Developer)devs.get(i);
                if(i == 0 && (currentOwner == null || currentOwner.equals("")))
                {
                    currentOwner = dev.getName();
                }
                menu.add(0, MENUITEM, 0, dev.getName()).setIcon(R.drawable.dev);
                
            }
        }
        getOwner(currentOwner);
        SpinnerAdapter mSpinnerAdapter;
        if(Build.VERSION.SDK_INT <= 10) 
        {
            mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.nav_list,android.R.layout.simple_spinner_item);
        }
        else
        {
            mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.nav_list,android.R.layout.simple_spinner_dropdown_item);
        }
        OnNavigationListener mOnNavigationListener = new OnNavigationListener() 
        {
            // Get the same strings provided for the drop-down's ArrayAdapter
            String[] strings = getResources().getStringArray(R.array.nav_list);

            @Override
            public boolean onNavigationItemSelected(int position, long itemId) 
            {
              switch (position)
              {
                  case 1:
                      Intent devIntent = new Intent(getApplicationContext(), DevActivity.class);
                      startActivity(devIntent);
                      break;
                  case 2:
                      Intent noteIntent = new Intent(getApplicationContext(), NotesActivity.class);
                      startActivity(noteIntent);
                      break;
                  case 3:
                      Intent eventIntent = new Intent(getApplicationContext(), EventActivity.class);
                      startActivity(eventIntent);
                      break;
                  case 4:
                      Intent dailyIntent = new Intent(getApplicationContext(), DailyNotesActivity.class);
                      startActivity(dailyIntent);
                      break;
                  case 5:
                      Intent goalsIntent = new Intent(getApplicationContext(), GoalsActivity.class);
                      startActivity(goalsIntent);
                      break;
                  case 6:
                      Intent reportIntent = new Intent(getApplicationContext(), DataTableActivity.class);
                      startActivity(reportIntent);
                      break;
                  case 7:
                	  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.acrylicgoat.scrumnotes")));
                	  break;
              }
              
              return true;
            }
          };
          aBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
          
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.clear();
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        readDB();
        if(devs != null && devs.size() > 0)
        {
            for (int i = 0; i < devs.size(); i++)
            {
                Developer dev = (Developer)devs.get(i);
                menu.add(0, MENUITEM, 0, dev.getName());
                
            }
        }
        return true;
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        String title = (String) item.getTitle();
        if(item.getItemId() == R.id.save)
        {
            saveNote();
            return true;
        }

        else
        {
            currentOwner=title;
            //Log.d("MainActivity", "currentOwner = " + currentOwner);
            getOwner(currentOwner);
        }

        return true;
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	SharedPreferences.Editor ed = sharedPref.edit();
        ed.putString("currentOwner", currentOwner);
        ed.commit();
    	saveNote();
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
        aBar.setSelectedNavigationItem(0);
        readDB();
        if(Build.VERSION.SDK_INT > 10 && devs != null && devs.size() > 0)
        {
            invalidateOptionsMenu();
        }
    }
    
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        //Log.d("ViewLenses.onSaveInstanceState()", "saving data");
        outState.putString("currentOwner", currentOwner);
        
    }
    
    private void displayPopup()
    {
      //retrieve yesterday's data
        String yData = getYesterday(currentOwner);
        if(yData.equals(""))
        {
            yData = getString(R.string.no_yesterday);
        }
        //display in popup
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Yesterday");
        alertDialog.setMessage(yData);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Done", new DialogInterface.OnClickListener() 
        {
              public void onClick(DialogInterface dialog, int which) 
              {
                  //do nothing
         
        } });
        alertDialog.show();
    }
    
    private void getOwner(String owner)
    {
        //Log.d("MainActivity", "getOwner() called: " + owner);
        getYesterday(owner);
        getToday(owner);
        devName.setText(currentOwner);
        
    }
    
    private String getYesterday(String owner)
    {
        //Log.d("MainActivity", "getYesterday() called: " + owner);
        StringBuilder sb = new StringBuilder();
        String results = "";
        sb.append("select notes_note from notes where notes_owner='");
        sb.append(currentOwner);
        
        Calendar calendar = new GregorianCalendar();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(day == 2)
        {
            //it is Monday so retrieve Friday
            sb.append("' and date(notes_date)=date('now','localtime','-3 day')");
        }
        else
        {
            //not Monday
            sb.append("' and date(notes_date)=date('now','localtime','-1 day')");
        }
        
        DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery(sb.toString(), null);
        if(cursor.getCount()>0)
        {
            cursor.moveToNext();
            int notesColumn = cursor.getColumnIndex(Notes.NOTE);
            //Log.d("MainActivity.getYesterday()", "notesColumn: " + notesColumn);
            results = cursor.getString(notesColumn);
            
        }

        cursor.close();
        db.close();
        return results;
    }
    
    private void getToday(String owner)
    {
        //Log.d("MainActivity", "getToday() called: " + owner);
        
        DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery(getTodaySQL(), null);
        if(cursor.getCount()>0)
        {
            cursor.moveToNext();
            int notesColumn = cursor.getColumnIndex(Notes.NOTE);
            today.setText(cursor.getString(notesColumn));
            
        }
        else
        {
            if(devs.size() > 0)
            {
                today.setText(getString(R.string.main_insert));
            }
            else
            {
                today.setText(getString(R.string.no_devs));
            }
                
        }
        Linkify.addLinks(today, Linkify.ALL);
        cursor.close();
        db.close();
    }
    
    private void saveNote()
    {
        ContentValues values = new ContentValues();
        
        String text = today.getText().toString() + " ";
        //Log.d("NoteEditorActivity", "note: " + text);
        int length = text.length();
        
        if (length == 0 || text.contains("To get started, select Tools") || text.equals("Yesterday: \n\nToday:")) 
        {
            //Toast.makeText(this, "Nothing to save.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        values.put(Notes.NOTE, text);
        values.put(Notes.OWNER, currentOwner);
        
        //check if a note already exists for today
        DatabaseHelper dbHelper = new DatabaseHelper(this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery(getTodaySQL(), null);
        if(cursor.getCount()>0)
        { 
            //Log.d("MainActivity", "saveNote(): doing update ");
            StringBuilder sb = new StringBuilder();
            sb.append("update notes set notes_note = '");
            sb.append(ScrumNotesUtil.escape(text));
            sb.append("' where notes_owner='");
            sb.append(currentOwner);
            sb.append("' and date(notes_date) = date('now','localtime')");
            dbHelper.getReadableDatabase().execSQL(sb.toString());
        }
        else
        {
            getContentResolver().insert(Notes.CONTENT_URI, values);
        }
        cursor.close();
        db.close();
        
    }
    
    private String getTodaySQL()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("select notes_note from notes where notes_owner='");
        sb.append(currentOwner);
        sb.append("' and date(notes_date) = date('now','localtime')");
        return sb.toString();
    }
    
//    private String escape(String text)
//    {
//        String returnVal = "";
//        
//        returnVal = text.replace("'", "''");
//        
//        return returnVal;
//    }
    
    private void readDB()
    {
         devs = DBUtils.readCursorIntoList(getContentResolver().query(Developers.CONTENT_URI, null, null, null, null));
          
         Collections.sort((List<Developer>)devs);
              
    }
    
}
