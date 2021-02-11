package com.example.myshoppal.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Glideloader
import com.example.myshoppal.models.product
import com.example.myshoppal.ui.fragments.ProductsFragment
import kotlinx.android.synthetic.main.products_items.view.*

class ProductsAdapter(val context: ProductsFragment, val products:ArrayList<product>): RecyclerView.Adapter<ProductsAdapter.myviewholder>() {

        private var onclicklistener:OnClickListener? = null


    class myviewholder(view:View):RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {

        return myviewholder(LayoutInflater.from(context.requireActivity()).inflate(R.layout.products_items,
                parent,false))
    }

    override fun onBindViewHolder(holder: myviewholder, position: Int) {

        val product = products[position]

        Glideloader(context.requireContext()).loadproductimage(product.image,holder.itemView.product_item_image)
        holder.itemView.product_item_title.text = product.title
        holder.itemView.product_item_price.text = " $${product.price}"

        holder.itemView.product_delete_image.setOnClickListener {

            alertdialogfordeletinglist(position,product.title)

        }

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

    // delete product dialog..
    private fun alertdialogfordeletinglist(position:Int,title:String){

        val Bulder = AlertDialog.Builder(context.requireActivity())
        Bulder.setTitle("Alert")
        Bulder.setMessage("are you sure you want to delete $title")
        Bulder.setPositiveButton("Yes"){dialoginterface,whitch ->

            dialoginterface.dismiss()

            context.deleteproduct(position)

        }.setNegativeButton("NO"){
            dialoginterface,whitch ->

            dialoginterface.dismiss()
        }

        val alertdialog = Bulder.create()
        alertdialog.setCancelable(false)
        alertdialog.show()
    }

}