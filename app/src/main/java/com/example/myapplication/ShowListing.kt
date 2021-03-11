package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import org.json.JSONObject

import com.example.myapplication.constants.worksheetsStartingRow
import com.example.myapplication.constants.apikey
import com.example.myapplication.constants.sheetID
import org.json.JSONArray
import java.net.URL
import kotlin.concurrent.thread

class ShowListing : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_listing)

        //Note: I would prefer if this code wasn't in onCreate, but to update views the easiest way is to put it onCreate. Otherwise A. Extra work will need to be done or B. Updating views wont work
        val table = findViewById<TableLayout>(R.id.listingtable)
        val context = applicationContext
        var values: JSONArray = JSONArray()
        thread(start = true) {
            values = getList("Pre+LOI")
            println(values)
        }
        while(values.length() == 0){
            //Block until table can be populated
        }
        for (i in 0 until values.length()) {
            val spacdata = values.getJSONArray(i)
            val tablerow = TableRow(context)
            val Tickerrow = TextView(context)
            //If there is no Ticker associated, don't add it
            if(spacdata[0].toString() != ""){
            Tickerrow.text = spacdata[0].toString()
            tablerow.addView(Tickerrow, 0)
            val Namerow = TextView(context)
            Namerow.maxWidth = 384
            Namerow.text = spacdata[1].toString() + "\n"
            tablerow.addView(Namerow, 1)
            val Marketcaprow = TextView(context)
            Marketcaprow.text = spacdata[2].toString()
            tablerow.addView(Marketcaprow, 2)
            //the SPAC ARBG does not have a listed Trust Value, which causes the table to stop working, so the if statement allows it to advance past that SPAC
            if (spacdata.length() > 3) {
                val Trustvaluerow = TextView(context)
                Trustvaluerow.text = spacdata[3].toString()
                tablerow.addView(Trustvaluerow, 3)
            }}
            table.addView(tablerow)
        }
    }

    fun getList(SPACtype: String): JSONArray{
        val startingRow: String? = worksheetsStartingRow[SPACtype]
        val jsondata = JSONObject(URL("https://sheets.googleapis.com/v4/spreadsheets/$sheetID/values/$SPACtype!$startingRow:D?key=$apikey").readText())
        val SPAClist = jsondata.getJSONArray("values")
        return SPAClist
    }

}