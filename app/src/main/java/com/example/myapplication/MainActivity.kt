package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        //Set the xml page to use as display page. R.layout is resources folder (res) / layout folder, as in app/res/layout/activity_main.xml
        setContentView(R.layout.activity_main)



    }

    //source: https://developer.android.com/guide/topics/ui/menus
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menubar, menu)
        println(R.menu.menubar)
        println("creating menu")
        return true
    }

}