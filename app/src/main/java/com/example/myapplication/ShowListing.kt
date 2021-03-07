package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import org.json.JSONObject

class ShowListing : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_listing)

        //Note: I would prefer if this code wasn't in onCreate, but to update views the easiest way is to put it onCreate. Otherwise A. Extra work will need to be done or B. Updating views wont work
        val table = findViewById<TableLayout>(R.id.listingtable)
        val context = applicationContext
        //Https Request from Fuel library, example can be seen on Fuel github
        val sheet = "https://sheets.googleapis.com/v4/spreadsheets/1dZOPswJcmPQ5OqTw7LNeTZOXklnmD-n7fyohbkRSsFE/values/Pre+LOI!A4:D174?key=AIzaSyCZP2fBW638Gip01kDHMbHLaM84hWwU7uo"
            .httpGet()
            .responseString{ request, response, result ->
                when (result){
                    //If the API returns an error, print out the exception
                    is Result.Failure -> {
                        val ex = result.getException()
                        println(ex)
                    }
                    //When the API successfully returns data, do stuff
                    is Result.Success -> {
                        //Get JSON Data
                        val data = result.get()
                        //Convert said data into JSON
                        val fullJson = JSONObject(data)
                        val values = fullJson.getJSONArray("values")
                        //Shows the first 170 SPACs
                        for(i in 0..170){
                            //For each item of data retrieved, convert it into android text, convert that into a row for a table, and then add that row to the table
                            val spacdata = values.getJSONArray(i)
                            val tablerow = TableRow(context)
                            val rowcontent1 = TextView(context)
                            rowcontent1.text = spacdata[0].toString()
                            tablerow.addView(rowcontent1, 0)
                            val rowcontent2 = TextView(context)
                            rowcontent2.maxWidth = 384
                            rowcontent2.text = spacdata[1].toString()
                            tablerow.addView(rowcontent2, 1)
                            val rowcontent3 = TextView(context)
                            rowcontent3.text = spacdata[2].toString()
                            tablerow.addView(rowcontent3, 2)
                            //the SPAC ARBG does not have a listed Trust Value, which causes the table to stop working, so the if statement allows it to advance past that SPAC
                            if (spacdata.length() > 3){
                                val rowcontent4 = TextView(context)
                                rowcontent4.text = spacdata[3].toString()
                                tablerow.addView(rowcontent4, 3)
                            }
                            table.addView(tablerow)
                        }
                    }
                }
            }
        sheet.join()

    }

}