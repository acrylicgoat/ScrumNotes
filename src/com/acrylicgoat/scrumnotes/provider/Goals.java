/**
 * Copyright (c) 2013 Acrylic Goat Software
 * 
 * This software is subject to the provisions of the GNU Lesser General
 * Public License Version 3 (LGPL).  See LICENSE.txt for details.
 */
package com.acrylicgoat.scrumnotes.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author ed woodward
 * 
 * Helper class for GoalsProvider
 *
 */
public class Goals implements BaseColumns
{
    private Goals()
    {
        
    }
    
    public static final Uri CONTENT_URI = Uri.parse("content://com.acrylicgoat.scrumnotes.provider.GoalsProvider");
    /** id column name*/
    public static final String ID = "_id";
    public static final String GOAL = "goals_goal";
    public static final String NOTE = "goals_note";

}
