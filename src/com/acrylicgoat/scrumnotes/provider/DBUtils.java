/**
 * Copyright (c) 2013 Acrylic Goat Software
 * 
 * This software is subject to the provisions of the GNU Lesser General
 * Public License Version 3 (LGPL).  See LICENSE.txt for details.
 */
package com.acrylicgoat.scrumnotes.provider;

import java.util.ArrayList;

import com.acrylicgoat.scrumnotes.beans.Developer;

import android.database.Cursor;

/**
 * @author ed woodward
 * 
 * Utility class for database operations
 *
 */
public class DBUtils
{
    /**
     * Private constructor since all methods are static
     */
    private DBUtils()
    {
        
    }
    
    /**
     * Read Cursor into ArrayList of Content objects.  Closes the cursor when read is finished.
     * @param c - the Cursor to read
     * @return ArrayList<Content>
     */
    public static ArrayList<Developer> readCursorIntoList(Cursor c)
    {
        ArrayList<Developer> devList = new ArrayList<Developer>();
        
        int nameColumn = c.getColumnIndex(Developers.NAME); 
        int scrummasterColumn = c.getColumnIndex(Developers.SCRUMMASTER);
        if(c.getCount() > 0)
        {
            c.moveToNext();
            do
            {
                Developer dev = new Developer();
                dev.setName(c.getString(nameColumn));
                String sm = c.getString(scrummasterColumn);
                if(sm != null && sm.equals("Y"))
                {
                    dev.setScrumMaster(true);
                }
                else
                {
                    dev.setScrumMaster(false);
                }
                devList.add(dev);
                
            }while(c.moveToNext());
        }
        c.close();
        
        return devList;
        
    }

}
