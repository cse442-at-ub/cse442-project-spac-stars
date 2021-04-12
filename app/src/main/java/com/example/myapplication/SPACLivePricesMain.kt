package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.ItemAdapter
import com.example.myapplication.data.DataSource
import kotlin.concurrent.thread

class SPACLivePricesMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.spac_live_main)

        updateUI()
    }

    fun updateUI() {
        thread (start=true) {
            // Initialize data.
            val myDataset = DataSource().loadSPACs()

            this@SPACLivePricesMain.runOnUiThread(Runnable {
                val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
                recyclerView.adapter = ItemAdapter(this, myDataset)

                // Use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                recyclerView.setHasFixedSize(true)
            })
        }
    }

}