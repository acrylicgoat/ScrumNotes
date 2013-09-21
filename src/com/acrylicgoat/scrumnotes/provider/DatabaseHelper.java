/**
 * Copyright (c) 2013 Acrylic Goat Software
 * 
 * This software is subject to the provisions of the GNU Lesser General
 * Public License Version 3 (LGPL).  See LICENSE.txt for details.
 */
package com.acrylicgoat.scrumnotes.provider;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author ed woodward
 * 
 * Helper class shared by providers
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "scrumnotes.db";
    /**  database version */
    private static final int DATABASE_VERSION = 3;
    
    /**  Constructor*/
    public DatabaseHelper(Context context) 
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) 
    {
        
        db.execSQL("CREATE TABLE " + NotesProvider.NOTES_TABLE + " ("
                + Notes._ID + " INTEGER PRIMARY KEY,"
                + Notes.OWNER + " TEXT,"
                + Notes.NOTE + " TEXT,"
                + Notes.DATE + " TIMESTAMP NOT NULL DEFAULT current_timestamp"
                + ");");
        db.execSQL("CREATE TABLE " + DevelopersProvider.DEVELOPERS_TABLE + " ("
                + Notes._ID + " INTEGER PRIMARY KEY,"
                + Developers.NAME + " TEXT,"
                + Developers.SCRUMMASTER + " TEXT"
                + ");");
        db.execSQL("CREATE TABLE " + GoalsProvider.GOALS_TABLE + " ("
                + Notes._ID + " INTEGER PRIMARY KEY,"
                + Goals.GOAL + " TEXT,"
                + Goals.NOTE + " TEXT"
                + ");");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
        //db.execSQL("alter table " + FAVS_TABLE + " add column " + Favs.OTHER + " text");
//        db.execSQL("CREATE TABLE " + NotesProvider.NOTES_TABLE + " ("
//                + Notes._ID + " INTEGER PRIMARY KEY,"
//                + Notes.OWNER + " TEXT,"
//                + Notes.NOTE + " TEXT,"
//                + Notes.DATE + " TIMESTAMP NOT NULL DEFAULT current_timestamp"
//                + ");");
//        db.execSQL("CREATE TABLE " + DevelopersProvider.DEVELOPERS_TABLE + " ("
//                + Notes._ID + " INTEGER PRIMARY KEY,"
//                + Developers.NAME + " TEXT,"
//                + Developers.SCRUMMASTER + " TEXT"
//                + ");");
//        db.execSQL("CREATE TABLE " + GoalsProvider.GOALS_TABLE + " ("
//                + Notes._ID + " INTEGER PRIMARY KEY,"
//                + Goals.GOAL + " TEXT,"
//                + Goals.NOTE + " TEXT"
//                + ");");
    }

}
