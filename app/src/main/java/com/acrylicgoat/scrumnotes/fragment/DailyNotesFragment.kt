package com.acrylicgoat.scrumnotes.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.acrylicgoat.scrumnotes.R
import android.widget.EditText
import android.widget.Toast
import android.content.ContentValues
import android.database.Cursor
import android.view.*
import com.acrylicgoat.scrumnotes.provider.DatabaseHelper
import com.acrylicgoat.scrumnotes.provider.Goals
import com.acrylicgoat.scrumnotes.util.ScrumNotesUtil




class DailyNotesFragment : androidx.fragment.app.Fragment()
{
    private var note: EditText? = null
    private var cursor: Cursor? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        return inflater.inflate(R.layout.fragment_dailynotes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        note = view?.findViewById(R.id.editDaily)

        note!!.setLines(50)

        getNotes()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater!!.inflate(R.menu.menu_dailynotes, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {

        if (item.itemId === R.id.save) {
            saveNote()
            return true
        }

        return true
    }

    private fun getNotes() {
        //Log.d("DevNotesActivity", "getYesterday() called: " + owner);

        val dbHelper = DatabaseHelper(view!!.context)
        val db = dbHelper.getReadableDatabase()
        cursor = db.rawQuery("select goals_note from goals", null)
        if (cursor!!.getCount() > 0) {
            cursor!!.moveToNext()
            val notesColumn = cursor!!.getColumnIndex(Goals.NOTE)
            //Log.d("DevNotesActivity.getYesterday()", "notesColumn: " + notesColumn);
            note!!.setText(cursor!!.getString(notesColumn))

        } else {
            note!!.setText("")
        }
        cursor!!.close()
        db.close()
    }

    fun saveNote() {
        val values = ContentValues()

        val noteStr = note!!.getText().toString()
        //Log.d("NoteEditorActivity", "note: " + text);
        val nlength = noteStr.length

        if (nlength == 0) {
            Toast.makeText(view!!.context, getString(com.acrylicgoat.scrumnotes.R.string.nothing_to_save), Toast.LENGTH_SHORT).show()
            return
        }

        values.put(Goals.NOTE, noteStr)

        //check if a note already exists for today
        val dbHelper = DatabaseHelper(view!!.context)
        val db = dbHelper.getReadableDatabase()
        cursor = db.rawQuery("select goals_goal from goals", null)
        if (cursor!!.getCount() > 0) {
            //Log.d("DevNotesActivity", "saveNote(): doing update ");
            val sb = StringBuilder()
            sb.append("update goals set goals_note = '")
            sb.append(ScrumNotesUtil.escape(noteStr))
            sb.append("'")
            dbHelper.getReadableDatabase().execSQL(sb.toString())
            activity!!.getContentResolver().update(Goals.CONTENT_URI, values, null, null)
        } else {
            activity!!.getContentResolver().insert(Goals.CONTENT_URI, values)
        }
        cursor!!.close()
        db.close()
        Toast.makeText(view!!.context, getString(R.string.note_saved), Toast.LENGTH_SHORT).show()

    }
}