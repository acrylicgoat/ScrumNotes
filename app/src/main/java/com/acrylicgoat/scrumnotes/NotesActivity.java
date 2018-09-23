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

import com.acrylicgoat.scrumnotes.util.ScrumNotesUtil;

/**
 * @author ed woodward
 * 
 * Activity for capturing Parking Lot notes
 *
 */
public class NotesActivity extends Activity
{
    ActionBar aBar;
    private EditText note;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        aBar = getActionBar();
        aBar.setTitle(getString(R.string.app_name));
        note = findViewById(R.id.editNotes);
        aBar.setDisplayHomeAsUpEnabled(true);
        aBar.setIcon(android.R.color.transparent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_notes, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.share)
        {
            String noteStr = note.getText().toString();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType(getString(R.string.mimetype_text));

            if(!noteStr.equals(""))
            {
                String today = ScrumNotesUtil.getTodaysDate();
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.parking_lot_note) + " " + today);
                intent.putExtra(Intent.EXTRA_TEXT, noteStr + getString(R.string.two_new_lines) + getString(R.string.shared_via));

                Intent chooser = Intent.createChooser(intent, getString(R.string.tell_friend) + getString(R.string.meeting_notes));
                startActivity(chooser);
            }
            else
            {
                Toast.makeText(NotesActivity.this, getString(R.string.no_data_msg),  Toast.LENGTH_LONG).show();
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
    

}
