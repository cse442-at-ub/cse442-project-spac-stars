package com.example.myapplication.data

import com.example.myapplication.model.SPACTopWeeklyPriceChange
import org.json.JSONObject
import java.net.URL


class DataSourceTopWeekly {

    fun loadSPACs(): List<SPACTopWeeklyPriceChange> {
        val list: MutableList<SPACTopWeeklyPriceChange> = mutableListOf()
        val jsonArray = URL("https://sheets.googleapis.com/v4/spreadsheets/1D61Q4V_LwTXVCOedHkg-IROuZKTiJ25wg_qL75XvWlc/values/Weekly % Change!A2:E265?key=AIzaSyCZP2fBW638Gip01kDHMbHLaM84hWwU7uo").readText()
        val info = JSONObject(jsonArray).getJSONArray("values")

        val len = info.length() - 1

//        finalList.add(0, SPACLivePrices("TICKER", "LIVE PRICE", "COMPANY NAME"))

        for(i in 0..len) {
            list.add(i, SPACTopWeeklyPriceChange(info.getJSONArray(i).getString(0),
                    info.getJSONArray(i).getString(1),
                    info.getJSONArray(i).getString(3).toFloatOrNull(),
                    info.getJSONArray(i).getString(4)))
        }

        val finalList: MutableList<SPACTopWeeklyPriceChange> = mutableListOf()

        for(j in 0..len) {
            if (list[j].stringResourceId3 != null){
                list[j].stringResourceId3 = "%.${2}f".format(list[j].stringResourceId3?.times(100.0)?.toFloat()).toFloatOrNull()
                finalList.add(list[j])
            }
        }

        finalList.sortByDescending { SPACTopWeeklyPriceChange -> SPACTopWeeklyPriceChange.stringResourceId3 }

        return finalList.take(10)
    }
}