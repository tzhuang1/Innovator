package com.example.solve


import android.content.Context
import android.graphics.Color
import android.os.Bundle
//import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment


class HomeFragment : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        val view = inflater.inflate(R.layout.fragment_home,container,false)
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