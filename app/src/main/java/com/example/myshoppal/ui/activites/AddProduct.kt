package com.example.myshoppal.ui.activites

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.Utils.Glideloader
import com.example.myshoppal.models.product
import com.example.myshoppal.ui.fragments.ProductsFragment
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.fragment_products.*

class AddProduct : BaseActivity(),View.OnClickListener {

    private val PICK_PRODUCT_IMAGE_FROM_Galary = 6
    private val READ_STORAGE_REQUEST_CODE_PRODUCT = 8
    private var mSelectedProductImage :Uri? = null
    private var mProductImage = ""
    private var mproduct :product? = null
   lateinit var  productFragment :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        settoolbar()

        pick_image_product.setOnClickListener(this)
        add_product_btn.setOnClickListener(this)

        if (intent.hasExtra(Constant.PRODUCT_EDIT) || intent.hasExtra(Constant.PRODUCT_FRAGMENT)){

            productFragment = intent.getStringExtra(Constant.PRODUCT_FRAGMENT).toString()
            mproduct = intent.getParcelableExtra(Constant.PRODUCT_EDIT)
        }

        if (mproduct != null){

            setUiItemswithdata()
        }
    }

    // this override function to recive request permission result...
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == READ_STORAGE_REQUEST_CODE_PRODUCT) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                choosingProfileimage()  // if we have the right permission we execute choose image function..
            }
        } else {  // this toast appear if the user denied the permission...
            Toast.makeText(
                    this, "oops, you just denied the permission for storage," +
                    " you can also allow it from setting", Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun createproductesuccess(){

        hideprogressdialog()
        finish()
    }

    // create products collection in data base...
    private fun createproduct(){

        showprogressdialog("please wait")

        val username = this.getSharedPreferences(Constant.MYSHOPPAL_PREFERENCE, MODE_PRIVATE).
                getString(Constant.LOGGED_IN_USERNAME,"")

        val title = product_title_ed.text.toString()
        val price = product_price_ed.text.toString()
        val description = product_description_ed.text.toString()
        val quantity = product_quantity_ed.text.toString()

        var image = ""
        if (mProductImage.isNotEmpty()) {
             image = mProductImage
        }

        val product = product(username!!,title,price,description,quantity,
            image,firebaseClass().getcurrentuserid())

        firebaseClass().createproduct(this,product)
    }

    // upload product image success...
    fun uploadimagesuccess(image:String){

        mProductImage =image

        hideprogressdialog()

        if (mproduct != null){
            updateproduct()

        }else {
            createproduct()
        }
    }

    //store product image in data base...
    private fun UploadProductImage (){

        if (mSelectedProductImage !=null) {
            showprogressdialog("please wait")

            firebaseClass().uploadimagetofirestore(
                this,
                mSelectedProductImage!!,
                Constant.USER_PRODUCT_TYPE
            )
        }else if (productFragment == "product fragment") {

            createproduct()

        }else {

            updateproduct()
        }
    }

    fun setUiItemswithdata(){


        title_add_product.text = "Edit product"
        Glideloader(this).loadproductimage(mproduct!!.image,product_image)
        product_title_ed.setText(mproduct!!.title)
        product_price_ed.setText(mproduct!!.price)
        product_description_ed.setText(mproduct!!.description)
        product_quantity_ed.setText(mproduct!!.quantity)

    }

    private fun updateproduct(){

            val hashmap = HashMap<String,String>()

            hashmap[Constant.PRODUCT_DESCRIPTION] = product_description_ed.text.toString()
            hashmap[Constant.PRODUCT_ID] = mproduct!!.id

            if (mProductImage.isNotEmpty()) {
                hashmap[Constant.PRODUCT_IMAGE] = mProductImage
            }

            hashmap[Constant.PRODUCT_PRICE] = product_price_ed.text.toString()
            hashmap[Constant.PRODUCT_QUANTITY] = product_quantity_ed.text.toString()
            hashmap[Constant.PRODUCT_TITLE] = product_title_ed.text.toString()

            showprogressdialog("please wait")

            firebaseClass().updateproduct(this,hashmap,mproduct!!.id)

    }


    fun productUpdatesuccess (){

        hideprogressdialog()
        Toast.makeText(this,"product updated successfully",Toast.LENGTH_LONG).show()

        val intent = Intent(this,DashboardActivity::class.java)
        intent.putExtra("add product",1)

        startActivity(intent)

        //startActivity(Intent(this,DashboardActivity::class.java))
        finish()
    }



    // this function to recive result from activity...
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PRODUCT_IMAGE_FROM_Galary && resultCode == Activity.RESULT_OK) {

            mSelectedProductImage = data!!.data

            pick_image_product.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_edit_24))
           // product_image.setImageURI(mSelectedProductImage)


            // use glide function to set user image...
            Glide
                    .with(this)
                    .load(mSelectedProductImage)
                    .fitCenter()
                    .into(product_image)
        }
    }

    // set toolbar method...
    private fun settoolbar(){

        setSupportActionBar(add_product_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_arrow_back_24)
        add_product_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {

        when(v!!.id){

            R.id.pick_image_product -> {
                //check if the user have permission or not..
                if (ContextCompat.checkSelfPermission(
                                this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                   == PackageManager.PERMISSION_GRANTED ) {

                    choosingProfileimage()

                } else {    // ask the user for permission..
                    ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            READ_STORAGE_REQUEST_CODE_PRODUCT
                    )
                }
            }

            R.id.add_product_btn -> {

                if (CheckItemsifEmpty()){

                     UploadProductImage()

                }
            }
        }
    }

    // this function to pick an image from galary..
    private fun choosingProfileimage() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_PRODUCT_IMAGE_FROM_Galary)

    }

    // this function to check the validation of register fields....
    private fun CheckItemsifEmpty() :Boolean{

        return when {

            TextUtils.isEmpty(product_title_ed.text.toString().trim {it <= ' '}) ->{

                showerrorsnackbar("please enter product title!",true)
                false
            }

            TextUtils.isEmpty(product_price_ed.text.toString().trim {it <= ' '}) ->{

                showerrorsnackbar("please enter product price!",true)
                false
            }

            TextUtils.isEmpty(product_description_ed.text.toString().trim {it <= ' '}) ->{

                showerrorsnackbar("please enter product description!",true)
                false
            }

            TextUtils.isEmpty(product_quantity_ed.text.toString().trim {it <= ' '}) ->{

                showerrorsnackbar("please enter product quantity!",true)
                false
            }

            product_image == null ->{

                showerrorsnackbar("please select image!",true)
                false
            }

            else -> {
                true
            }
        }
    }
}