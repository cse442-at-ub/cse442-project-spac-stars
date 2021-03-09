package com.example.myapplication

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import android.widget.ListView

import com.example.myapplication.constants.worksheetsStartingRow
import com.example.myapplication.constants.apikey
import com.example.myapplication.constants.sheetID
import com.example.myapplication.TickerListAdapter

import java.net.URL
import org.json.*
import kotlin.concurrent.*



class CategoryList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list2)

        val extras = intent.extras
        var SPACtype: String = ""
        if (extras != null) {
            SPACtype = extras.getString("key").toString()
        }

        thread(start=true) {
            val results = getList(SPACtype)
            val tickers = results.first
            val names = results.second
            println("thread run")
            this@CategoryList.runOnUiThread(Runnable {
                val listAdapter: TickerListAdapter = TickerListAdapter(this, tickers, names)
                var viewList: ListView = findViewById(R.id.categoryList)
                viewList.adapter = listAdapter
            })


        }



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menubar, menu)
        return true
    }

    fun getList(SPACtype: String): /*MutableList<Array<String>>*/ /*MutableList<String> */ Pair<Array<String>, Array<String>>{
        var response: String = ""
        val startingRow: String? = worksheetsStartingRow[SPACtype]
        val jsonResult =
            URL("https://sheets.googleapis.com/v4/spreadsheets/$sheetID/values/$SPACtype!$startingRow:C?key=$apikey")
                .readText()

        val information: JSONObject = JSONObject(jsonResult)
        val rawSpacList = information.getJSONArray("values")
        val len = rawSpacList.length() - 1

        var tickers: MutableList<String> = mutableListOf()
        var names: MutableList<String> = mutableListOf()

        for(i in 0..len){
            if(rawSpacList.getJSONArray(i).getString(0) != "N/A" ||
                rawSpacList.getJSONArray(i).getString(0) != ""
            ){
                tickers.add(rawSpacList.getJSONArray(i).getString(0))
                names.add(rawSpacList.getJSONArray(i).getString(1))
            }
        }

        return Pair(tickers.toTypedArray(),names.toTypedArray())
    }




}