package com.example.myapplication


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

import kotlin.concurrent.*
import com.example.myapplication.ListFilter
import com.example.myapplication.constants


class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        //Set the xml page to use as display page. R.layout is resources folder (res) / layout folder, as in app/res/layout/activity_main.xml
        setContentView(R.layout.activity_main)

//        thread(start=true) {
//            val testList: ListFilter = ListFilter("Pre+IPO")
//        }


    }


    //source: https://developer.android.com/guide/topics/ui/menus
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menubar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var selection: String = ""

        when(item.itemId){
            R.id.showAll -> selection = "Show All"
            R.id.preLOI -> selection = "Pre+LOI"
            R.id.defAgree -> selection = "Definitive+Agreement"
            R.id.optionChads -> selection = "Option+Chads"
            R.id.preUnit -> selection = "Pre+Unit+Split"
            R.id.preIPO -> selection = "Pre+IPO"
            R.id.warrants -> selection = "Warrants+(Testing)"
        }

        if(constants.worksheetsStartingRow.containsKey(selection)){
            val intent = Intent(this, CategoryList::class.java)
            intent.putExtra("key", selection)
            startActivity(intent)
        }


        return super.onOptionsItemSelected(item)
    }

}