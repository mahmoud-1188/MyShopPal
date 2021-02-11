package com.example.myshoppal.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.Utils.Glideloader
import com.example.myshoppal.models.CartItem
import com.example.myshoppal.ui.activites.MycartActivity

import kotlinx.android.synthetic.main.cart_item.view.*

class CartItemsAdapter(val activity: Activity,val cartitems:ArrayList<CartItem>, val updatecart:Boolean):
    RecyclerView.Adapter<CartItemsAdapter.myviewholder>() {


    private var onclicklistener: OnClickListener? = null

    lateinit var view :View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {

        view = LayoutInflater.from(activity).inflate(R.layout.cart_item,parent,false)
        return myviewholder(view)

    }

   // private var selectedItem = -1

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: myviewholder, position: Int) {

        val model = cartitems[position]

        val price = model.price

      /*  if (position == selectedItem){

            holder.itemView.setBackgroundColor(Color.parseColor("#EFF3F4"))
        }else{

            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"))
        }*/

        holder.itemView.cart_item_title.text = model.title
        holder.itemView.cart_item_quantity.text = model.cart_quantity
        holder.itemView.cart_item_price.text = "$${model.price}"

        Glideloader(activity).loadproductimage(model.image,holder.itemView.cart_item_image)



        holder.itemView.cart_item_delete.setOnClickListener {

            alertdialogfordeletingcartitem(model)
        }


        var addtoCart = model.cart_quantity.toInt()

        if (model.cart_quantity == "0"){

            if (updatecart){
                holder.itemView.cart_item_delete.visibility = View.VISIBLE
            }else{
                holder.itemView.cart_item_delete.visibility = View.GONE
            }

            holder.itemView.cart_add.visibility = View.GONE
            holder.itemView.cart_remove.visibility  =View.GONE
            holder.itemView.cart_item_quantity.layoutParams.width = 350
            holder.itemView.cart_item_quantity.text = "out of stock"
            holder.itemView.cart_item_quantity.setTextColor(Color.RED)

        }else {

            if (updatecart){
                holder.itemView.cart_add.visibility = View.VISIBLE
                holder.itemView.cart_remove.visibility  =View.VISIBLE
                holder.itemView.cart_item_delete.visibility = View.VISIBLE

                holder.itemView.cart_add.setOnClickListener {

                    if (addtoCart < model.stock_quantity.toInt()) {

                        addtoCart += 1
                        val hashBiMap = HashMap<String, Any>()
                        hashBiMap[Constant.CART_QUANTITY_EDIT] = addtoCart.toString()

                        firebaseClass().UpdateCartQuantity(activity, hashBiMap, model.id)

                        firebaseClass().getAllItemsInCart(activity)
                        holder.itemView.cart_item_quantity.text = addtoCart.toString()

                    }else{

                        when(activity){

                            is MycartActivity ->{

                                activity.showerrorsnackbar("Available stock is ${addtoCart}" +
                                        " you cant add more than stock quantity",true)
                            }
                        }
                    }
                }


                holder.itemView.cart_remove.setOnClickListener {

                    if (addtoCart == 1) {

                        firebaseClass().deletecartitem(activity as MycartActivity,model.id)

                    }else{

                        addtoCart -= 1
                        val hashBiMap = HashMap<String, Any>()
                        hashBiMap[Constant.CART_QUANTITY_EDIT] = addtoCart.toString()

                        firebaseClass().UpdateCartQuantity(activity, hashBiMap, model.id)

                        firebaseClass().getAllItemsInCart(activity)

                        holder.itemView.cart_item_quantity.text = addtoCart.toString()

                    }
                }
            }else{
                holder.itemView.cart_add.visibility = View.GONE
                holder.itemView.cart_remove.visibility  =View.GONE
                holder.itemView.cart_item_delete.visibility = View.GONE
            }
        }


        holder.itemView.setOnClickListener {

           /* if (selectedItem == position){

                selectedItem = -1
                firebaseClass().getAllItemsInCart(context as MycartActivity)

                notifyDataSetChanged()
                return@setOnClickListener
            }

            selectedItem = position

            firebaseClass().getAllItemsInCart(context as MycartActivity)

            notifyDataSetChanged()*/


            onclicklistener!!.onclick(position,model)

        }

  }

    // delete task list dialog..
    private fun alertdialogfordeletingcartitem(cartItem: CartItem){

        val Bulder = AlertDialog.Builder(activity)
        Bulder.setTitle("Alert")
        Bulder.setMessage("are you sure you want to delete ${cartItem.title}")
        Bulder.setPositiveButton("Yes"){dialoginterface,whitch ->

            firebaseClass().deletecartitem(activity as MycartActivity,cartItem.id)

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

    override fun getItemCount(): Int {
          return cartitems.size
    }

    class myviewholder (view:View):RecyclerView.ViewHolder(view){

    }

    fun setonclicklistener(onclicklistener:OnClickListener){

        this.onclicklistener = onclicklistener
    }

    interface OnClickListener {

        fun onclick (position: Int,item: CartItem)
    }

}