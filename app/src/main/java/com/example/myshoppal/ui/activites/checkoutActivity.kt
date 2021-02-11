package com.example.myshoppal.ui.activites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.adapters.CartItemsAdapter
import com.example.myshoppal.models.Address
import com.example.myshoppal.models.CartItem
import com.example.myshoppal.models.Order
import com.example.myshoppal.models.product
import kotlinx.android.synthetic.main.activity_checkout.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class checkoutActivity : BaseActivity() {

    lateinit var mcartlist :ArrayList<CartItem>
    private var maddress :Address? = null
    lateinit var mproductslist : ArrayList<product>
    private var  msubtotal :Double = 0.0
    private var  mtotalamount :Double = 0.0
    private lateinit var morder :Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

       /* if (intent.hasExtra(Constant.CART_ITEMS_LIST)){

            mcartlist = (intent.getParcelableArrayListExtra(Constant.CART_ITEMS_LIST))!!
        }*/

        if (intent.hasExtra(Constant.ADDRESS_DETAILS)){

            maddress = intent.getParcelableExtra(Constant.ADDRESS_DETAILS)!!
        }

        settoolbar()

        getallproducts()

        btn_place_order.setOnClickListener {

            placeorder()
        }
    }

    private fun setupaddressUi (){

            tv_checkout_address_type.text = maddress!!.place
            tv_checkout_address.text = maddress!!.address
            tv_checkout_additional_note.text = maddress!!.address_note
            tv_checkout_full_name.text = maddress!!.first_name
            tv_checkout_other_details.text = maddress!!.other_details
            tv_mobile_number.text = maddress!!.phone_number.toString()

    }

    private fun getallproducts(){

        showprogressdialog("please waite")
        firebaseClass().getallproducts(this@checkoutActivity)
    }

    fun getallproductssuccess(productslist:ArrayList<product>){

        hideprogressdialog()

        mproductslist = productslist

        getcartitems()
    }

    private fun getcartitems(){

        showprogressdialog("please waite")
        firebaseClass().getAllItemsInCart(this)
    }


    fun getcartitemssuccess(cartitems:ArrayList<CartItem>) {

        hideprogressdialog()

        for (product in mproductslist){
            for (cartitem in cartitems){
                if (cartitem.product_id == product.id){
                    cartitem.stock_quantity = product.quantity
                    if (cartitem.cart_quantity.toInt() > cartitem.stock_quantity.toInt()){
                        cartitem.cart_quantity = cartitem.stock_quantity
                        val hashmap : HashMap<String,Any> = HashMap()
                        hashmap["cart_quantity"] = cartitem.cart_quantity
                        firebaseClass().UpdateCartQuantity(this,hashmap,cartitem.id)
                    }
                }
            }
        }
        mcartlist = cartitems

        if (mcartlist.size > 0) {

            val adapter = CartItemsAdapter(this, mcartlist,false)

            rv_cart_list_items.layoutManager = LinearLayoutManager(this)
            rv_cart_list_items.adapter = adapter


            for (i in mcartlist){

                msubtotal += i.cart_quantity.toInt() * i.price.toInt()
            }

            tv_checkout_sub_total.text = msubtotal.toString()

            mtotalamount = msubtotal + 10

            tv_checkout_total_amount.text = mtotalamount.toString()
        }

        setupaddressUi()

    }

    private fun placeorder(){

        showprogressdialog("please wait")


      if (maddress != null) {
              morder = Order(

              firebaseClass().getcurrentuserid(),
              "",
              mcartlist,
              maddress!!,
              msubtotal.toString(),
              "10.0",
              mtotalamount.toString(),
              " ${System.currentTimeMillis()}",
              mcartlist[0].image,
              System.currentTimeMillis()
          )

        firebaseClass().createplaceoredercoolection(this,morder)
       }
    }

    fun alldetailsupdatedsuccessfully(){

        hideprogressdialog()
        Toast.makeText(this,"order placed successfully",Toast.LENGTH_LONG).show()
        val intent = Intent(this,DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()


    }

    fun placeordersuccess (){

        firebaseClass().updatealldetails(this,mcartlist,morder)

    }

        // set toolbar method...
    private fun settoolbar(){

        setSupportActionBar(toolbar_checkout_activity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_arrow_back_24)
        toolbar_checkout_activity.setNavigationOnClickListener { onBackPressed() }
    }
}