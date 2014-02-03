/**
 * Copyright (c) 2013 Acrylic Goat Software
 * 
 * This software is subject to the provisions of the GNU Lesser General
 * Public License Version 3 (LGPL).  See LICENSE.txt for details.
 */
package com.acrylicgoat.scrumnotes;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author ed woodward
 * 
 * Activity for adding an event to calendar
 *
 */
public class EventActivity extends Activity
{
    private EditText title;
    private EditText description;
    ActionBar aBar;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        aBar = getActionBar();
        aBar.setTitle("Scrum Notes - Event");
        aBar.setDisplayHomeAsUpEnabled(true);
        title = (EditText) findViewById(R.id.eventTitle);
        description = (EditText) findViewById(R.id.eventDescription);
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.event)
        {
            String titleStr = title.getText().toString();
            if(titleStr.equals(""))
            {
                Toast.makeText(EventActivity.this, "Please enter a title.",  Toast.LENGTH_SHORT).show();
            }
            String descr = description.getText().toString();
            
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("title", titleStr);
            intent.putExtra("description", descr);
            startActivity(intent);
            
        }
        else if(item.getItemId() == android.R.id.home)
        {
            Intent mainIntent = new Intent(getApplicationContext(), DailyNotesActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        }
        return true;
    }

}
