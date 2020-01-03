package com.example.crypto_tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crypto_tracker.Adapter.CoinAdapter
import com.example.crypto_tracker.Common.Common
import com.example.crypto_tracker.Model.CoinModel
import com.example.crypto_tracker.`interface`.loadMore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_crypto.*
import okhttp3.*
import java.io.IOException
import java.sql.ClientInfoStatus

class crypto : AppCompatActivity(),loadMore {

    internal var items:MutableList<CoinModel> = ArrayList()
    internal lateinit var adapter: CoinAdapter
    internal lateinit var client: OkHttpClient
    internal lateinit var request: Request
    override fun onLoadMore() {
        if (items.size <= Common.MAX_COIN_LOAD)
            loadnext10Coin(items.size)
        else(Toast.makeText(this@crypto,"Data max is "+Common.MAX_COIN_LOAD,Toast.LENGTH_SHORT).show())
    }

    private fun loadnext10Coin(index: Int) {
        client = OkHttpClient()
        request = Request.Builder()
            .url(String.format("https://api.coinmarketcap.com/v1/ticker/?start=%d&limit=10",index))
            .build()
        swipeContainer.isRefreshing= true
        client.newCall(request)
            .enqueue(object :Callback
            {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("Error",e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body()!!.string()
                    val gson = Gson()
                    val newItems = gson.fromJson<List<CoinModel>>(body,object: TypeToken<List<CoinModel>>(){}.type)
                    runOnUiThread{
                        items.addAll(newItems)
                        adapter.setLoaded()
                        adapter.updateData(items)

                        swipeContainer.isRefreshing = false
                    }
                }
            })
    }
    private fun loadFirst10Coin() {
        client = OkHttpClient()
        request = Request.Builder()
            .url(String.format("https://api.coinmarketcap.com/v1/ticker/?start=0&limit=10"))
            .build()
        swipeContainer.isRefreshing= true
        client.newCall(request)
            .enqueue(object :Callback
            {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("Error",e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body()!!.string()
                    val gson = Gson()
                    items = gson.fromJson(body,object: TypeToken<List<CoinModel>>(){}.type)
                    runOnUiThread{
                        adapter.updateData(items)
                    }
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crypto)
        swipeContainer.post { loadFirst10Coin() }
        swipeContainer.setOnRefreshListener {
             items.clear()
            loadFirst10Coin()
            setupAdater()
        }
        coin_recycler_view.layoutManager=LinearLayoutManager(this)
        setupAdater()
    }

    private fun setupAdater() {
        adapter = CoinAdapter(coin_recycler_view,this@crypto,items)
        coin_recycler_view.adapter = adapter
        adapter.setLoadMore(this)
    }
}
