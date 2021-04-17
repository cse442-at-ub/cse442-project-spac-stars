package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.storageHandlers.DBHandlerSavedList


class MainActivity : AppCompatActivity(){

    private var saved = mutableListOf<Array<String>>()
    private var listAdapter = SavedListAdapter(this, saved, this)

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        //Set the xml page to use as display page. R.layout is resources folder (res) / layout folder, as in app/res/layout/activity_main.xml
        setContentView(R.layout.activity_main)
        val db = DBHandlerSavedList(this)
        saved.addAll(db.getAllSavedSPAC())
        db.closeDB()
        //https://suragch.medium.com/updating-data-in-an-android-recyclerview-842e56adbfd8
//        listAdapter = SavedListAdapter(saved)
        listAdapter.notifyDataSetChanged()


        val viewList: RecyclerView = findViewById(R.id.recyclerViewSaved)
        viewList.adapter = listAdapter
        viewList.layoutManager = LinearLayoutManager(this)
        viewList.setHasFixedSize(true)

        //Set up search interface
        val searchtext = findViewById<TextView>(R.id.mainsearchtext)
        val search = findViewById<Button>(R.id.mainsearchbutton)
        search.setOnClickListener {search(searchtext, viewList)}
        //If there is no saved lists, tell user to add some
        if(saved.isEmpty()){
            searchtext.hint = "Save SPACs from the listings!"
            searchtext.isEnabled = false
            search.visibility = View.GONE
        }
    }


    //source: https://developer.android.com/guide/topics/ui/menus
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
                this.recreate()
            }


          //R.id.showAll -> showListSelection = "Show All"
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

    fun removeButtonHandler(view: View) {
        val parent = view.parent.parent as View
        val ticker = parent.findViewById<TextView>(R.id.ticker).text
//        val name = parent.findViewById<TextView>(R.id.name)
        println(saved.indexOfFirst { it[0] == ticker })
        val db = DBHandlerSavedList(this)
        db.removeSPAC(ticker.toString())
        saved.removeAt(saved.indexOfFirst { it[0] == ticker })
        println(saved.size)
        listAdapter.notifyDataSetChanged()
    }

    //Search the saved list
    fun search(text: TextView, viewlist: RecyclerView){
        val query = text.text.toString().toUpperCase()
        val searchresults: MutableList<Array<String>> = mutableListOf()
        if(query.isEmpty()){
            listAdapter = SavedListAdapter(this, saved, this)
            viewlist.adapter = listAdapter
            listAdapter.notifyDataSetChanged()
        }
        else {
            for (i in saved) {
                for (j in i) {
                    if (j.toUpperCase().contains(query)) {
                        searchresults.add(0, i)
                        break
                    }
                }
            }
            listAdapter = SavedListAdapter(this, searchresults, this)
            viewlist.adapter = listAdapter
            listAdapter.notifyDataSetChanged()
        }
    }

}