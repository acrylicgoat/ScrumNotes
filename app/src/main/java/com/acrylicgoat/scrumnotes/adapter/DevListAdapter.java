/**
 * Copyright (c) 2013 Acrylic Goat Software
 * 
 * This software is subject to the provisions of the GNU Lesser General
 * Public License Version 3 (LGPL).  See LICENSE.txt for details.
 */
package com.acrylicgoat.scrumnotes.adapter;

import java.util.ArrayList;

import com.acrylicgoat.scrumnotes.R;
import com.acrylicgoat.scrumnotes.beans.Developer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author ed woodward
 * 
 * List adapter to display list of developers
 *
 */
public class DevListAdapter extends ArrayAdapter<Developer>
{
    /** Current context */
    private Context context;
    private ArrayList<Developer> devList;

    public DevListAdapter(Context context, ArrayList <Developer> devList)
    {
        super(context, android.R.layout.simple_list_item_1, devList);
        this.context = context;
        this.devList = devList;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        View v = convertView;
        DevHolder holder;
        if (v == null) 
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.dev_list, null);
            holder = new DevHolder(v);
            v.setTag(holder);
        }
        else
        {
            holder= (DevHolder)v.getTag();
            if(holder == null)
            {
                holder = new DevHolder(v);
                v.setTag(holder);
            }
        }
        
        Developer d = devList.get(position);
        if(d != null)
        {
            //Log.d("LandingListAdapter", "title = " + c.title);
            TextView name = holder.nameView;
            if(name != null)
            {
                holder.nameView.setText(d.getName());
            }
        }
        else
        {
            //Log.d("LandingListAdapter", "Content is null");
        }
        
        return v;
    }

}
