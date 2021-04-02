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


    //parameters:
    // list = of arrays, each array is in the format of [ticker, name, information] (all Strings)
    // index = which index of the individual array to sort by (example: index 0 would sort by ticker
    // type = String or Int, determines if you want to sort the information by alphabetical or numeric order (usually only the information is sorted numerically)
    // isDescending = if you want the list sorted in descending order

    //returns a list of arrays, each array is in the format of [ticker, name, information] (all Strings)
    fun sortingOrder(list: MutableList<Array<String>>, index: Int, type: String, isDescending: Boolean): MutableList<Array<String>>{
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
        return sorted
    }
}