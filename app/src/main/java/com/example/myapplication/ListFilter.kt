package com.example.myapplication

import java.net.URL
import org.json.*

class ListFilter constructor(val SPACtype: String) {
    var response: String = ""
    val sheetID: String = "1dZOPswJcmPQ5OqTw7LNeTZOXklnmD-n7fyohbkRSsFE"
    val apikey: String = "AIzaSyCZP2fBW638Gip01kDHMbHLaM84hWwU7uo"
    val worksheetsStartingRow: Map<String, String> = mapOf(
            "Pre+LOI" to "A5",
            "Definitive+Agreement" to "A5",
            "Option+Chads" to "A3",
            "Pre+Unit+Split" to "A3",
            "Pre+IPO" to "A3",
            "Warrants+(Testing)" to "A2"
    )
//    val useGID: String? = worksheetsGID[SPACtype]
//    val csvResult = URL("https://docs.google.com/spreadsheets/d/$sheetID/export?format=csv&id=$sheetID&gid=$useGID").readText()

    val startingRow: String? = worksheetsStartingRow[SPACtype]
    val jsonResult =
            URL("https://sheets.googleapis.com/v4/spreadsheets/$sheetID/values/$SPACtype!$startingRow:C?key=$apikey")
                    .readText()

    val information: JSONObject = JSONObject(jsonResult)
//    val spacList = information.getJSONArray("values").getJSONArray(0).getString(0)
    val rawSpacList = information.getJSONArray("values")
    val len = rawSpacList.length() - 1

    var finalSpacList: MutableList<Array<String>> = mutableListOf()

    init{
        for(i in 0..len){
            if(rawSpacList.getJSONArray(i).getString(0) != "N/A"){
                val unit: Array<String> = arrayOf(
                        rawSpacList.getJSONArray(i).getString(0),
                        rawSpacList.getJSONArray(i).getString(1)
                )
                finalSpacList.add(unit)
            }
        }
        for(i in finalSpacList){
            println(i[0] + "," + i[1])
        }
    }
}