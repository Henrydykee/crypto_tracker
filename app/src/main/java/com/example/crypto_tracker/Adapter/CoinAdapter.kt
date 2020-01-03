package com.example.crypto_tracker.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crypto_tracker.Common.Common
import com.example.crypto_tracker.Model.CoinModel
import com.example.crypto_tracker.R
import com.example.crypto_tracker.`interface`.loadMore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coin_layout.view.*
import java.lang.StringBuilder

class CoinViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
   var coinIcon= itemView.coin_Icon
    var coinsymbol= itemView.coin_symbol!!
    var coinName = itemView.coin_name!!
    var coinprice = itemView.priceUsd!!
    var oneHourChage= itemView.oneHour!!
    var twentyFourChange= itemView.twentyfourHour!!
    var sevendayChange= itemView.sevenDay!!
}
class CoinAdapter (recyclerView: RecyclerView,internal var activity: Activity,var items:List<CoinModel>):RecyclerView.Adapter<CoinViewHolder>()
{
    internal var loadMore: loadMore?=null
    var isLoading:Boolean=false
    var visibleThreshold=5
    var lastvisibleItem:Int=0
    var totalItemCount:Int=0

    init {
        val linearLayout = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount=linearLayout.itemCount
                lastvisibleItem=linearLayout.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <=lastvisibleItem+visibleThreshold)
                {
                    if (loadMore!=null)
                        loadMore!!.onLoadMore()
                    isLoading= true
                }
            }
        })
    }

    fun  setLoadMore(loadMore: loadMore)
    {
        this.loadMore=loadMore
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.coin_layout,parent,false)
        return CoinViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coinModel = items.get(position)
        val item = holder
        item.coinName.text=coinModel.name
        item.coinsymbol.text=coinModel.symbol
        item.coinprice.text=coinModel.price_usd
        item.oneHourChage.text=coinModel.percent_change_1h+"%"
        item.sevendayChange.text=coinModel.percent_change_7d+"%"
        item.twentyFourChange.text=coinModel.percent_change_24h+"%"
        Picasso.with(activity.baseContext)
            .load(StringBuilder(Common.imageUrl)
                .append(coinModel.symbol!!.toLowerCase())
                .append(".png")
                .toString())
            .into(item.coinIcon)

        //setcolour
        item.oneHourChage.setTextColor(if(coinModel.percent_change_1h!!.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32cd32")
        )
        item.sevendayChange.setTextColor(if(coinModel.percent_change_7d!!.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32cd32")
        )
        item.twentyFourChange.setTextColor(if(coinModel.percent_change_24h!!.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32cd32")
        )
    }

    fun setLoaded(){
        isLoading= false
    }
    fun updateData(coinModels: List<CoinModel>)
    {
        this.items=coinModels
        notifyDataSetChanged()
    }

}