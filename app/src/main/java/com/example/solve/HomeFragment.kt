package com.example.solve

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        val view = inflater!!.inflate(R.layout.fragment_home,container,false)

        val dailyChallengeBtn : Button = view.findViewById(R.id.dailyChallengeButton)
        dailyChallengeBtn.setOnClickListener { v ->
            val intent = Intent(getActivity(), QuestionMainActivity::class.java)
            startActivity(intent)
        }
        //setContentView(view);

//        // Get the text view widget reference from custom layout
//        val tv = view.findViewById<TextView>(R.id.text_view)
//
//        // Set a click listener for text view object
//        tv.setOnClickListener{
//            // Change the text color
//            tv.setTextColor(Color.RED)
//
//            // Show click confirmation
//            Toast.makeText(view.context,"TextView clicked.",Toast.LENGTH_SHORT).show()
//        }
//
//        // Return the fragment view/layout
        return view
    }

}