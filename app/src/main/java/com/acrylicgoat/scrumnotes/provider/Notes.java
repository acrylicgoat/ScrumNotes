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
 * Helper class for NotesProvider
 *
 */
public class Notes implements BaseColumns
{
    /** Private constructor.  Cannot instanciate this class */
    private Notes()
    {
        
    }
    
    public static final Uri CONTENT_URI = Uri.parse("content://com.acrylicgoat.scrumnotes.provider.NotesProvider");
    /** title column name*/
    public static final String OWNER = "notes_owner";
    /** url column name*/
    public static final String DATE = "notes_date";
    /** id column name*/
    public static final String ID = "_id";
    /** other column name*/
    public static final String NOTE = "notes_note";

}
