/**
 * Copyright (c) 2015 Acrylic Goat Software
 *
 * This software is subject to the provisions of the GNU Lesser General
 * Public License Version 3 (LGPL).  See LICENSE.txt for details.
 */
package com.acrylicgoat.scrumnotes.listener;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.acrylicgoat.scrumnotes.DailyNotesActivity;
import com.acrylicgoat.scrumnotes.DataTableActivity;
import com.acrylicgoat.scrumnotes.DevActivity;
import com.acrylicgoat.scrumnotes.EventActivity;
import com.acrylicgoat.scrumnotes.GoalsActivity;
import com.acrylicgoat.scrumnotes.NotesActivity;

/**
 * Created by ed on 11/22/15.
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener
{
    private Context context;
    private DrawerLayout drawerLayout;

    public DrawerItemClickListener(Context c, DrawerLayout layout)
    {
        context = c;
        drawerLayout = layout;
    }

    public void onItemClick(AdapterView parent, View view, int position, long id)
    {
        selectItem(position);
    }

    private void selectItem(int position)
    {
        switch (position)
        {
            case 0:
                Intent dailyIntent = new Intent(context, DailyNotesActivity.class);
                context.startActivity(dailyIntent);
                drawerLayout.closeDrawers();
                break;
            case 1:
                drawerLayout.closeDrawers();
                break;
            case 2:
                Intent devIntent = new Intent(context, DevActivity.class);
                context.startActivity(devIntent);
                drawerLayout.closeDrawers();
                break;
            case 3:
                Intent goalsIntent = new Intent(context, GoalsActivity.class);
                context.startActivity(goalsIntent);
                drawerLayout.closeDrawers();
                break;
            case 4:
                Intent noteIntent = new Intent(context, NotesActivity.class);
                context.startActivity(noteIntent);
                drawerLayout.closeDrawers();
                break;
            case 5:
                Intent eventIntent = new Intent(context, EventActivity.class);
                context.startActivity(eventIntent);
                drawerLayout.closeDrawers();
                break;
            case 6:
                Intent reportIntent = new Intent(context, DataTableActivity.class);
                context.startActivity(reportIntent);
                drawerLayout.closeDrawers();
                break;
            case 7:
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.acrylicgoat.scrumnotes")));
                drawerLayout.closeDrawers();
                break;
        }
    }
}
