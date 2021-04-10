package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.storageHandlers.DBHandlerSavedList

import com.example.myapplication.constants.sheetID
import com.example.myapplication.constants.apikey
import com.example.myapplication.constants.worksheetsStartingRow
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread


class SavedListAdapter(private val context: Context, private val listing: MutableList<Array<String>>, private val activity: MainActivity) : RecyclerView.Adapter<SavedListAdapter.ListViewHolder>() {
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

        holder.itemView.setOnClickListener{
            thread (start=true) {
                val db = DBHandlerSavedList(context)
                val info = db.getSavedSPACInfo(current[0])
                val startingRow: String? = worksheetsStartingRow[info[2]]
                val category = info[2]
                val jsonResult =
                        URL("https://sheets.googleapis.com/v4/spreadsheets/$sheetID/values/$category!$startingRow:AF?key=$apikey")
                                .readText()
                val information: JSONObject = JSONObject(jsonResult)
                val rawSpacList = information.getJSONArray("values")
                val len = rawSpacList.length() - 1

                var spacdata: JSONArray = JSONArray()

                for(i in 0 .. len){
                    val currentArray = rawSpacList.getJSONArray(i)
                    if(currentArray.getString(0) == current[0]){
                        spacdata = currentArray
                        break
                    }
                }

                activity.runOnUiThread(Runnable {
                    val alert: AlertDialog.Builder = AlertDialog.Builder(context)

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
                            alert.setTitle(spacdata[1].toString())
                            alert.create().show()
                        }


                    }

                })

            }
        }
    }

    class ListViewHolder(item: View): RecyclerView.ViewHolder(item){
        val tickerView: TextView = item.findViewById(R.id.ticker)
        val nameView: TextView = item.findViewById(R.id.name)
    }
}