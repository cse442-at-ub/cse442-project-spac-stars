package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        //Set the xml page to use as display page. R.layout is resources folder (res) / layout folder, as in app/res/layout/activity_main.xml
        setContentView(R.layout.activity_main)

    }

}