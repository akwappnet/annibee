package com.devstree.annibee.dacorator


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devstree.annibee.R
import com.devstree.annibee.adapter.EventBasedOnDateAdapter


class PopUpClass(val context: Context) {
    //PopupWindow display method
    @SuppressLint("ClickableViewAccessibility")
    fun showPopupWindow(view: View, text: String, x: Int, y: Int) {


        //Create a View object yourself through inflater
    /*    val inflater = view.getContext()
            .getSystemService(context.LAYOUT_INFLATER_SERVICE) as LayoutInflater*/
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)

        val popupView = LayoutInflater.from(context).inflate(
            R.layout.dialog_event_based_on_date,
            null
        )

        //Specify the length and width through constants
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT

        //Make Inactive Items Outside Of PopupWindow
        val focusable = true

//        val btnClose = popupView.findViewById<ImageView>(R.id.txtDone)
        val txtDate = popupView.findViewById<TextView>(R.id.txtDate)
        val rvEvent = popupView.findViewById<RecyclerView>(R.id.rvEvent)

        //Create a window with our parameters
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        //Set the location of the window on the screen
        popupWindow.showAsDropDown(view, 50, 50)

       /* btnClose.setOnClickListener{
            popupWindow.dismiss()
        }*/
        txtDate.text = "\t$text"

        rvEvent.layoutManager = LinearLayoutManager(context)
//        val adapter = EventBasedOnDateAdapter(context, eventList)
//        adapter.setOnItemClickListener(object : EventBasedOnDateAdapter.ClickListener {
//            override fun onItemClick(position: Int) {
//
//            }
//
//        })
//        rvEvent.adapter = adapter

       /* popupView.setOnTouchListener { v, event -> //Close the window when clicked
            popupWindow.dismiss()
            true
        }*/

    }
}