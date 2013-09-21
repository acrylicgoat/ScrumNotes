/**
 * Copyright (c) 2013 Acrylic Goat Software
 * 
 * This software is subject to the provisions of the GNU Lesser General
 * Public License Version 3 (LGPL).  See LICENSE.txt for details.
 */
package com.acrylicgoat.scrumnotes.beans;

/**
 * @author ed woodward
 * 
 * Bean to hold developer data from database
 *
 */
public class DevNote
{
    private String date;
    private String devName;
    private String note;
    
    public String getDate()
    {
        return date;
    }
    public void setDate(String date)
    {
        this.date = date;
    }
    public String getDevName()
    {
        return devName;
    }
    public void setDevName(String devName)
    {
        this.devName = devName;
    }
    public String getNote()
    {
        return note;
    }
    public void setNote(String note)
    {
        this.note = note;
    }
    

}
