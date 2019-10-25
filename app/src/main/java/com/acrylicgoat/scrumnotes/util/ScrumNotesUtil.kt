/**
 * Copyright (c) 2013 Acrylic Goat Software
 *
 * This software is subject to the provisions of the GNU Lesser General
 * Public License Version 3 (LGPL).  See LICENSE.txt for details.
 */
package com.acrylicgoat.scrumnotes.util

import android.content.Context

import com.acrylicgoat.scrumnotes.R

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.Locale

/**
 * @author ed woodward
 *
 * Utility class
 */
object ScrumNotesUtil {
    val todaysDate: String
        get() {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            val date = Date()
            return dateFormat.format(date)

        }

    val fileDate: String
        get() {
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
            val date = Date()
            return dateFormat.format(date)

        }

    fun escape(text: String): String {

        return text.replace("'", "''")

    }

}
