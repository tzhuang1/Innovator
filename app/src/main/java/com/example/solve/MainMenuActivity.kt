package com.example.solve

import android.os.Bundle

import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {

    //private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> { //HOME
                //textMessage.setText(R.string.title_home)
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> { //EXPLORE
                //textMessage.setText(R.string.title_explore)
                //supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ExploreFragment() ).commit()
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    TopicSelectFragment()
                ).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> { //SETTINGS
                //textMessage.setText(R.string.title_notifications)
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SettingsFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {
                //textMessage.setText(R.string.title_account)
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, AccountFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.angela_activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        //textMessage = findViewById(R.id.)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, HomeFragment()).commit()
    }
}
