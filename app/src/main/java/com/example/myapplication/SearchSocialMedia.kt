package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_search_social_media.*
import java.net.URLEncoder

class SearchSocialMedia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_social_media)
        btnGoogle.setOnClickListener {
            loadWebActivity("https://google.com/search?q=$encodedSearchTerm")
        }

        btnTwitter.setOnClickListener{
            loadWebActivity("https://twitter.com/search?q=$encodedSearchTerm")
        }

        btnReddit.setOnClickListener{
            loadWebActivity("https://www.reddit.com/search/?q=$encodedSearchTerm")
        }

        btnFacebook.setOnClickListener {
            loadWebActivity("https://www.facebook.com/search/top?q=$encodedSearchTerm")
        }

        etSearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                toggleButtonsState(etSearch.text.isNotEmpty())
            }
        })

        toggleButtonsState(false)

        //Update the titlebar from "SPAC Stars" to "Show Listing"
        val titlebar: ActionBar? = supportActionBar
        if (titlebar != null) {
            titlebar.title = "Search Social Media"
        }

    }

    private val encodedSearchTerm : String
        get() = URLEncoder.encode(etSearch.text.toString(),"UTF-8")

    private fun loadWebActivity(url: String) {
        val intent = Intent(this, WebSearch::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }

    private fun toggleButtonsState(enabled: Boolean){
        btnGoogle.isEnabled = enabled
        btnTwitter.isEnabled = enabled
        btnReddit.isEnabled = enabled
        btnFacebook.isEnabled = enabled
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menubar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var showListSelection: String = ""


        when (item.itemId) {

            R.id.searchsocialmedia -> {
                val intent = Intent(this, SearchSocialMedia::class.java)
                startActivity(intent)
            }

            R.id.all -> {
                val intent = Intent(this, SPACLivePricesMain::class.java)
                startActivity(intent)
            }

            R.id.top10DailyPriceChange -> {
                val intent = Intent(this, SPACTopDailyPriceChangeMain::class.java)
                startActivity(intent)
            }

            R.id.bottom10DailyPriceChange -> {
                val intent = Intent(this, SPACBottomDailyPriceChangeMain::class.java)
                startActivity(intent)
            }

            R.id.top10WeeklyPriceChange -> {
                val intent = Intent(this, SPACTopWeeklyPriceChangeMain::class.java)
                startActivity(intent)
            }

            R.id.bottom10WeeklyPriceChange -> {
                val intent = Intent(this, SPACBottomWeeklyPriceChangeMain::class.java)
                startActivity(intent)
            }

            R.id.top10MonthlyPriceChange -> {
                val intent = Intent(this, SPACTopMonthlyPriceChangeMain::class.java)
                startActivity(intent)
            }

            R.id.bottom10MonthlyPriceChange -> {
                val intent = Intent(this, SPACBottomMonthlyPriceChangeMain::class.java)
                startActivity(intent)
            }

            R.id.preferences -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.alertsetup -> {
                val intent = Intent(this, Alerts::class.java)
                startActivity(intent)
            }
            R.id.addremove -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.showAll -> {
                val intent = Intent(this, ShowListing::class.java)
                startActivity(intent)
            }
            R.id.preLOI -> showListSelection = "Pre+LOI"
            R.id.defAgree -> showListSelection = "Definitive+Agreement"
//            R.id.optionChads -> showListSelection = "Option+Chads"
            R.id.preUnit -> showListSelection = "Pre+Unit+Split"
            R.id.preIPO -> showListSelection = "Pre+IPO"
//            R.id.warrants -> showListSelection = "Warrants+(Testing)"
        }

        if(constants.worksheetsStartingRow.containsKey(showListSelection)){
            val intent = Intent(this, CategoryList::class.java)
            intent.putExtra("key", showListSelection)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}
