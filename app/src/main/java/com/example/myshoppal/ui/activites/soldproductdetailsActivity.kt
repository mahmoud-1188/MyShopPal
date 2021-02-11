package com.example.myshoppal.ui.activites

import android.os.Bundle
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.Utils.Glideloader
import com.example.myshoppal.models.SoldProduct
import kotlinx.android.synthetic.main.activity_add__edit__address.*
import kotlinx.android.synthetic.main.activity_soldproductdetails.*
import java.text.SimpleDateFormat
import java.util.*


class soldproductdetailsActivity : BaseActivity() {

    lateinit var msoldproductdetails :SoldProduct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soldproductdetails)

        if (intent.hasExtra(Constant.SOLD_PRODUCT_DETAILS)){

            msoldproductdetails = intent.getParcelableExtra(Constant.SOLD_PRODUCT_DETAILS)!!
        }

        settoolbar()

    }

    override fun onResume() {
        populateUi()
        super.onResume()
    }

    private fun populateUi(){

        showprogressdialog("please waite")
        if (msoldproductdetails.image.isNotEmpty()) {

            val dateformat = "dd MM yyyy HH:mm"
            val formatter = SimpleDateFormat(dateformat, Locale.getDefault())
            val calender = Calendar.getInstance()
            calender.timeInMillis = msoldproductdetails.order_date
            val orderdatetime = formatter.format(calender.time)

            sold_products_details_date.text = orderdatetime.toString()
            sold_products_item_title.text = msoldproductdetails.title
            sold_products_details_address_place.text = msoldproductdetails.address.place
            sold_products_details_address_name.text = msoldproductdetails.address.first_name
            sold_products_details_address_address.text = msoldproductdetails.address.address
            sold_products_details_address_note.text = msoldproductdetails.address.address_note
            sold_products_details_address_other_details.text = msoldproductdetails.address.other_details
            sold_products_details_address_mobile.text = msoldproductdetails.address.phone_number.toString()

            sold_products_details_no.text = msoldproductdetails.order_title
            sold_products_item_price.text = "$${msoldproductdetails.price}"
            sold_products_item_quantity.text = msoldproductdetails.sold_quantity

            Glideloader(this).loadproductimage(msoldproductdetails.image,sold_products_item_image)

            sold_products_details_total_amount.text = " $${msoldproductdetails.total_amount}"
            sold_products_details_shipping.text = "$${msoldproductdetails.shipping_charge}"
            sold_products_details_subtotal.text = "$${msoldproductdetails.sub_total_amount}"
      }

        hideprogressdialog()

    }

    // set toolbar method...
    private fun settoolbar(){

        setSupportActionBar(sold_products_details_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_arrow_back_24)
        sold_products_details_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

}