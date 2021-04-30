package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.ItemAdapter
import com.example.myapplication.constants.sortingLivePricesOrder
import com.example.myapplication.data.DataSource
import com.example.myapplication.model.SPACLivePrices
import kotlin.concurrent.thread

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

        updateUI()
    }

    fun updateUI() {
        val searchtext = findViewById<TextView>(R.id.livesearch)
        val search = findViewById<Button>(R.id.livesearchbutton)
        thread (start=true) {
            // Initialize data.
            myDataset = DataSource().loadSPACs()

            this@SPACLivePricesMain.runOnUiThread(Runnable {
                val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
                recyclerView.adapter = ItemAdapter(this, myDataset)

                // Use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                recyclerView.setHasFixedSize(true)
            })
            search.setOnClickListener { searchspacs(searchtext) }
            searchtext.hint = "Search..."
        }
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

}