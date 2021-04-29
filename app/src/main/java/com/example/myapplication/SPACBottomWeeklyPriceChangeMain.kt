package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.ItemAdapterBottomWeeklyPriceChange
import com.example.myapplication.data.DataSourceBottomWeekly
import kotlin.concurrent.thread

class SPACBottomWeeklyPriceChangeMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.spac_top_bottom)

        //Update the titlebar from "SPAC Stars" to "Live Prices"
        val titlebar: ActionBar? = supportActionBar
        if (titlebar != null) {
            titlebar.title = "Live Prices"
            titlebar.subtitle = "Bottom 10 Weekly Changes"
        }

        updateUI()
    }

    fun updateUI() {
        thread (start=true) {
            // Initialize data.
            val myDataset = DataSourceBottomWeekly().loadSPACs()

            this@SPACBottomWeeklyPriceChangeMain.runOnUiThread(Runnable {
                val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
                recyclerView.adapter = ItemAdapterBottomWeeklyPriceChange(this, myDataset)

                // Use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                recyclerView.setHasFixedSize(true)
            })
        }
    }

    fun refreshButtonHandler(view: View){
        this.recreate()
    }

}