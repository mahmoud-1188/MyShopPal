package com.example.myshoppal.ui.activites

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.adapters.AddressesAdapter
import com.example.myshoppal.models.Address
import com.example.myshoppal.models.CartItem
import com.myshoppal.utils.SwipeToEditCallback
import com.myshoppal.utils.SwipeTodeleteCallback
import kotlinx.android.synthetic.main.activity_addresses.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_setting.*

class AddressesActivity : BaseActivity() {

    lateinit var maddresseslist :ArrayList<Address>
    lateinit var msharedpreference :SharedPreferences
    private var mchosingaddress = ""
    private var mitemposition = -1
    private var mselectaddress = false
    private var mcart_items:ArrayList<CartItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addresses)

        settoolbar()

        if (intent.hasExtra(Constant.SHARED_ADDRESS)){

            mchosingaddress = intent.getStringExtra(Constant.SHARED_ADDRESS)!!
        }

        if (intent.hasExtra(Constant.EXTRA_SELECT_ADDRESS)){

            mselectaddress = intent.getBooleanExtra(Constant.EXTRA_SELECT_ADDRESS,false)
        }

        if (intent.hasExtra(Constant.CART_ITEMS_LIST)){

            mcart_items = intent.getParcelableArrayListExtra(Constant.CART_ITEMS_LIST)!!
        }

        if (mselectaddress){

            address_title.setText("SELECT ADDRESS")
        }

            add_address.setOnClickListener {
                val intent = Intent(this, Add_Edit_AddressActivity::class.java)
                if (mcart_items != null) {
                    intent.putExtra(Constant.CART_ITEMS_LIST, mcart_items)
                }
                    startActivityForResult(intent, Constant.ADD_ADDRESS_REQUEST_CODE)
            }


        getalladdress()

        msharedpreference = getSharedPreferences(Constant.SHARED_PREFERENCE, MODE_PRIVATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==Constant.ADD_ADDRESS_REQUEST_CODE && resultCode == Activity.RESULT_OK){

            Log.i("loadd","loadd")
            getalladdress()
        }
    }

    private fun getalladdress(){

        showprogressdialog("please waite")

        firebaseClass().getalladdresses(this)
    }


    fun getaddressessuccess(addresses:ArrayList<Address>){

          maddresseslist = addresses

        if (addresses.size > 0){

            address_recycler.visibility = View.VISIBLE
            address_no_found.visibility = View.GONE

            val adapter = AddressesAdapter(this,addresses,mselectaddress)

            address_recycler.layoutManager = LinearLayoutManager(this)
            address_recycler.setHasFixedSize(true)
            address_recycler.adapter = adapter

            if (!mselectaddress) {
                adapter.setonclicklistener(object : AddressesAdapter.OnClickListener {
                    override fun onclick(position: Int, address: String) {

                        Log.i("address", address.toString())
                        val intent = Intent(this@AddressesActivity, SettingActivity::class.java)
                        intent.putExtra(Constant.INTENT_ADDRESS, address)
                        intent.putExtra("addressesActivity", "address")
                        startActivity(intent)
                        finish()
                    }
                })
            }


            if (!mselectaddress) {
                val editswiphandler = object : SwipeToEditCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        val madapter = address_recycler.adapter as AddressesAdapter
                        madapter.notifiyedititem(this@AddressesActivity, viewHolder.adapterPosition)
                    }
                }
                val edititemtouchHelper = ItemTouchHelper(editswiphandler)
                edititemtouchHelper.attachToRecyclerView(address_recycler)


                val deleteswiphandler = object : SwipeTodeleteCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        val madapter = address_recycler.adapter as AddressesAdapter
                        madapter.notifiyDeleteitem(viewHolder.adapterPosition)

                        mitemposition = viewHolder.adapterPosition

                    }
                }
                val deleteitemtouchHelper = ItemTouchHelper(deleteswiphandler)
                deleteitemtouchHelper.attachToRecyclerView(address_recycler)
            }


        }else{

            address_recycler.visibility = View.GONE
            address_no_found.visibility = View.VISIBLE
        }

        hideprogressdialog()
    }


    fun deleteaddresssuccess(){

        if (mchosingaddress == maddresseslist[mitemposition].address) {

            val editor = msharedpreference.edit()
            editor.remove(firebaseClass().getcurrentuserid()).apply()
        }

        hideprogressdialog()

        getalladdress()
    }



    // set toolbar method...
    private fun settoolbar(){

        setSupportActionBar(address_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_arrow_back_24)
        address_toolbar.setNavigationOnClickListener {

          if (!mselectaddress) {
              val intent = Intent(this, SettingActivity::class.java)
              intent.putExtra("addresseslist", maddresseslist)
              intent.putExtra("addressesActivity", "address")
              startActivity(intent)
              finish()
          }else{
              onBackPressed()
          }
        }
    }
}