package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.constants.apikey
import com.example.myapplication.constants.categoryInfoColumn
import com.example.myapplication.constants.categoryInfoLabel
import com.example.myapplication.constants.sheetID
import com.example.myapplication.constants.sortingOrder
import com.example.myapplication.constants.worksheetsStartingRow
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread



class CategoryList : AppCompatActivity() {

    private var results: MutableList<Array<String>> = mutableListOf()
    private var tickerMap: MutableMap<String, JSONArray> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list2)

        val searchtext = findViewById<TextView>(R.id.categorysearchinput)
        val search = findViewById<Button>(R.id.categorysearch)
        searchtext.hint = "Loading SPACs..."

        val extras = intent.extras
        val context = this
        var SPACtype: String = ""
        if (extras != null) {
            SPACtype = extras.getString("key").toString()
        }

        val spinner:Spinner = findViewById(R.id.sortDropdown)
        val items: Array<String> =
                arrayOf("Select Sorting Order",
                        "Ticker (A-Z)",
                        "Ticker (Z-A)",
                        "Name (A-Z)",
                        "Name (Z-A)",
                        categoryInfoLabel[SPACtype] + "(ascending)",
                        categoryInfoLabel[SPACtype] + "(descending)"
                )

        val parameterMap: Array<Triple<Int, String, Boolean>> = arrayOf(
                Triple(0, "String", false),
                Triple(0, "String", true),
                Triple(1, "String", false),
                Triple(1, "String", true),
                Triple(2, "Int", false),
                Triple(2, "Int", true)
        )

        val dropdownAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = dropdownAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                println(parent.getItemAtPosition(position).toString())
                if(position != 0){
                    val index = items.indexOfFirst { it == parent.getItemAtPosition(position).toString() } - 1
                    val newOrder = sortingOrder(results, parameterMap[index].first, parameterMap[index].second, parameterMap[index].third)
                    results = newOrder
                    val listAdapter = TickerListAdapter(context, results, categoryInfoLabel[SPACtype], SPACtype, tickerMap)
                    val viewList: RecyclerView = findViewById(R.id.recyclerView)
                    viewList.adapter = listAdapter
                }
//                viewList.layoutManager = LinearLayoutManager(this)
//                viewList.setHasFixedSize(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        thread(start=true) {
            results = getList(SPACtype)
//            println(results.joinToString())
            this@CategoryList.runOnUiThread(Runnable {
                val listAdapter = TickerListAdapter(context, results, categoryInfoLabel[SPACtype], SPACtype, tickerMap)
                val viewList: RecyclerView = findViewById(R.id.recyclerView)
                viewList.adapter = listAdapter
                viewList.layoutManager = LinearLayoutManager(this)
                viewList.setHasFixedSize(true)
            })
            searchtext.hint = "Search..."
            search.setOnClickListener { searchspacs(searchtext, SPACtype) }

        }






    }

    private fun getList(SPACtype: String): MutableList<Array<String>> {
        val startingRow: String? = worksheetsStartingRow[SPACtype]
        val extraIndex: Int = categoryInfoColumn[SPACtype] as Int
//        println("https://sheets.googleapis.com/v4/spreadsheets/$sheetID/values/$SPACtype!$startingRow:$extrasColumn?key=$apikey")
        val jsonResult =
            URL("https://sheets.googleapis.com/v4/spreadsheets/$sheetID/values/$SPACtype!$startingRow:AF?key=$apikey")
                .readText()

        val information: JSONObject = JSONObject(jsonResult)
        val rawSpacList = information.getJSONArray("values")
        val len = rawSpacList.length() - 1


        val finalList: MutableList<Array<String>> = mutableListOf()

        for(i in 0..len){
//            val extraIndex = rawSpacList.getJSONArray(i).length() - 1
            if(rawSpacList.getJSONArray(i).getString(0) != "N/A" &&
                rawSpacList.getJSONArray(i).getString(0) != "" &&
                    rawSpacList.getJSONArray(i).getString(extraIndex) != "#N/A"
            ){
                val innerArray: Array<String> = arrayOf(
                    rawSpacList.getJSONArray(i).getString(0),
                    rawSpacList.getJSONArray(i).getString(1).trim(),
                    rawSpacList.getJSONArray(i).getString(extraIndex)
                )
                finalList.add(innerArray)
                tickerMap[rawSpacList.getJSONArray(i).getString(0)] = rawSpacList.getJSONArray(i)
            }
        }

//        val sorted = sortingOrder(finalList, 2, "Int", "ascending")


        return finalList
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menubar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var showListSelection: String = ""


        when (item.itemId) {
            R.id.searchsocialmedia -> {
                val intent = Intent(this, SearchSocialMedia::class.java)
                startActivity(intent)
            }
            R.id.addremove -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.showAll -> showListSelection = "Show All"
            R.id.preLOI -> showListSelection = "Pre+LOI"
            R.id.defAgree -> showListSelection = "Definitive+Agreement"
            R.id.optionChads -> showListSelection = "Option+Chads"
            R.id.preUnit -> showListSelection = "Pre+Unit+Split"
            R.id.preIPO -> showListSelection = "Pre+IPO"
//            R.id.warrants -> showListSelection = "Warrants+(Testing)"
        }

        if(worksheetsStartingRow.containsKey(showListSelection)){
            val intent = Intent(this, CategoryList::class.java)
            intent.putExtra("key", showListSelection)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    //Spac Search Function
    fun searchspacs(text: TextView, SPACtype: String){
        val searchresults: MutableList<Array<String>> = mutableListOf()
        val query = text.text.toString().toUpperCase()
        if(query.isEmpty()){
            val listAdapter = TickerListAdapter(this, results, categoryInfoLabel[SPACtype], SPACtype, tickerMap)
            val viewList: RecyclerView = findViewById(R.id.recyclerView)
            viewList.adapter = listAdapter
        }
        else {
            for (i in results) {
                for (j in i) {
                    if (j.toUpperCase().contains(query)) {
                        searchresults.add(0, i)
                        break
                    }
                }
            }
            val listAdapter = TickerListAdapter(this, searchresults, categoryInfoLabel[SPACtype], SPACtype, tickerMap)
            val viewList: RecyclerView = findViewById(R.id.recyclerView)
            viewList.adapter = listAdapter
        }
    }

}