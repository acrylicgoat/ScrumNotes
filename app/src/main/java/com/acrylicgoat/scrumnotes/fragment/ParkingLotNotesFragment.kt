package com.acrylicgoat.scrumnotes.fragment

import android.app.PendingIntent.getActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.EditText
import com.acrylicgoat.scrumnotes.R
import android.widget.Toast
import android.content.Intent
import com.acrylicgoat.scrumnotes.util.ScrumNotesUtil



class ParkingLotNotesFragment : androidx.fragment.app.Fragment()
{

    private var note: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        //activity = getActivity()


        return inflater.inflate(R.layout.fragment_parking_notes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        note = view?.findViewById(R.id.editNotes);

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater!!.inflate(R.menu.menu_notes, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {

        if (item.itemId === R.id.share)
        {
            val noteStr = note!!.getText().toString()
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = getString(R.string.mimetype_text)

            if (noteStr != "")
            {
                val today = ScrumNotesUtil.todaysDate
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.parking_lot_note) + " " + today)
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    noteStr + getString(R.string.two_new_lines) + getString(R.string.shared_via)
                )

                val chooser =
                    Intent.createChooser(intent, getString(R.string.tell_friend) + getString(R.string.meeting_notes))
                startActivity(chooser)
            } else {
                Toast.makeText(view!!.context, getString(R.string.no_data_msg), Toast.LENGTH_LONG).show()
            }
            return true
        }

        return true
    }
}