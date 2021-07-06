package com.example.newsfeed

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter:NewsListAdaptar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Define ActionBar object

        // Define ActionBar object
        val actionBar: ActionBar?
        actionBar = supportActionBar

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        val colorDrawable = ColorDrawable(Color.parseColor("#45135A"))

        // Set BackgroundDrawable

        // Set BackgroundDrawable
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(colorDrawable)
        }



        recyclerView.layoutManager=LinearLayoutManager(this)
        fetchData()
        mAdapter= NewsListAdaptar(this)
        recyclerView.adapter=mAdapter
    }

    private fun fetchData(){

//        api key:  7802659816204908a849f5ebfdc19247
        val url="https://newsapi.org/v2/top-headlines?country=in&apiKey=7802659816204908a849f5ebfdc19247";
        val jsonObjectRequest = object:JsonObjectRequest(Request.Method.GET,
                url,
                null,
                Response.Listener {
                    val newsJsonArray=it.getJSONArray("articles")
                    val newsArray=ArrayList<News>()
                    for(i in 0 until newsJsonArray.length()){
                        val newsJsonObject=newsJsonArray.getJSONObject(i)

                        val news=News(
                                newsJsonObject.getString("title"),
                                newsJsonObject.getString("author"),
                                newsJsonObject.getString("url"),
                                newsJsonObject.getString("urlToImage")
                        )
                        Log.d("errorThis",newsJsonObject.getString("title"))
                        newsArray.add(news)
                    }
                    mAdapter.updateNews(newsArray)
                },
                Response.ErrorListener {
                    Toast.makeText(this,"Something went wrong...",Toast.LENGTH_LONG).show()
                }

        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {

        val colorInt = Color.parseColor("#45135A")

        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(colorInt)
        val customTabsIntent = builder.build()


        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }

}