package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

    private val encodedSearchTerm : String
        get() = URLEncoder.encode(etSearch.text.toString(),"UTF-8")

    private fun loadWebActivity(url: String) {
        val intent = Intent(this, WebSearch::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }
}