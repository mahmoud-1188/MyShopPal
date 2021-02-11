package com.example.myshoppal.ui.activites

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.adapters.OrderAdapter
import com.example.myshoppal.models.Order
import kotlinx.android.synthetic.main.activity_orderd_details.*
import kotlinx.android.synthetic.main.activity_soldproductdetails.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class OrderdDetailsActivity : BaseActivity() {

    private var morderdetails :ArrayList<Order> = ArrayList()
    private var mposition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orderd_details)

        settoolbar()

        if (intent.hasExtra(Constant.ORDER_DETAILS) || intent.hasExtra(Constant.ORDER_POSITION)){

            morderdetails = intent.getParcelableArrayListExtra(Constant.ORDER_DETAILS)!!
            mposition = intent.getIntExtra(Constant.ORDER_POSITION,-1)

        }

        populateUi()
    }



private fun populateUi(){

    showprogressdialog("please waite")
  if (morderdetails.size > 0) {

      val dateformat = "dd MM yyyy HH:mm"
      val formatter = SimpleDateFormat(dateformat, Locale.getDefault())
      val calender = Calendar.getInstance()
      calender.timeInMillis = morderdetails[mposition].order_date
      val orderdatetime = formatter.format(calender.time)
      order_details_date.text = orderdatetime.toString()

      val diffinMilliSeconds = System.currentTimeMillis() - morderdetails[mposition].order_date
      val diffinHours = TimeUnit.MILLISECONDS.toHours(diffinMilliSeconds)

      when{

          diffinHours < 1 ->{

              order_details_status.text = "pending"
              order_details_status.setTextColor(
                  ContextCompat.getColor(this,R.color.colorPrimary))
          }
          diffinHours < 2 ->{

              order_details_status.text = "in process"
              order_details_status.setTextColor(
                  ContextCompat.getColor(this,R.color.orange))
          }
           else ->{

              order_details_status.text = "Delivered"
              order_details_status.setTextColor(
                  ContextCompat.getColor(this,R.color.green))
          }
      }

    order_details_no.text = morderdetails[mposition].title
    order_details_address_place.text = morderdetails[mposition].address.place
    order_details_address_name.text = morderdetails[mposition].address.first_name
    order_details_address_address.text = morderdetails[mposition].address.address
    order_details_address_note.text = morderdetails[mposition].address.address_note
    order_details_address_other_details.text = morderdetails[mposition].address.other_details
    order_details_address_mobile.text = morderdetails[mposition].address.phone_number.toString()

    order_details_subtotal.text = morderdetails[mposition].sub_total_amount
    order_details_shipping.text = morderdetails[mposition].shipping_charge
    order_details_total_amount.text = morderdetails[mposition].total_amount
  }

    if (mposition > -1){

        val adapter = OrderAdapter(this,morderdetails,mposition)

        order_details_recycle.layoutManager = LinearLayoutManager(this)
        order_details_recycle.adapter = adapter

        order_details_total_amount.text = " $${morderdetails[mposition].total_amount}"
    }

    hideprogressdialog()

   }

    // set toolbar method...
    private fun settoolbar(){

        setSupportActionBar(order_details_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_arrow_back_24)
        order_details_toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}