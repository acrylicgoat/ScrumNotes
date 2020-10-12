package com.acrylicgoat.scrumnotes.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.EditText
import com.acrylicgoat.scrumnotes.R
import android.content.Intent
import android.widget.Toast
import android.view.*


class EventFragment : androidx.fragment.app.Fragment()
{

    var title: EditText? = null
    var description: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        title = view?.findViewById(R.id.eventTitle);
        description = view?.findViewById(R.id.eventDescription);
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){
        inflater?.inflate(R.menu.menu_event, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === R.id.event) {
            val titleStr = title!!.getText().toString()
            if (titleStr == "") {
                Toast.makeText(view!!.context, "Please enter a title.", Toast.LENGTH_SHORT).show()
                return true
            }
            val descr = description!!.getText().toString()

            val intent = Intent(Intent.ACTION_EDIT)
            intent.type = "vnd.android.cursor.item/event"
            intent.putExtra("title", titleStr)
            intent.putExtra("description", descr)
            startActivity(intent)

        }
        return true
    }
}