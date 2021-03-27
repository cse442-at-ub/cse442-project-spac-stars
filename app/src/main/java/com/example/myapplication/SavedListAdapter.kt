package com.example.myapplication

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R


class SavedListAdapter(private val listing: MutableList<Array<String>>) : RecyclerView.Adapter<SavedListAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedListAdapter.ListViewHolder {
        val item: View = LayoutInflater.from(parent.context).inflate(R.layout.saved_list_items, parent, false)
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
    }

    class ListViewHolder(item: View): RecyclerView.ViewHolder(item){
        val tickerView: TextView = item.findViewById(R.id.ticker)
        val nameView: TextView = item.findViewById(R.id.name)
    }
}