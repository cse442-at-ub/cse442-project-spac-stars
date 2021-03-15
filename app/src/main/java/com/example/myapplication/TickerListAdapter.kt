
package com.example.myapplication

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.zip.Inflater

class TickerListAdapter(private val context: Activity, private val title: Array<String>, private val subtitle: Array<String> )
    : ArrayAdapter<String>(context, R.layout.list_display, title){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val item = inflater.inflate(R.layout.list_display, null, true)

        val ticker = item.findViewById<View>(R.id.ticker) as TextView
        val name = item.findViewById<View>(R.id.name) as TextView

        ticker.text = title[position]
        name.text = subtitle[position]

        return item
    }
}