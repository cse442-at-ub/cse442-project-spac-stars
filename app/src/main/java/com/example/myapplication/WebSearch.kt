package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.webkit.WebViewClient
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_search_social_media.*
import kotlinx.android.synthetic.main.activity_web_search.*
import java.net.URLEncoder

class WebSearch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_search)
        val titlebar: ActionBar? = supportActionBar
        if (titlebar != null) {
            titlebar.title = "Search Social Media"
        }
        google.setOnClickListener {
            loadurl("https://google.com/search?q=$encodedSearchTerm")
        }

        twitter.setOnClickListener{
            loadurl("https://twitter.com/search?q=$encodedSearchTerm")
        }

        reddit.setOnClickListener{
            loadurl("https://www.reddit.com/search/?q=$encodedSearchTerm")
        }

        facebook.setOnClickListener {
            loadurl("https://www.facebook.com/search/top?q=$encodedSearchTerm")
        }

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        val url = savedInstanceState?.getString("url") ?: intent.getStringExtra("url")

        webView.loadUrl(url!!)

        websearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                toggleButtonsState(websearch.text.isNotEmpty())
            }
        })
        toggleButtonsState(false)
    }

    private val encodedSearchTerm : String
        get() = URLEncoder.encode(websearch.text.toString(),"UTF-8")

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("url", webView.url)
    }

    private fun toggleButtonsState(enabled: Boolean){
        google.isEnabled = enabled
        twitter.isEnabled = enabled
        reddit.isEnabled = enabled
        facebook.isEnabled = enabled
    }

    private fun loadurl(page: String){
        webView.loadUrl(page)
    }

}
