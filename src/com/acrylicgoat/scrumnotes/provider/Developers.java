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
 * @author ed Woodward
 * 
 * Helper class for DeveloperProvider
 *
 */
public class Developers implements BaseColumns
{
    private Developers()
    {
        
    }
    
    public static final Uri CONTENT_URI = Uri.parse("content://com.acrylicgoat.scrumnotes.provider.DevelopersProvider");
    /** id column name*/
    public static final String ID = "_id";
    public static final String NAME = "developers_name";
    public static final String SCRUMMASTER = "developers_scrummaster";

}
