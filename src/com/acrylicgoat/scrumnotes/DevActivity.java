/**
 * Copyright (c) 2013 Acrylic Goat Software
 * 
 * This software is subject to the provisions of the GNU Lesser General
 * Public License Version 3 (LGPL).  See LICENSE.txt for details.
 */
package com.acrylicgoat.scrumnotes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.acrylicgoat.scrumnotes.adapter.DevListAdapter;
import com.acrylicgoat.scrumnotes.beans.Developer;
import com.acrylicgoat.scrumnotes.provider.DBUtils;
import com.acrylicgoat.scrumnotes.provider.Developers;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

/**
 * @author ed woodward
 * 
 * Activity for adding Developers to app
 *
 */
public class DevActivity extends SherlockActivity
{
    ActionBar aBar;
    ArrayList<Developer> devs;
    DevListAdapter adapter;
    ListView devList;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devs);
        aBar = getSupportActionBar();
        aBar.setTitle("Scrum Notes - Developers");
        aBar.setDisplayHomeAsUpEnabled(true);
        //get devs from database
        readDB();
        setUI();
        
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
    {
        //Log.d("ViewLenses.onCreateContextMenu()", "Called");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Developer dev = (Developer)devList.getItemAtPosition(info.position);
        menu.setHeaderTitle(dev.getName());
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.activity_dev, menu);
    }
    
    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) 
    {
        AdapterContextMenuInfo info= (AdapterContextMenuInfo) item.getMenuInfo();
        Developer dev = (Developer)devList.getItemAtPosition(info.position);
        if(item.getItemId() == R.id.delete_from__devs)
        {
            deleteDeveloper(dev.getName());
            devs.remove(dev);
            
            adapter.notifyDataSetChanged();
        }
        
        return super.onContextItemSelected(item);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        if(item.getItemId() == android.R.id.home)
        {
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        }
        return true;
    }
    
    private void readDB()
    {
        Thread loadFavsThread = new Thread() 
        {
          public void run() 
          {
              
              devs = DBUtils.readCursorIntoList(getContentResolver().query(Developers.CONTENT_URI, null, null, null, null));
              
             Collections.sort((List<Developer>)devs);
              
             fillData(devs);
             
          }
        };
        loadFavsThread.start();
        
    }
    
    private void fillData(ArrayList<Developer> devs)
    {
        devList = (ListView)findViewById(R.id.devsList);
        adapter = new DevListAdapter(DevActivity.this,devs);
        devList.setAdapter(adapter);
        registerForContextMenu(devList);
    }
    
    private void setUI()
    {
        ImageButton devSaveButton = (ImageButton)findViewById(R.id.devSave);
        devSaveButton.setOnClickListener(new OnClickListener() 
        {
                  
              public void onClick(View v) 
              {
                  EditText devName = (EditText)findViewById(R.id.devName);
                  saveDeveloper(devName.getText().toString());
                  Developer dev = new Developer();
                  dev.setName(devName.getText().toString());
                  devName.setText("");

                  devs.add(dev);
                  Collections.sort((List<Developer>)devs);
                  adapter.notifyDataSetChanged();
                  
              }
          });
        EditText devName = (EditText)findViewById(R.id.devName);
        devName.setOnKeyListener(new View.OnKeyListener()
        {
            
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER)
                    {
                        EditText devName = (EditText)findViewById(R.id.devName);
                        saveDeveloper(devName.getText().toString());
                    }
                }
                return false;
            }
        });

    }
    
    //save dev
    private void saveDeveloper(String name)
    {
        ContentValues values = new ContentValues();
        
        //Log.d("NoteEditorActivity", "note: " + text);
        int length = name.length();
        
        if (length == 0) 
        {
            Toast.makeText(this, "Name not entered, so nothing to save.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        
        values.put(Developers.NAME, name);

        //values.put(Developers.SCRUMMASTER, isScrummaster);

        try 
        {
            
            getContentResolver().insert(Developers.CONTENT_URI, values);
        } 
        catch (NullPointerException e) 
        {
            Log.e("DevActivity", e.getMessage());
        }
        
    }
    
    private void deleteDeveloper(String name)
    {
        getContentResolver().delete(Developers.CONTENT_URI, "developers_name='"+name+"'" , null);
    }

}
