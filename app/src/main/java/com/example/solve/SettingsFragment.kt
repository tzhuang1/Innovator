package com.example.solve

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.settings_fragment.*
import java.util.*


class SettingsFragment : Fragment() {

    var sp: SharedPreferences? = null
    var saved: SharedPreferences? = null
    var gradeStr: String? = null
    var actPerDayStr: String? = null
    var notifsBool: Boolean? = false
    var notifsTime: String? = null

    var displayHour: Int = 0
    var displayMinute: Int = 0

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

        createNotificationChannel()

        //Load saved data
        saved = this.getActivity()?.getSharedPreferences("pref", Context.MODE_PRIVATE);
        gradeSettingsNumInput.setText(saved?.getString("grade", ""))
        activitiesPerDayNumInput.setText(saved?.getString("activitiesPerDay",""))
        saved?.getBoolean("notifsChecked", false)?.let { notifsCheckbox.setChecked(it) }
        notifsTimeDisplay.setText(saved?.getString("notifsTime",""))

        if (notifsCheckbox.isChecked()) {
            notifsTimeTxtView.isEnabled = true
            notifsTimeDisplay.isEnabled = true
            notifsTimeBtn.isEnabled = true
        }
        else{
            notifsTimeTxtView.isEnabled = false
            notifsTimeDisplay.isEnabled = false
            notifsTimeBtn.isEnabled = false
        }

        notifsCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                notifsTimeTxtView.isEnabled = true
                notifsTimeDisplay.isEnabled = true
                notifsTimeBtn.isEnabled = true
            }
            else{
                notifsTimeTxtView.isEnabled = false
                notifsTimeDisplay.isEnabled = false
                notifsTimeBtn.isEnabled = false
            }
        }

        //Set notification time
        notifsTimeBtn.setOnClickListener(View.OnClickListener {
            val timePickerDialog = TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                { view, hourOfDay, minute ->
                    displayHour = hourOfDay
                    displayMinute = minute

                    val calendar: Calendar = Calendar.getInstance()
                    calendar.set(0, 0, 0, displayHour, displayMinute)

                    // Display Selected date in textbox
                    notifsTimeDisplay.setText(DateFormat.format("hh:mm aa", calendar))
                }, 12, 0, false)
            timePickerDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);

            timePickerDialog.updateTime(displayHour, displayMinute)
            timePickerDialog.show()

        })

        //Save button, uses shared preferences to save data locally
        settingsSaveBtn.setOnClickListener(View.OnClickListener {
            gradeStr = gradeSettingsNumInput.getText().toString()
            actPerDayStr = activitiesPerDayNumInput.getText().toString()
            notifsBool = notifsCheckbox.isChecked()
            notifsTime = notifsTimeDisplay.getText().toString()

            sp = this.getActivity()?.getSharedPreferences("pref", Context.MODE_PRIVATE);
            val editor = sp!!.edit()
            editor.putString("grade", gradeStr)
            editor.putString("activitiesPerDay", actPerDayStr)
            editor.putBoolean("notifsChecked", notifsBool!!)
            editor.putString("notifsTime", notifsTime)
            editor.commit()

            //Notifications
            if (notifsBool as Boolean) {

                val intent = Intent(
                    this.activity,
                    ReminderBroadcast::class.java
                )
                val pendingIntent = PendingIntent.getBroadcast(this.activity, 0, intent, 0)
                val alarmManager = context?.let { it1 -> getSystemService(it1, AlarmManager::class.java) }
                val timeAtButtonClick = System.currentTimeMillis()
                val tenSecondsInMillis = (1000 * 10).toLong()

                if (alarmManager != null) {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        timeAtButtonClick + tenSecondsInMillis,
                        pendingIntent
                    )
                }
            }
            else{
                //disable the notification system
            }

            Toast.makeText(this.requireActivity(), "Settings saved.", Toast.LENGTH_SHORT).show()
        })

        // TODO: Use the ViewModel
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "ReminderChannel"
            val description = "Channel for reminder notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("notif", name, importance)
            channel.description = description
            val notificationManager: NotificationManager? =
                context?.let { getSystemService(it, NotificationManager::class.java) }
            notificationManager!!.createNotificationChannel(channel)
        }
    }
}
