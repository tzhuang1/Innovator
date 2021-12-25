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
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.settings_fragment.*
import java.util.*


class SettingsFragment : Fragment() {

    var sp: SharedPreferences? = null
    var saved: SharedPreferences? = null
    var gradeStr: String? = null
    var actPerDayStr: String? = null
    var notifsBool: Boolean = false
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

        //enable or disable notification settings based on saved checkbox
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

        //enable or disable notification settings based on user clicking it
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

            //require grade and if notifs are enabled, activities per day goal
            if (gradeSettingsNumInput.getText().toString() == ""){
                Toast.makeText(this.requireActivity(), "Please enter your grade before saving!", Toast.LENGTH_SHORT).show()
            }
            else if (activitiesPerDayNumInput.getText().toString() == "" && notifsCheckbox.isChecked()) {
                Toast.makeText(this.requireActivity(), "Please set a daily goal for reminders!", Toast.LENGTH_SHORT).show()
            }
            else{
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
                val intent = Intent(
                        this.activity,
                        ReminderBroadcast::class.java
                )
                val pendingIntent = PendingIntent.getBroadcast(this.activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

                //Notifications
                if (notifsBool as Boolean) {

                    println(notifsTimeDisplay.toString())

                    val calendar = Calendar.getInstance()
                    var notifHour:Int =  notifsTimeDisplay.getText().toString().subSequence(0,2).toString().toInt()
                    var notifMinute:Int =  notifsTimeDisplay.getText().toString().subSequence(3,5).toString().toInt()

                    //changes 12 hour clock to 24 hour clock for the alarm
                    if (notifHour == 12) {
                        notifHour = 0
                    }
                    if (notifsTimeDisplay.getText().toString().subSequence(6,8) == "pm"){
                        notifHour += 12
                    }

                    //sets notification at the time the user set
                    calendar[Calendar.HOUR_OF_DAY] = notifHour
                    calendar[Calendar.MINUTE] = notifMinute
                    calendar[Calendar.SECOND] = 0

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pendingIntent);
                }
                else{
                    alarmManager.cancel(pendingIntent);
                }

                Toast.makeText(this.requireActivity(), "Settings saved.", Toast.LENGTH_SHORT).show()
            }
        })

        // TODO: Use the ViewModel
        // TODO: Change notification message based on how many questions left to do and don't send it if goal is met
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

    fun getGoal(): String? {
        return actPerDayStr
    }
}