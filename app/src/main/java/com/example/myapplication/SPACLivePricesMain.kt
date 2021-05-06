package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.ItemAdapter
import com.example.myapplication.constants.sortingLivePricesOrder
import com.example.myapplication.data.DataSource
import com.example.myapplication.model.SPACLivePrices

class SPACLivePricesMain : AppCompatActivity() {

    var myDataset: List<SPACLivePrices> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.spac_live_main)
        val searchtext = findViewById<TextView>(R.id.livesearch)
        searchtext.hint = "Loading Live Prices..."

        //Update the titlebar from "SPAC Stars" to "Live Prices"
        val titlebar: ActionBar? = supportActionBar
        if (titlebar != null) {
            titlebar.title = "Live Prices"
            titlebar.subtitle = "All"
        }

        val spinner: Spinner = findViewById(R.id.sortDropdown)
        val items: Array<String> =
                arrayOf("Select Sorting Order",
                        "Ticker (A-Z)",
                        "Ticker (Z-A)",
                        "Name (A-Z)",
                        "Name (Z-A)",
                        "Prices (ascending)",
                        "Prices (descending)"
                )

        val parameterMap: Array<Triple<Int, String, Boolean>> = arrayOf(
                Triple(0, "String", false),
                Triple(0, "String", true),
                Triple(1, "String", false),
                Triple(1, "String", true),
                Triple(2, "Float", false),
                Triple(2, "Float", true)
        )

        val context = this
        //sorting dropdown
        val dropdownAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = dropdownAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                println(parent.getItemAtPosition(position).toString())
                if(position != 0){
                    val index = items.indexOfFirst { it == parent.getItemAtPosition(position).toString() } - 1
                    val newOrder = sortingLivePricesOrder(myDataset, parameterMap[index].first, parameterMap[index].second, parameterMap[index].third)
                    myDataset = newOrder
                    println(myDataset)
                    val listAdapter = ItemAdapter(context,myDataset)
                    val viewList: RecyclerView = findViewById(R.id.recycler_view)
                    viewList.adapter = listAdapter
                }
//                viewList.layoutManager = LinearLayoutManager(this)
//                viewList.setHasFixedSize(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        fetchLivePrices()
    }

    private fun fetchLivePrices() {
        val thread = Thread {
            // Initialize data.
            val myDataset = DataSource().loadSPACs()
            updateTextView(myDataset)
        }
        thread.start()
    }

    private fun updateTextView (data: List<SPACLivePrices>) {
        val searchtext = findViewById<TextView>(R.id.livesearch)
        val search = findViewById<Button>(R.id.livesearchbutton)
        runOnUiThread {
            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            recyclerView.adapter = ItemAdapter(this, data)

            // Use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            recyclerView.setHasFixedSize(true)
        }
        search.setOnClickListener { searchspacs(searchtext) }
        searchtext.hint = "Search..."
    }

    //Spac Search Function
    fun searchspacs(text: TextView){
        val searchresults: MutableList<SPACLivePrices> = mutableListOf()
        val query = text.text.toString().toUpperCase()
        if(query.isEmpty()){
            val listAdapter = ItemAdapter(this, myDataset)
            val viewList: RecyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            viewList.adapter = listAdapter
        }
        else {
            for (i in myDataset) {
                    if (i.FullName.toUpperCase().contains(query) || i.stringResourceId1.toUpperCase().contains(query)) {
                        searchresults.add(i)
                }
            }
            val listAdapter = ItemAdapter(this, searchresults)
            val viewList: RecyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            viewList.adapter = listAdapter
        }
    }

    fun refreshButtonHandler(view: View){
        this.recreate()
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

            R.id.all -> {
                val intent = Intent(this, SPACLivePricesMain::class.java)
                startActivity(intent)
            }

            R.id.top10DailyPriceChange -> {
                val intent = Intent(this, SPACTopDailyPriceChangeMain::class.java)
                startActivity(intent)
            }

            R.id.bottom10DailyPriceChange -> {
                val intent = Intent(this, SPACBottomDailyPriceChangeMain::class.java)
                startActivity(intent)
            }

            R.id.top10WeeklyPriceChange -> {
                val intent = Intent(this, SPACTopWeeklyPriceChangeMain::class.java)
                startActivity(intent)
            }

            R.id.bottom10WeeklyPriceChange -> {
                val intent = Intent(this, SPACBottomWeeklyPriceChangeMain::class.java)
                startActivity(intent)
            }

            R.id.top10MonthlyPriceChange -> {
                val intent = Intent(this, SPACTopMonthlyPriceChangeMain::class.java)
                startActivity(intent)
            }

            R.id.bottom10MonthlyPriceChange -> {
                val intent = Intent(this, SPACBottomMonthlyPriceChangeMain::class.java)
                startActivity(intent)
            }

            R.id.preferences -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.alertsetup -> {
                val intent = Intent(this, Alerts::class.java)
                startActivity(intent)
            }
            R.id.addremove -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.showAll -> {
                val intent = Intent(this, ShowListing::class.java)
                startActivity(intent)
            }
            R.id.preLOI -> showListSelection = "Pre+LOI"
            R.id.defAgree -> showListSelection = "Definitive+Agreement"
//            R.id.optionChads -> showListSelection = "Option+Chads"
            R.id.preUnit -> showListSelection = "Pre+Unit+Split"
            R.id.preIPO -> showListSelection = "Pre+IPO"
//            R.id.warrants -> showListSelection = "Warrants+(Testing)"
        }

        if(constants.worksheetsStartingRow.containsKey(showListSelection)){
            val intent = Intent(this, CategoryList::class.java)
            intent.putExtra("key", showListSelection)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

}