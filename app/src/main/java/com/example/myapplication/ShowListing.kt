package com.example.myapplication

import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

import com.example.myapplication.constants.worksheetsStartingRow
import com.example.myapplication.constants.apikey
import com.example.myapplication.constants.sheetID
import com.example.myapplication.constants.sortTableRows
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.w3c.dom.Text
import java.net.URL
import kotlin.concurrent.thread

class ShowListing : AppCompatActivity() {

    private var tableRows: MutableList<TableRow> = mutableListOf()
    private var loadeddata = 0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_listing)

        //Initialize values for updating the SPAC table
        val table = findViewById<TableLayout>(R.id.listingtable)
        val context = applicationContext
        val searchtext = findViewById<TextView>(R.id.searchinput)
        searchtext.hint = "Loading SPACs..."

        //Update the titlebar from "SPAC Stars" to "Show Listing"
        val titlebar: ActionBar? = supportActionBar
        if (titlebar != null) {
            titlebar.title = "Show Listings"
            titlebar.subtitle = "Show All"
        }

        val spinner: Spinner = findViewById(R.id.sortDropdown)
        val items: Array<String> =
                arrayOf("Select Sorting Order",
                        "Ticker (A-Z)",
                        "Ticker (Z-A)",
                        "Name (A-Z)",
                        "Name (Z-A)"
                )

        val parameterMap: Array<Triple<Int, String, Boolean>> = arrayOf(
                Triple(0, "String", false),
                Triple(0, "String", true),
                Triple(1, "String", false),
                Triple(1, "String", true)
        )

        val dropdownAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = dropdownAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                println(parent.getItemAtPosition(position).toString())
                if(position != 0){
                    val index = items.indexOfFirst { it == parent.getItemAtPosition(position).toString() } - 1
                    alterTable(parameterMap[index], table)
                }
