package com.example.myapplication

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R


class TickerListAdapter(private val listing: MutableList<Array<String>>, private val infoLabel: String?)
    : RecyclerView.Adapter<TickerListAdapter.ListViewHolder>(){

    //source: https://www.youtube.com/watch?v=afl_i6uvvU0

//    private val context: Activity, private val title: Array<String>, private val subtitle: Array<String>, private val extra: Array<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val item: View = LayoutInflater.from(parent.context).inflate(R.layout.list_display, parent, false)
        return ListViewHolder(item)
    }

    override fun getItemCount(): Int {
        return listing.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val current = listing[position]

        var name = current[1]

        if(name.length > 15){
            name = name.slice(IntRange(0,14)) + "..."
        }

        holder.tickerView.text = current[0]
        holder.nameView.text = name
        holder.infoView.text = current[2]
        holder.labelView.text = infoLabel
    }

    class ListViewHolder(item: View): RecyclerView.ViewHolder(item){
        val tickerView: TextView = item.findViewById(R.id.ticker)
        val nameView: TextView = item.findViewById(R.id.name)
        val infoView: TextView = item.findViewById(R.id.info)
        val labelView: TextView = item.findViewById(R.id.label)
    }


    //ArrayAdapter<String>(context, R.layout.list_display, title){



    //source: https://www.javatpoint.com/kotlin-android-custom-listview
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val inflater: LayoutInflater = LayoutInflater.from(context)
//        val item = inflater.inflate(R.layout.list_display, null, true)
//
//        val ticker = item.findViewById<View>(R.id.ticker) as TextView
//        val name = item.findViewById<View>(R.id.name) as TextView
//
//        ticker.text = title[position]
//        name.text = subtitle[position]
//
//        return item
//    }
}