package com.example.myapplication.data

import com.example.myapplication.model.SPACLivePrices
import org.json.JSONObject
import java.net.URL


class DataSource {

    fun loadSPACs(): List<SPACLivePrices> {
        val finalList: MutableList<SPACLivePrices> = mutableListOf()
        val jsonArray = URL("https://sheets.googleapis.com/v4/spreadsheets/1D61Q4V_LwTXVCOedHkg-IROuZKTiJ25wg_qL75XvWlc/values/SPAC Live Prices!A2:C265?key=AIzaSyCZP2fBW638Gip01kDHMbHLaM84hWwU7uo").readText()
        val info = JSONObject(jsonArray).getJSONArray("values")

        val len = info.length() - 1

//        finalList.add(0, SPACLivePrices("TICKER", "LIVE PRICE", "COMPANY NAME"))

        for(i in 0..len) {
            finalList.add(i, SPACLivePrices(info.getJSONArray(i).getString(0),
                    info.getJSONArray(i).getString(1),
                    info.getJSONArray(i).getString(2)))
        }

        return finalList
    }
}