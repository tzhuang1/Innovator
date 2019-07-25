package com.example.angela_innovator_v2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.innovatorsetup.R

class MainActivity : Fragment() {

    //private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> { //HOME
                //textMessage.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> { //EXPLORE
                //textMessage.setText(R.string.title_explore)
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ExploreFragment() ).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> { //SETTINGS
                //textMessage.setText(R.string.title_notifications)
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SettingsFragment() ).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account-> {
                //textMessage.setText(R.string.title_account)
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, AccountFragment() ).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_bottom_bar, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navView: BottomNavigationView = view.findViewById(R.id.nav_view)

        //textMessage = findViewById(R.id.)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        //val fm : FragmentManager = getFragmentManager();
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, HomeFragment() ).commit()
        super.onViewCreated(view, savedInstanceState)
    }
}



