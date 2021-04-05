package com.example.myapplication

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
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

        //Initialize values for updating the SPAC table
        val table = findViewById<TableLayout>(R.id.listingtable)
        val context = applicationContext
        var PreLOIvalues: JSONArray = JSONArray()
        var DefinitiveAgreementvalues: JSONArray = JSONArray()
        var OptionChadsvalues: JSONArray = JSONArray()
        var PreUnitSplittvalues: JSONArray = JSONArray()
        var PreIPOvalues: JSONArray = JSONArray()
        var Warrantsvalues: JSONArray = JSONArray()

        //Get the data from the sheet
        thread(start = true) {
            PreLOIvalues = getList("Pre+LOI")
            DefinitiveAgreementvalues = getList("Definitive+Agreement")
            OptionChadsvalues = getList("Option+Chads")
            PreUnitSplittvalues = getList("Pre+Unit+Split")
            PreIPOvalues = getList("Pre+IPO")
            Warrantsvalues = getList("Warrants+(Testing)")
        }

        //Update the titlebar from "SPAC Stars" to "Show Listing"
        val titlebar: ActionBar? = supportActionBar
        if (titlebar != null) {
            titlebar.title = "Show Listings: Show All"
        }

        //Create the first row for the table that shows "TICKER  SPAC NAME   CATEGORY"
        val firstrow = TableRow(context)
        val Tickerrow = TextView(context)
        val Namerow = TextView(context)
        val Categoryrow = TextView(context)
        val Blackcolor = "#000000"
        Tickerrow.setTypeface(null, Typeface.BOLD_ITALIC)
        Namerow.setTypeface(null, Typeface.BOLD_ITALIC)
        Categoryrow.setTypeface(null, Typeface.BOLD_ITALIC)
        Tickerrow.text = "TICKER\t"
        Tickerrow.setTextColor(Color.parseColor(Blackcolor))
        Namerow.setTextColor(Color.parseColor(Blackcolor))
        Categoryrow.setTextColor(Color.parseColor(Blackcolor))
        Namerow.text = "SPAC NAME\t"
        Categoryrow.text = "CATEGORY\t"
        firstrow.addView(Tickerrow, 0)
        firstrow.addView(Namerow, 1)
        firstrow.addView(Categoryrow, 2)
        table.addView(firstrow)

        //Add all the values to the table for Pre LOI
        while(PreLOIvalues.length() == 0){
            //Block until table can be populated
        }
        addtablerows(table, "Pre LOI", PreLOIvalues)

        //Add all the values for Definitive Agreement
        while(DefinitiveAgreementvalues.length() == 0){
            //Block until table can be populated
        }
        addtablerows(table, "Definitive Agreement", DefinitiveAgreementvalues)

        //Add all the values for Option Chads
        while(OptionChadsvalues.length() == 0){
            //Block until table can be populated
        }
        addtablerows(table, "Option Chads", OptionChadsvalues)

        //Add all the values for Pre Unit Split
        while(PreUnitSplittvalues.length() == 0){
            //Block until table can be populated
        }
        addtablerows(table, "Pre Unit Split", PreUnitSplittvalues)

        //Add all the values for Pre IPO
        while(PreIPOvalues.length() == 0){
            //Block until table can be populated
        }
        addtablerows(table, "Pre IPO", PreIPOvalues)

        //Add all the values for Warrants (Testing)
        while(Warrantsvalues.length() == 0){
            //Block until table can be populated
        }
