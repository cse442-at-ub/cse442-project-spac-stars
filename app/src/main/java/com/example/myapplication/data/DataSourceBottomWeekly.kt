package com.example.myapplication.data

import com.example.myapplication.model.SPACBottomWeeklyPriceChange
import org.json.JSONObject
import java.net.URL


class DataSourceBottomWeekly {

    fun loadSPACs(): List<SPACBottomWeeklyPriceChange> {
        val list: MutableList<SPACBottomWeeklyPriceChange> = mutableListOf()
        val jsonArray = URL("https://sheets.googleapis.com/v4/spreadsheets/1D61Q4V_LwTXVCOedHkg-IROuZKTiJ25wg_qL75XvWlc/values/Weekly % Change!A2:I265?key=AIzaSyCZP2fBW638Gip01kDHMbHLaM84hWwU7uo").readText()
        val info = JSONObject(jsonArray).getJSONArray("values")

        val len = info.length() - 1

//        finalList.add(0, SPACLivePrices("TICKER", "LIVE PRICE", "COMPANY NAME"))

        for(i in 0..len) {
            val currentSPAC = info.getJSONArray(i)
            var company_name = currentSPAC.getString(4)
            if(company_name.length > 15){
                company_name = company_name.slice(IntRange(0,14)) + "..."
            }
            list.add(i, SPACBottomWeeklyPriceChange(currentSPAC.getString(0),
                    currentSPAC.getString(1),
                    currentSPAC.getString(3).toFloatOrNull(),
                    company_name,currentSPAC.getString(5),
                    currentSPAC.getString(6),
                    currentSPAC.getString(7),
                    currentSPAC.getString(8),
                    currentSPAC.getString(4)))
        }

        val finalList: MutableList<SPACBottomWeeklyPriceChange> = mutableListOf()

        for(j in 0..len) {
            if (list[j].stringResourceId3 != null){
                list[j].stringResourceId3 = "%.${2}f".format(list[j].stringResourceId3?.times(100.0)?.toFloat()).toFloatOrNull()
                finalList.add(list[j])
            }
        }

        finalList.sortBy { SPACBottomWeeklyPriceChange -> SPACBottomWeeklyPriceChange.stringResourceId3 }

        return finalList.take(10)
    }
}