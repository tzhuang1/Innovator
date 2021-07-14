package com.example.solve

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.settings_fragment.*


class SettingsFragment : Fragment() {

    var sp: SharedPreferences? = null
    var saved: SharedPreferences? = null
    var gradeStr: String? = null
    var actPerDayStr: String? = null
    var notifsBool: Boolean? = false

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

        saved = this.getActivity()?.getSharedPreferences("pref", Context.MODE_PRIVATE);
        gradeSettingsNumInput.setText(saved?.getString("grade", ""))
        activitiesPerDayNumInput.setText(saved?.getString("activitiesPerDay",""))
        saved?.getBoolean("notifsChecked", false)?.let { notificationsCheckbox.setChecked(it) }

        settingsSaveBtn.setOnClickListener(View.OnClickListener {
            gradeStr = gradeSettingsNumInput.getText().toString()
            actPerDayStr = activitiesPerDayNumInput.getText().toString()
            notifsBool = notificationsCheckbox.isChecked()

            sp = this.getActivity()?.getSharedPreferences("pref", Context.MODE_PRIVATE);
            val editor = sp!!.edit()
            editor.putString("grade", gradeStr)
            editor.putString("activitiesPerDay", actPerDayStr)
            editor.putBoolean("notifsChecked", notifsBool!!)
            editor.commit()

            Toast.makeText(this.requireActivity(), "Settings saved.", Toast.LENGTH_SHORT).show()
        })

        // TODO: Use the ViewModel
    }


}