//        addtablerows(table, "Warrants", Warrantsvalues)

    }

    //Function for getting data from URl
    fun getList(SPACtype: String): JSONArray{
        val startingRow: String? = worksheetsStartingRow[SPACtype]
        val jsondata = JSONObject(URL("https://sheets.googleapis.com/v4/spreadsheets/$sheetID/values/$SPACtype!$startingRow:AF?key=$apikey").readText())
        val SPAClist = jsondata.getJSONArray("values")
        return SPAClist
    }

    //Function for adding data entries to the table
    fun addtablerows(table: TableLayout, category: String, data: JSONArray) {
        val context = applicationContext
        for (i in 0 until data.length()) {
            val spacdata = data.getJSONArray(i)
            val tablerow = TableRow(context)
            val Tickerrow = TextView(context)
            val Namerow = TextView(context)
            val Categoryrow = TextView(context)
            val darkgraycolor = "#333333"
            //If there is no Ticker associated, don't add it
            if (spacdata[0].toString() != "") {
                //Add ticker, name, and category all to table, set a color for that text
                Tickerrow.text = spacdata[0].toString() + "\t"
                Tickerrow.setTextColor(Color.parseColor(darkgraycolor))
                tablerow.addView(Tickerrow, 0)

                Namerow.maxWidth = 448
                Namerow.text = spacdata[1].toString() + "\n"
                Namerow.setTextColor(Color.parseColor(darkgraycolor))
                tablerow.addView(Namerow, 1)

                Categoryrow.text = "\t" + category
                Categoryrow.setTextColor(Color.parseColor(darkgraycolor))
                tablerow.addView(Categoryrow, 2)
            }
            //Set the row to display data on click
            onclicksetter(tablerow, category, spacdata)
            //Add the row the table
            table.addView(tablerow)
        }
    }

    //Make the table entry show more data when clicked, depends on category name
    fun onclicksetter(tablerow: TableRow, category: String, spacdata: JSONArray){
        //Load the user preferences
        val preference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        var alertstring = "Ticker: " + spacdata[0].toString() + "\n\nCompany Name: " + spacdata[1].toString()
        when(category){

            "Pre LOI" -> {
                tablerow.setOnClickListener {
                    //Create a builder for the alert window
                    val alert: AlertDialog.Builder = AlertDialog.Builder(this)
                    if(preference.getBoolean("preloi_marketcap", true)){
                        alertstring += "\n\nMarket Cap: " + spacdata[2].toString()
                    }
                    if(preference.getBoolean("preloi_esttrustvalue", true)){
                        alertstring += "\n\nEstimated Trust Value: " + spacdata[3].toString()
                    }
                    if(preference.getBoolean("preloi_currentvolume", true)){
                        alertstring += "\n\nCurrent Volume: " + spacdata[15].toString()
                    }
                    if(preference.getBoolean("preloi_averagevolume", true)){
                        alertstring += "\n\nAverage Volume " + spacdata[16].toString()
                    }
                    if(preference.getBoolean("preloi_warrantticker", true)){
                        alertstring += "\n\nWarrant Ticker: " + spacdata[18].toString()
                    }
                    if(preference.getBoolean("preloi_targetfocus", true)){
                        alertstring += "\n\nTarget Focus: " + spacdata[26].toString()
                    }
                    if(preference.getBoolean("preloi_underwriters", true)){
                        alertstring += "\n\nUnderwriters: " + spacdata[27].toString()
                    }
                    if(preference.getBoolean("preloi_ipodate", true)){
                        alertstring += "\n\nIPO Date: " + spacdata[28].toString()
                    }
                    if(preference.getBoolean("preloi_deadlinedate", true)){
                        alertstring += "\n\nDeadline Date: " + spacdata[30].toString()
                    }
                    //Set the message of the alert window that appears
                    alert.setMessage(alertstring)
                    /*  This sets an "OK" button in the dialog window that
                    doesn't currently do anything except close the window
                    and print a message to the console  */
                    alert.setPositiveButton("OK"){
                        _, _ -> println("POSITIVE PRESSED, PRE LOI")
                    }

                    //Set the title for the alert window to the SPAC name
                    alert.setTitle(spacdata[1].toString())

                    //Display the window to the user
                    alert.create().show()
                }
            }

            "Definitive Agreement" -> {
                tablerow.setOnClickListener {
                    val alert: AlertDialog.Builder = AlertDialog.Builder(this)
                    if(preference.getBoolean("definitiveagreement_marketcap", true)){
                        alertstring += "\n\nMarket Cap: " + spacdata[2].toString()
                    }
                    if(preference.getBoolean("definitiveagreement_currentvolume", true)){
                        alertstring += "\n\nCurrent Volume: " + spacdata[14].toString()
                    }
                    if(preference.getBoolean("definitiveagreement_volumeaverage", true)){
                        alertstring += "\n\nVolume Average: " + spacdata[15].toString()
                    }
                    if(preference.getBoolean("definitiveagreement_target", true)){
                        alertstring += "\n\nTarget: " + spacdata[17].toString()
                    }
                    alert.setMessage(alertstring)
                    alert.setPositiveButton("OK"){
                        _, _ -> println("POSITIVE PRESSED, DEFINITIVE AGREEMENT")
                    }
                    alert.setTitle(spacdata[1].toString())
                    alert.create().show()
                }
            }

            "Option Chads" -> {
                tablerow.setOnClickListener {
                    val alert: AlertDialog.Builder = AlertDialog.Builder(this)
                    if(preference.getBoolean("optionchads_marketcap", true)){
                        alertstring += "\n\nMarket Cap: " + spacdata[2].toString()
                    }
                    if(preference.getBoolean("optionchads_esttrustvalue", true)){
                        alertstring += "\n\nEstimated Trust Value: " + spacdata[3].toString()
                    }
                    if(preference.getBoolean("optionchads_currentvolume", true)){
                        alertstring += "\n\nCurrent Volume: " + spacdata[15].toString()
                    }
                    if(preference.getBoolean("optionchads_volumeaverage", true)){
                        alertstring += "\n\nAverage Volume " + spacdata[16].toString()
                    }
                    alert.setMessage(alertstring)
                    alert.setPositiveButton("OK"){
                        _, _ -> println("POSITIVE PRESSED, OPTION CHADS")
                    }
                    alert.setTitle(spacdata[1].toString())
                    alert.create().show()
                }
            }

            "Pre Unit Split" -> {
                tablerow.setOnClickListener {
                    val alert: AlertDialog.Builder = AlertDialog.Builder(this)
                    if(preference.getBoolean("preunit_unit", true)){
                        alertstring += "\n\nUnit & Warrant Details: " + spacdata[5].toString()
                    }
                    if(preference.getBoolean("preunit_ets", true)){
                        alertstring += "\n\nEstimated Trust Size: " + spacdata[6].toString()
                    }
                    if(preference.getBoolean("preunit_pl", true)){
                        alertstring += "\n\nProminent Leadership / Directors / Advisors: " + spacdata[8].toString()
                    }
                    if(preference.getBoolean("preunit_tf", true)){
                        alertstring += "\n\nTarget Focus: " + spacdata[9].toString()
                    }
                    alert.setMessage(alertstring)
                    alert.setPositiveButton("OK"){
                        _, _ -> println("POSITIVE PRESSED, PRE UNIT SPLIT")
                    }
                    alert.setTitle(spacdata[1].toString())
                    alert.create().show()
                }
            }

            "Pre IPO" -> {
                tablerow.setOnClickListener {
                    val alert: AlertDialog.Builder = AlertDialog.Builder(this)
                    if(preference.getBoolean("preipo_etv", true)){
                        alertstring += "\n\nEstimated Trust Value: " + spacdata[2].toString()
                    }
                    if(preference.getBoolean("preipo_managementteam", true)){
                        alertstring += "\n\nManagement Team: " + spacdata[3].toString()
                    }
                    if(preference.getBoolean("preipo_targetfocus", true)){
                        alertstring += "\n\nTarget Focus: " + spacdata[4].toString()
                    }
                    alert.setMessage(alertstring)
                    alert.setPositiveButton("OK"){
                        _, _ -> println("POSITIVE PRESSED, PRE IPO")
                    }
                    alert.setTitle(spacdata[1].toString())
                    alert.create().show()
                }
            }

            //Warrants currently disabled.
            "Warrants" -> {
                tablerow.setOnClickListener {
                    val alert: AlertDialog.Builder = AlertDialog.Builder(this)
                    alert.setMessage("Ticker: " + spacdata[0].toString()
                            + "\n\nCompany Name: " + spacdata[1].toString()
                            + "\n\nCurrent Volume: " + spacdata[12].toString()
                            + "\n\nAverage Volume: " + spacdata[13].toString()
                    )
                    alert.setPositiveButton("OK"){
                        _, _ -> println("POSITIVE PRESSED, WARRANTS")
                    }
                    alert.setTitle(spacdata[1].toString())
                    alert.create().show()
                }
            }

        }
    }

}