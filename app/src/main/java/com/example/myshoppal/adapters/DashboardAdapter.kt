package com.example.myshoppal.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Glideloader
import com.example.myshoppal.models.product
import kotlinx.android.synthetic.main.dashboard_items.view.*

class DashboardAdapter (val context: Context, val products:ArrayList<product>): RecyclerView.Adapter<DashboardAdapter.myviewholder>() {

    private var onclicklistener: OnClickListener? = null


    class myviewholder(view: View): RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {

        return myviewholder(LayoutInflater.from(context).inflate(R.layout.dashboard_items,
                parent,false))
    }

    override fun onBindViewHolder(holder: myviewholder, position: Int) {

        val product = products[position]

        Glideloader(context).loadproductimage(product.image,holder.itemView.dashboard_image)
        holder.itemView.dashboard_title.text = product.title
        holder.itemView.dashboard_price.text = " $${product.price}"

        holder.itemView.setOnClickListener {

            onclicklistener!!.onclick(position,product)

        }

    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setonclicklistener(onclicklistener:OnClickListener){

        this.onclicklistener = onclicklistener
    }

    interface OnClickListener {

        fun onclick (position: Int,product:product)
    }
}