 package com.example.myshoppal.ui.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.adapters.CartItemsAdapter
import com.example.myshoppal.models.CartItem
import com.example.myshoppal.models.product
import kotlinx.android.synthetic.main.activity_mycart.*

class MycartActivity : BaseActivity() {

    private lateinit var mproductslist:ArrayList<product>
    private lateinit var mcartitems:ArrayList<CartItem>
    private val mselectaddress = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycart)

        settoolbar()

        checkout_btn.setOnClickListener {

            val intent = Intent(this,AddressesActivity::class.java)
            intent.putExtra(Constant.CART_ITEMS_LIST,mcartitems)
            intent.putExtra(Constant.EXTRA_SELECT_ADDRESS, mselectaddress)
            startActivity(intent)

        }
    }

    override fun onResume() {

        getallproducts()
        super.onResume()
    }

    // set toolbar method...
    private fun settoolbar(){

        setSupportActionBar(cart_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_arrow_back_24)
        cart_toolbar.setNavigationOnClickListener { onBackPressed() }
    }


    private fun getallproducts(){

        showprogressdialog("please waite")
        firebaseClass().getallproducts(this)
    }

    fun getallproductssuccess(productslist:ArrayList<product>){

        hideprogressdialog()

        mproductslist = productslist

        getcartitems()
    }

     fun getcartitems(){

        showprogressdialog("please waite")
        firebaseClass().getAllItemsInCart(this)
    }


    fun getcartitemssuccess(cartitems:ArrayList<CartItem>){

       // Toast.makeText(this,"cart items downloaded success",Toast.LENGTH_LONG).show()

        for (product in mproductslist){
            for (cartitem in cartitems){
                if (cartitem.product_id == product.id){

                    cartitem.stock_quantity = product.quantity

                    if (product.quantity.toInt() == 0){

                        cartitem.cart_quantity = product.quantity
                    }
                }
            }

        }

        mcartitems = cartitems

        if (mcartitems.size > 0){

            cart_recycle.visibility = View.VISIBLE
            ll_cart.visibility = View.VISIBLE
            cart_no_item_found_text.visibility = View.GONE

            val adapter = CartItemsAdapter(this,cartitems,true)
            cart_recycle.layoutManager = LinearLayoutManager(this)
            cart_recycle.adapter = adapter
            var subtotal = 0.0

            for (item in mcartitems){
                val availablequantity = item.stock_quantity.toInt()
                if (availablequantity > 0) {
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()
                    subtotal += price * quantity
                }
            }
            if (subtotal > 0) {
                ll_cart.visibility = View.VISIBLE
                cart_subtotal.text = "$${subtotal}"
                cart_shipping_charge.text = "$10"
                val total = subtotal + 10
                cart_total_amount.text = "$${total}"
            }else{
                ll_cart.visibility = View.GONE
            }
            adapter.setonclicklistener(object :CartItemsAdapter.OnClickListener{
                override fun onclick(position: Int, item: CartItem) {

                  //  firebaseClass().getAllItemsInCart(this@MycartActivity)

                //    adapter.notifyDataSetChanged()
                }
            })
        }else{

            ll_cart.visibility = View.GONE
            cart_recycle.visibility = View.GONE
            cart_no_item_found_text.visibility = View.VISIBLE

        }

        hideprogressdialog()
    }
}