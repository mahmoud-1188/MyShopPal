package com.example.myshoppal.ui.activites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.Utils.Glideloader
import com.example.myshoppal.models.CartItem
import com.example.myshoppal.models.product
import kotlinx.android.synthetic.main.activity_product_details.*
class ProductDetailsActivity : BaseActivity(),View.OnClickListener {

    lateinit var product :product
    private var menuItem :Boolean = false
    lateinit var currentUser :String
    private var mcartitems:CartItem? = null
    private var mproductfragment:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        settoolbar()


        add_to_cart_btn.setOnClickListener(this)
        go_to_cart_btn.setOnClickListener(this)

        if (intent.hasExtra("PRODUCT_FRAGMENT")||intent.hasExtra(Constant.PRODUCT_DETAILS) || intent.hasExtra(Constant.MENU_ITEM_DISPLAY)){

            mproductfragment = intent.getStringExtra("PRODUCT_FRAGMENT").toString()
            menuItem = intent.getBooleanExtra(Constant.MENU_ITEM_DISPLAY,true)
            product = intent.getParcelableExtra(Constant.PRODUCT_DETAILS)!!
        }

       //  getproductsfromCart()


    }

    override fun onResume() {

        stupUielement()
        getproductsfromCart()
        super.onResume()
    }

    fun stupUielement(){

        Glideloader(this).loadproductimage(product.image,product_details_image)

        product_details_title.text = product.title
        product_details_price.text = "$${product.price}"
        product_details_description.text = product.description
        product_details_quantity.text = product.quantity

        currentUser = firebaseClass().getcurrentuserid()


        if (currentUser == product.user_id ){

                add_to_cart_btn.visibility = View.GONE
                go_to_cart_btn.visibility = View.GONE

        }else{

            add_to_cart_btn.visibility = View.VISIBLE
        }


        if ( product.quantity.toInt() == 0 ){
            cart_out_of_stock.visibility = View.VISIBLE
            add_to_cart_btn.visibility = View.GONE
            go_to_cart_btn.visibility = View.GONE
        }else{
            cart_out_of_stock.visibility = View.GONE
        }

    }
    fun addToCart (){

        showprogressdialog("please waite")
        val addtocart = CartItem(

            product.user_id,
            product.id,
            product.title,
            product.price,
            Constant.CART_QUANTITY,
            product.quantity,
            product.image,
            firebaseClass().getcurrentuserid()

        )

        firebaseClass().addCartItem(this,addtocart)
    }

    fun addtocartsuccess (){

        hideprogressdialog()

       // Toast.makeText(this,"added to cart successfully",Toast.LENGTH_LONG).show()
        add_to_cart_btn.visibility = View.GONE
        go_to_cart_btn.visibility = View.VISIBLE

    }

       private fun getproductsfromCart (){
        showprogressdialog("please waite")

        firebaseClass().checkifitemExistincart(this,product.id)

    }

    fun getitemsfromcartsuccess(cartitems: CartItem){

        mcartitems = cartitems

        if (currentUser != product.user_id && mcartitems != null) {
            if (product.id == mcartitems!!.product_id && mproductfragment != "product fragment2" ) {

                add_to_cart_btn.visibility = View.GONE
                go_to_cart_btn.visibility = View.VISIBLE
            }
        }

       // Toast.makeText(this,"cart items download successfully",Toast.LENGTH_LONG).show()
        hideprogressdialog()
    }

    fun setcartbuttonshow(){


        if (currentUser != product.user_id && mcartitems == null && mproductfragment != "product fragment2" && product.quantity.toInt() != 0 ) {

            add_to_cart_btn.visibility = View.VISIBLE
            go_to_cart_btn.visibility = View.GONE
        }

       // Toast.makeText(this,"cart items download successfully",Toast.LENGTH_LONG).show()
        hideprogressdialog()
    }

    // set toolbar method...
    private fun settoolbar(){

        setSupportActionBar(product_details_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_arrow_back_24)
        product_details_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.product_details_edite_menu,menu)

        val itemEdit :MenuItem = menu!!.getItem(0)

          if (!menuItem) {
              itemEdit.isVisible = false
          }


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.product_edit ->{

                val intent = Intent(this,AddProduct::class.java)
                    intent.putExtra(Constant.PRODUCT_EDIT,product)
                    startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {

        if (v !=null){
            when(v.id){

                R.id.add_to_cart_btn ->{

                    addToCart()
                }

                R.id.go_to_cart_btn ->{

                    val intent = Intent(this,MycartActivity::class.java)
                    intent.putExtra(Constant.PRODUCT_EDIT,product)
                    startActivity(intent)
                }
            }
        }
    }
}