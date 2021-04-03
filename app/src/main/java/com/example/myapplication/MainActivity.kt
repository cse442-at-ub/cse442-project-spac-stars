package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(){

    private var saved = mutableListOf<Array<String>>()
    private var listAdapter = SavedListAdapter(this, saved, this)

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        //Set the xml page to use as display page. R.layout is resources folder (res) / layout folder, as in app/res/layout/activity_main.xml
        setContentView(R.layout.activity_main)
        val db = DBHandlerSavedList(this)
//        db.insertNewSavedSPAC("TEST", "test")
        saved.addAll(db.getAllSavedSPAC())
        //https://suragch.medium.com/updating-data-in-an-android-recyclerview-842e56adbfd8
//        listAdapter = SavedListAdapter(saved)
        listAdapter.notifyDataSetChanged()

//        //THIS IS A TEST
//        val test = TestHandler(this)

//        val db = DBHandlerSavedList(this)
//        db.insertNewSavedSPAC("TEST", "test")

        val viewList: RecyclerView = findViewById(R.id.recyclerViewSaved)
        viewList.adapter = listAdapter
        viewList.layoutManager = LinearLayoutManager(this)
        viewList.setHasFixedSize(true)
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

            R.id.spacliveprices -> {
                val intent = Intent(this, SPACLivePricesMain::class.java)
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

          //R.id.showAll -> showListSelection = "Show All"
            R.id.showAll -> {
                val intent = Intent(this, ShowListing::class.java)
                startActivity(intent)
            }
            R.id.preLOI -> showListSelection = "Pre+LOI"
            R.id.defAgree -> showListSelection = "Definitive+Agreement"
            R.id.optionChads -> showListSelection = "Option+Chads"
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
        //LinearLayout vwParentRow = (LinearLayout)v.getParent();
        //
        //        TextView child = (TextView)vwParentRow.getChildAt(0);
        //        Button btnChild = (Button)vwParentRow.getChildAt(1);
        //        btnChild.setText(child.getText());
        //        btnChild.setText("I've been clicked!");
        //
        //        int c = Color.CYAN;
        //
        //        vwParentRow.setBackgroundColor(c);
        //        vwParentRow.refreshDrawableState();
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

}