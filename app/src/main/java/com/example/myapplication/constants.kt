package com.example.myapplication

object constants {
    val worksheetsStartingRow: Map<String, String> = mapOf(
        "Pre+LOI" to "A5",
        "Definitive+Agreement" to "A5",
        "Option+Chads" to "A3",
        "Pre+Unit+Split" to "A3",
        "Pre+IPO" to "A3",
        "Warrants+(Testing)" to "A2"
    )
    val categoryInfoColumn: Map<String, String> = mapOf(
        "Pre+LOI" to "P",
        "Definitive+Agreement" to "O",
        "Option+Chads" to "P",
        "Pre+Unit+Split" to "G",
        "Pre+IPO" to "C",
        "Warrants+(Testing)" to "M"
    )
    val categoryInfoLabel: Map<String, String> = mapOf(
        "Pre+LOI" to "Current Volume",
        "Definitive+Agreement" to "Current Volume",
        "Option+Chads" to "Current Volume",
        "Pre+Unit+Split" to "Est. Trust Value",
        "Pre+IPO" to "Est. Trust Size",
        "Warrants+(Testing)" to "Current Volume"
    )
    val sheetID: String = "1dZOPswJcmPQ5OqTw7LNeTZOXklnmD-n7fyohbkRSsFE"
    val apikey: String = "AIzaSyCZP2fBW638Gip01kDHMbHLaM84hWwU7uo"


    fun sortingOrder(list: MutableList<Array<String>>, index: Int, type: String, isDescending: Boolean): MutableList<Array<String>>{
//        var sorted: MutableList<Array<String>> = mutableListOf()
        var sorted =
            if(type == "Int"){
                list.sortedBy { it[index].replace("$", "")
                        .replace(",","")
                        .replace(" ","")
                        .toInt() }.toMutableList()

            }else{
                list.sortedBy { it[index].toLowerCase() }.toMutableList()
            }
        if(isDescending){
            sorted = sorted.asReversed()
        }
//        for(i in sorted){
//            println(i[0] + ", " + i[1] + ", " + i[2])
//        }
        return sorted
    }
}