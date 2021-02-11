package com.example.myshoppal.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.Utils.Glideloader
import com.example.myshoppal.models.Order
import com.example.myshoppal.models.SoldProduct
import com.example.myshoppal.ui.activites.OrderdDetailsActivity
import com.example.myshoppal.ui.activites.soldproductdetailsActivity
import com.example.myshoppal.ui.fragments.OrderFragment
import kotlinx.android.synthetic.main.order_item.view.*

class OrderAdapter (val context: Context,
                    val orderlist:ArrayList<Order> =ArrayList(),
                    val orderposition: Int = -1,
                    val fragment: OrderFragment = OrderFragment(),
                    val soldproducts:ArrayList<SoldProduct> = ArrayList(),
                    val soldproductsFragment:Boolean = false
                    ) :RecyclerView.Adapter<OrderAdapter.myviewholder>() {

    private var onclicklistener: OnClickListener? = null

    class myviewholder (view:View):RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {
       return myviewholder(LayoutInflater.from(context).inflate(R.layout.order_item,parent,false))

    }

    override fun onBindViewHolder(holder: myviewholder, position: Int) {

        if (orderposition > -1){

            val orderitem = orderlist[orderposition].items[position]

            holder.itemView.order_item_title.text = orderitem.title
            holder.itemView.order_item_price.text = "$${orderitem.price}"
            holder.itemView.order_item_quantity.text = orderitem.cart_quantity
            holder.itemView.order_item_delete.visibility = View.GONE

            Glideloader(context).loadproductimage(orderitem.image, holder.itemView.order_item_image)


        } else if (soldproductsFragment) {

            val soldProduct = soldproducts[position]

            holder.itemView.order_item_title.text = soldProduct.title
            holder.itemView.order_item_price.text = "$${soldProduct.price}"
            holder.itemView.order_item_quantity.text = soldProduct.sold_quantity
            holder.itemView.order_item_delete.visibility = View.GONE

            Glideloader(context).loadproductimage(soldProduct.image, holder.itemView.order_item_image)

            holder.itemView.setOnClickListener {

                val intent = Intent(context,soldproductdetailsActivity::class.java)
                intent.putExtra(Constant.SOLD_PRODUCT_DETAILS,soldProduct)
                context.startActivity(intent)

            }


        }else{

            val model = orderlist[position]

            holder.itemView.order_item_title.text = model.title
            holder.itemView.order_item_price.text = "$${model.total_amount}"
            holder.itemView.order_item_quantity.visibility = View.GONE

            Glideloader(context).loadproductimage(model.image, holder.itemView.order_item_image)

            holder.itemView.order_item_delete.setOnClickListener {

                alertdialogfordeletingorderitem(model)

            }

            holder.itemView.setOnClickListener {

                onclicklistener!!.onclick(position,orderlist)
            }
        }
    }

    override fun getItemCount(): Int {

        return if (orderposition > -1) {
            orderlist[orderposition].items.size

        }else if (soldproductsFragment){

          soldproducts.size
        }else{

            orderlist.size
        }
    }
    fun setonclicklistener(onclicklistener:OnClickListener){

        this.onclicklistener = onclicklistener
    }

    interface OnClickListener {

        fun onclick (position: Int,orderlist:ArrayList<Order>)
    }

    // delete task list dialog..
    private fun alertdialogfordeletingorderitem(orderplace:Order){

        val Bulder = AlertDialog.Builder(context)
        Bulder.setTitle("Alert")
        Bulder.setMessage("are you sure you want to delete ${orderplace.title}")
        Bulder.setPositiveButton("Yes"){dialoginterface,whitch ->

            firebaseClass().deleteorder(fragment,orderplace.id)

            // firebaseClass().getAllItemsInCart(context)
            dialoginterface.dismiss()

        }.setNegativeButton("NO"){
                dialoginterface,whitch ->

            dialoginterface.dismiss()
        }

        val alertdialog = Bulder.create()
        alertdialog.setCancelable(false)
        alertdialog.show()
    }
}