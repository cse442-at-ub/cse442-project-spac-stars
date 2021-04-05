package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import org.json.JSONArray


class TickerListAdapter(private val context: Context,
                        private val listing: MutableList<Array<String>>,
                        private val infoLabel: String?,
                        private val category: String, private val infoMap: MutableMap<String,JSONArray>)
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

        holder.itemView.setOnClickListener {
            val alert: AlertDialog.Builder = AlertDialog.Builder(context)
            val spacdata = infoMap[current[0]] as JSONArray
            val db = DBHandlerSavedList(context)
            when(category){

                "Pre+LOI" -> {

                    //Set the message of the alert window that appears
                    alert.setMessage("Ticker: " + spacdata[0].toString()
                            + "\n\nCompany Name: " + spacdata[1].toString()
                            + "\n\nMarket Cap: " + spacdata[2].toString()
                            + "\n\nEstimated Trust Value: " + spacdata[3].toString()
                            + "\n\nCurrent Volume: " + spacdata[15].toString()
                            + "\n\nAverage Volume " + spacdata[16].toString()
                            + "\n\nWarrant Ticker: " + spacdata[18].toString()
                            + "\n\nTarget Focus: " + spacdata[26].toString()
                            + "\n\nUnderwriters: " + spacdata[27].toString()
                            + "\n\nIPO Date: " + spacdata[28].toString()
                            + "\n\nDeadline Date: " + spacdata[30].toString()
                    )
                    /*  This sets an "OK" button in the dialog window that
                        doesn't currently do anything except close the window
                        and print a message to the console  */
                    alert.setPositiveButton("OK"){ _, _ -> println("POSITIVE PRESSED, PRE LOI")
                    }
                    if(!db.getSavedSPACExists(current[0])){
                        alert.setNegativeButton("SAVE"){ _, _ ->
                            println("NEGATIVE PRESSED, SAVE SPAC PRE LOI")
                            db.insertNewSavedSPAC(current[0], current[1], category)
                        }
                    }

                    //Set the title for the alert window to the SPAC name
                    alert.setTitle(spacdata[1].toString())

                    //Display the window to the user
                    alert.create().show()
                }

                "Definitive+Agreement" -> {
                    alert.setMessage("Ticker: " + spacdata[0].toString()
                            + "\n\nCompany Name: " + spacdata[1].toString()
                            + "\n\nMarket Cap: " + spacdata[2].toString()
                            + "\n\nCurrent Volume: " + spacdata[14].toString()
                            + "\n\nVolume Average: " + spacdata[15].toString()
                            + "\n\nTarget: " + spacdata[17].toString()
                    )
                    alert.setPositiveButton("OK"){ _, _ -> println("POSITIVE PRESSED, DEFINITIVE AGREEMENT")
                    }
                    if(!db.getSavedSPACExists(current[0])){
                        alert.setNegativeButton("SAVE"){ _, _ ->
                            println("NEGATIVE PRESSED, SAVE SPAC DEFINITIVE AGREEMENT")
                            db.insertNewSavedSPAC(current[0], current[1], category)
                        }
                    }
                    alert.setTitle(spacdata[1].toString())
                    alert.create().show()
                }

                "Option+Chads" -> {
                    alert.setMessage("Ticker: " + spacdata[0].toString()
                            + "\n\nCompany Name: " + spacdata[1].toString()
                            + "\n\nMarket Cap: " + spacdata[2].toString()
                            + "\n\nEstimated Trust Value: " + spacdata[3].toString()
                            + "\n\nCurrent Volume: " + spacdata[15].toString()
                            + "\n\nAverage Volume " + spacdata[16].toString()
                    )
                    alert.setPositiveButton("OK"){ _, _ -> println("POSITIVE PRESSED, OPTION CHADS")
                    }
                    if(!db.getSavedSPACExists(current[0])){
                        alert.setNegativeButton("SAVE"){ _, _ ->
                            println("NEGATIVE PRESSED, SAVE SPAC OPTION CHADS")
                            db.insertNewSavedSPAC(current[0], current[1], category)
                        }
                    }
                    alert.setTitle(spacdata[1].toString())
                    alert.create().show()
                }

                "Pre+Unit+Split" -> {
                    alert.setMessage("Ticker: " + spacdata[0].toString()
                            + "\n\nCompany Name: " + spacdata[1].toString()
                            + "\n\nUnit & Warrant Details: " + spacdata[5].toString()
                            + "\n\nEstimated Trust Size: " + spacdata[6].toString()
                            + "\n\nProminent Leadership / Directors / Advisors: " + spacdata[8].toString()
                            + "\n\nTarget Focus: " + spacdata[9].toString()
                    )
                    alert.setPositiveButton("OK"){ _, _ -> println("POSITIVE PRESSED, PRE UNIT SPLIT")
                    }
                    if(!db.getSavedSPACExists(current[0])){
                        alert.setNegativeButton("SAVE"){ _, _ ->
                            println("NEGATIVE PRESSED, SAVE SPAC PRE UNIT SPLIT")
                            db.insertNewSavedSPAC(current[0], current[1], category)
                        }
                    }
                    alert.setTitle(spacdata[1].toString())
                    alert.create().show()
                }

                "Pre+IPO" -> {
                    alert.setMessage("Ticker: " + spacdata[0].toString()
                            + "\n\nCompany Name: " + spacdata[1].toString()
                            + "\n\nEstimated Trust Value: " + spacdata[2].toString()
                            + "\n\nManagement Team: " + spacdata[3].toString()
                            + "\n\nTarget Focus: " + spacdata[4].toString()
                    )
                    alert.setPositiveButton("OK"){ _, _ -> println("POSITIVE PRESSED, PRE IPO")
                    }
                    if(!db.getSavedSPACExists(current[0])){
                        alert.setNegativeButton("SAVE"){ _, _ ->
                            println("NEGATIVE PRESSED, SAVE SPAC PRE IPO")
                            db.insertNewSavedSPAC(current[0], current[1], category)
                        }
                    }
                    alert.setTitle(spacdata[1].toString())
                    alert.create().show()
                }


            }
        }
    }

//    fun onClick(item: View){
//
//    }

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