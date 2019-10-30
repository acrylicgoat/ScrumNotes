package com.acrylicgoat.scrumnotes.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.text.HtmlCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.acrylicgoat.scrumnotes.R
import com.acrylicgoat.scrumnotes.fragment.DailyNotesFragment
import com.acrylicgoat.scrumnotes.fragment.EventFragment
import com.acrylicgoat.scrumnotes.fragment.ParkingLotNotesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //setSupportActionBar(toolbar)
        supportActionBar!!.title = HtmlCompat.fromHtml(getString(R.string.app_name), HtmlCompat.FROM_HTML_MODE_LEGACY)
        val context = this


        navView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_daily -> {
                    selectedFragment = DailyNotesFragment()
                }
                R.id.navigation_event -> {
                    selectedFragment = EventFragment()
                }
                R.id.navigation_parking -> {
                    selectedFragment = ParkingLotNotesFragment()
                }

            }
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, selectedFragment!!)
            transaction.commit()
            true
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, DailyNotesFragment())
        transaction.commit()
    }
}
