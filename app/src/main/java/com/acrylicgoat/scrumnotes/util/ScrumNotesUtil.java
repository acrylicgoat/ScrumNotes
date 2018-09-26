/**
 * Copyright (c) 2013 Acrylic Goat Software
 * 
 * This software is subject to the provisions of the GNU Lesser General
 * Public License Version 3 (LGPL).  See LICENSE.txt for details.
 */
package com.acrylicgoat.scrumnotes.util;

import android.content.Context;

import com.acrylicgoat.scrumnotes.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * @author ed woodward
 * 
 * Utility class
 *
 */
public class ScrumNotesUtil
{
    public static String getTodaysDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date date = new Date();
        return dateFormat.format(date);
        
    }
    
    public static String getFileDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        Date date = new Date();
        return dateFormat.format(date);
        
    }
    
    public static String escape(String text)
    {

        return text.replace("'", "''");
        
    }
    public static ArrayList<HashMap<String,String>> setNavDrawer(Context context)
    {
        ArrayList<HashMap<String,String>> navTitles;
        String[] items = context.getResources().getStringArray(R.array.nav_list);
        HashMap<String,String> hm1 = new HashMap<>();
        hm1.put(context.getString(R.string.nav_icon),Integer.toString(R.drawable.home));
        hm1.put(context.getString(R.string.nav_item),items[0]);

        HashMap<String,String> hm2 = new HashMap<>();
        hm2.put(context.getString(R.string.nav_icon),Integer.toString(R.drawable.edit));
        hm2.put(context.getString(R.string.nav_item),items[1]);

        HashMap<String,String> hm3 = new HashMap<>();
        hm3.put(context.getString(R.string.nav_icon),Integer.toString(R.drawable.dev));
        hm3.put(context.getString(R.string.nav_item),items[2]);

        HashMap<String,String> hm4 = new HashMap<>();
        hm4.put(context.getString(R.string.nav_icon),Integer.toString(R.drawable.ic_action_time));
        hm4.put(context.getString(R.string.nav_item),items[3]);

        HashMap<String,String> hm5 = new HashMap<>();
        hm5.put(context.getString(R.string.nav_icon),Integer.toString(R.drawable.ic_action_chat));
        hm5.put(context.getString(R.string.nav_item),items[4]);

        HashMap<String,String> hm6 = new HashMap<>();
        hm6.put(context.getString(R.string.nav_icon),Integer.toString(R.drawable.ic_action_new_event));
        hm6.put(context.getString(R.string.nav_item),items[5]);

        HashMap<String,String> hm7 = new HashMap<>();
        hm7.put(context.getString(R.string.nav_icon),Integer.toString(R.drawable.ic_action_group));
        hm7.put(context.getString(R.string.nav_item),items[6]);

        HashMap<String,String> hm8 = new HashMap<>();
        hm8.put(context.getString(R.string.nav_icon),Integer.toString(R.drawable.star));
        hm8.put(context.getString(R.string.nav_item),items[7]);

        navTitles = new ArrayList<>();

        navTitles.add(hm1);
        navTitles.add(hm2);
        navTitles.add(hm3);
        navTitles.add(hm4);
        navTitles.add(hm5);
        navTitles.add(hm6);
        navTitles.add(hm7);
        navTitles.add(hm8);

        return navTitles;
    }

}