//                viewList.layoutManager = LinearLayoutManager(this)
//                viewList.setHasFixedSize(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        //Create the first row for the table that shows "TICKER  SPAC NAME   CATEGORY"
        addFirstRow(context, table)

        //Get the data for each category, starts on different threads
        getdata("Pre+LOI", table)
        getdata("Definitive+Agreement", table)
        getdata("Option+Chads", table)
        getdata("Pre+Unit+Split", table)
        getdata("Pre+IPO", table)

        val search = findViewById<Button>(R.id.searchbutton)
        //Once all the data is loaded, enable the search button
        load(search, table, searchtext)
    }

    //Wait until data is retrieved to allow search
    fun load(button: Button, table: TableLayout, searchtext: TextView){
        thread(start = true) {
            while(loadeddata < 5){"wait for data to load before search becomes available"}
            searchtext.hint = "Search..."
            button.setOnClickListener { searchTable(table, searchtext) }
        }
    }

    fun getdata(category: String, table: TableLayout){
        val displaycategory = category.replace("+", " ")
        println("Getting Data: $displaycategory")
        thread(start = true){
            val datalist = getList(category)
            runOnUiThread { addtablerows(table, displaycategory, datalist) }
            loadeddata += 1
            println("Data Acquired: $displaycategory")
        }
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
            if (spacdata[0].toString() != "" && spacdata[0].toString() != "N/A") {
                //Add ticker, name, and category all to table, set a color for that text
                Tickerrow.text = spacdata[0].toString() + "\t"
                //Tickerrow.setTextColor(Color.parseColor(darkgraycolor))
                tablerow.addView(Tickerrow, 0)

                Namerow.maxWidth = 448
                Namerow.text = spacdata[1].toString() + "\n"
                //Namerow.setTextColor(Color.parseColor(darkgraycolor))
                tablerow.addView(Namerow, 1)

                Categoryrow.text = "\t" + category
                //Categoryrow.setTextColor(Color.parseColor(darkgraycolor))
                tablerow.addView(Categoryrow, 2)
                //Sets the tag to be used when searching later, adds \t do separate ticker and name
                tablerow.tag = spacdata[0].toString() + "\t" + spacdata[1].toString()
            }

            if(tablerow.getChildAt(0) != null){
                //Set the row to display data on click
                onclicksetter(tablerow, category, spacdata)
                //Add the row the table
                table.addView(tablerow)
                tableRows.add(tablerow)
            }
        }
    }

    fun addFirstRow(context: android.content.Context, table: TableLayout){
//        val context = applicationContext
        val firstrow = TableRow(context)
        val Tickerrow = TextView(context)
        val Namerow = TextView(context)
        val Categoryrow = TextView(context)
        val Blackcolor = "#000000"
        Tickerrow.setTypeface(null, Typeface.BOLD_ITALIC)
        Namerow.setTypeface(null, Typeface.BOLD_ITALIC)
        Categoryrow.setTypeface(null, Typeface.BOLD_ITALIC)
        Tickerrow.text = "TICKER\t"
        //Tickerrow.setTextColor(Color.parseColor(Blackcolor))
        //Namerow.setTextColor(Color.parseColor(Blackcolor))
        //Categoryrow.setTextColor(Color.parseColor(Blackcolor))
        Namerow.text = "SPAC NAME\t"
        Categoryrow.text = "CATEGORY\t"
        firstrow.addView(Tickerrow, 0)
        firstrow.addView(Namerow, 1)
        firstrow.addView(Categoryrow, 2)
        table.addView(firstrow)
    }

    //rebuild table after sorting
    fun alterTable(selection: Triple<Int, String, Boolean>, table: TableLayout){
        tableRows = sortTableRows(tableRows, selection.first, selection.third)
        table.removeAllViews()
        addFirstRow(applicationContext, table)
        for(i in tableRows){
            table.addView(i)
        }

    }

    //Make the table entry show more data when clicked, depends on category name
    fun onclicksetter(tablerow: TableRow, category: String, spacdata: JSONArray){
        val db = DBHandlerSavedList(applicationContext)
        //Load the user preferences
        val preference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        var alertstring = "Ticker: " + spacdata[0].toString() + "\n\nCompany Name: " + spacdata[1].toString()
        when(category){

            "Pre LOI" -> {
                //Set a boolean for not duplicating the data
                tablerow.setOnClickListener {
                    //Create a builder for the alert window
                    val alert: AlertDialog.Builder = AlertDialog.Builder(this)
                    //Display data based on preferences chosen
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
                    //Reset the string so that it doesn't display duplicate data when clicked again
                    alertstring = "Ticker: " + spacdata[0].toString() + "\n\nCompany Name: " + spacdata[1].toString()
                    /*  This sets an "OK" button in the dialog window that
                    doesn't currently do anything except close the window
                    and print a message to the console  */
                    alert.setPositiveButton("OK"){
                        _, _ -> println("POSITIVE PRESSED, PRE LOI")
                    }
                    if(!db.getSavedSPACExists(spacdata[0].toString())){
                        alert.setNegativeButton("SAVE"){ _, _ ->
                            println("NEGATIVE PRESSED, SAVE SPAC PRE LOI")
                            db.insertNewSavedSPAC(spacdata[0].toString(), spacdata[1].toString(), category.replace(" ", "+"))
                        }
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
                    alertstring = "Ticker: " + spacdata[0].toString() + "\n\nCompany Name: " + spacdata[1].toString()
                    alert.setPositiveButton("OK"){
                        _, _ -> println("POSITIVE PRESSED, DEFINITIVE AGREEMENT")
                    }
                    if(!db.getSavedSPACExists(spacdata[0].toString())){
                        alert.setNegativeButton("SAVE"){ _, _ ->
                            println("NEGATIVE PRESSED, SAVE SPAC DEFINITIVE AGREEMENT")
                            db.insertNewSavedSPAC(spacdata[0].toString(), spacdata[1].toString(), category.replace(" ", "+"))
                        }
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
                    alertstring = "Ticker: " + spacdata[0].toString() + "\n\nCompany Name: " + spacdata[1].toString()
                    alert.setPositiveButton("OK"){
                        _, _ -> println("POSITIVE PRESSED, OPTION CHADS")
                    }
                    if(!db.getSavedSPACExists(spacdata[0].toString())){
                        alert.setNegativeButton("SAVE"){ _, _ ->
                            println("NEGATIVE PRESSED, SAVE SPAC OPTION CHADS")
                            db.insertNewSavedSPAC(spacdata[0].toString(), spacdata[1].toString(), category.replace(" ", "+"))
                        }
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
                    alertstring = "Ticker: " + spacdata[0].toString() + "\n\nCompany Name: " + spacdata[1].toString()
                    alert.setPositiveButton("OK"){
                        _, _ -> println("POSITIVE PRESSED, PRE UNIT SPLIT")
                    }
                    if(!db.getSavedSPACExists(spacdata[0].toString())){
                        alert.setNegativeButton("SAVE"){ _, _ ->
                            println("NEGATIVE PRESSED, SAVE SPAC PRE UNIT SPLIT")
                            db.insertNewSavedSPAC(spacdata[0].toString(), spacdata[1].toString(), category.replace(" ", "+"))
                        }
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
                    alertstring = "Ticker: " + spacdata[0].toString() + "\n\nCompany Name: " + spacdata[1].toString()
                    alert.setPositiveButton("OK"){
                        _, _ -> println("POSITIVE PRESSED, PRE IPO")
                    }
                    if(!db.getSavedSPACExists(spacdata[0].toString())){
                        alert.setNegativeButton("SAVE"){ _, _ ->
                            println("NEGATIVE PRESSED, SAVE SPAC PRE IPO")
                            db.insertNewSavedSPAC(spacdata[0].toString(), spacdata[1].toString(), category.replace(" ", "+"))
                        }
                    }
                    alert.setTitle(spacdata[1].toString())
                    alert.create().show()
                }
            }
        }
    }

    //rebuild table after searching
    fun searchTable(table: TableLayout, text: TextView){
        val query = text.text.toString().toUpperCase()
        table.removeAllViews()
        addFirstRow(applicationContext, table)
        //Reset when search is empty, otherwise only display items that match the search
        if(query.isEmpty()){
            for(i in tableRows){
                    table.addView(i)
            }
        }
        else{
        for(i in tableRows){
            if(i.tag.toString().toUpperCase().contains(query)) {
                table.addView(i)
            }
        }}

    }

}