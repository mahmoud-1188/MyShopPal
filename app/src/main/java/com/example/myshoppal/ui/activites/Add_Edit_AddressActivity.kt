package com.example.myshoppal.ui.activites

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.models.Address
import com.example.myshoppal.models.CartItem
import kotlinx.android.synthetic.main.activity_add__edit__address.*

class Add_Edit_AddressActivity : BaseActivity() {

    private var maddressdetails : Address? = null
    lateinit var mcart_items:ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add__edit__address)

        settoolbar()

        if (intent.hasExtra(Constant.ADDRESS_EDIT_DETAILS)){

            maddressdetails = intent.getParcelableExtra(Constant.ADDRESS_EDIT_DETAILS)!!

            setUiFieldToEdit()
        }



        address_radio_group.setOnCheckedChangeListener { _, chichedid ->

            if (chichedid == R.id.radio_other){

                address_other_text.visibility = View.VISIBLE

                }else{
                    address_other_ed.setText(" ")
                    address_other_text.visibility = View.GONE
                }
            }

        address_btn.setOnClickListener {

            if (maddressdetails != null ){

                if (CheckItemsifEmpty()) {
                    updateaddressdata()
                }

            }else {

                if (CheckItemsifEmpty()) {
                    addaddress()
                }
            }

        }
    }

    private fun setUiFieldToEdit (){

        address_btn.setText("UPDATE")
        edit_address_title.setText("EDIT ADDRESS")
        address_ed_first_name.setText(maddressdetails!!.first_name)
        address_phone_number_ed.setText(maddressdetails!!.phone_number.toString())
        address_address_ed.setText(maddressdetails!!.address)
        address_zibcode_ed.setText(maddressdetails!!.zibcode.toString())
        address_note_ed.setText(maddressdetails!!.address_note)

        when(maddressdetails!!.place){

            "home" -> {radio_home.isChecked = true}
            "office" -> {radio_office.isChecked = true}
            "other" -> {radio_other.isChecked = true
                address_other_text.visibility = View.VISIBLE
                address_other_ed.setText(maddressdetails!!.other_details)
            }
        }
    }

    private fun updateaddressdata (){

        val hashmap :HashMap<String,Any> = HashMap()

       val name= address_ed_first_name.text.toString()
       val mobile = address_phone_number_ed.text.toString().toInt()
       val address = address_address_ed.text.toString()
       val zibcode =  address_zibcode_ed.text.toString().toInt()
       val note = address_note_ed.text.toString()
        val place = when{

            radio_home.isChecked ->{"home"}
            radio_office.isChecked ->{"office"}
            radio_other.isChecked ->{"other"}
            else -> {""}
        }

        hashmap[Constant.ADDRESS_UPDATE] = address
        hashmap[Constant.ADDRESS_NOTE] = note
        hashmap[Constant.FIRST_NAME_update] = name
        hashmap[Constant.OTHER_DETAILS] = if (radio_other.isChecked) address_other_ed.text.toString() else ""
        hashmap[Constant.PHONE_NUMBER] = mobile
        hashmap[Constant.PLACE] = place
        hashmap[Constant.ZIBCODE] = zibcode

        showprogressdialog("please waite")

        firebaseClass().updateAddressdata(this,hashmap,maddressdetails!!.id)

    }

    fun updateaddresssuccess (){

        Toast.makeText(this, "Address updated successfully", Toast.LENGTH_SHORT).show()
        hideprogressdialog()
        setResult(RESULT_OK)
        finish()

    }

    override fun onDestroy() {

        hideprogressdialog()
        super.onDestroy()
    }

    private fun  addaddress (){

       showprogressdialog("please waite")

       val place = if (radio_home.isChecked){

           "home"
       }else if(radio_office.isChecked){

           "office"
       }else{

           "other"
       }

        val address = Address(

            "",
            address_ed_first_name.text.toString(),
            address_phone_number_ed.text.toString().toInt(),
            address_address_ed.text.toString(),
            address_zibcode_ed.text.toString().toInt(),
            address_note_ed.text.toString(),
            place,
            firebaseClass().getcurrentuserid(),
            address_other_ed.text.toString()

       )

        firebaseClass().addAddress(this,address)

    }

    fun addAddressSuccess(){

        hideprogressdialog()
        Toast.makeText(this,"address added successfully",Toast.LENGTH_LONG).show()

        setResult(RESULT_OK)
        finish()

    }

    // set toolbar method...
    private fun settoolbar(){

        setSupportActionBar(add_edit_address_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_arrow_back_24)
        add_edit_address_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    // this function to check the validation of register fields....
    private fun CheckItemsifEmpty() :Boolean{


        return when {

            TextUtils.isEmpty(address_ed_first_name.text.toString().trim {it <= ' '}) ->{

                showerrorsnackbar("please enter your first name!",true)
                false
            }

            TextUtils.isEmpty(address_phone_number_ed.text.toString().trim {it <= ' '}) ->{

                showerrorsnackbar("please enter your phone number!",true)
                false
            }

            TextUtils.isEmpty(address_address_ed.text.toString().trim {it <= ' '}) ->{

                showerrorsnackbar("please enter your address!",true)
                false
            }

            TextUtils.isEmpty(address_zibcode_ed.text.toString().trim {it <= ' '}) ->{

                showerrorsnackbar("please enter your password!",true)
                false
            }

             TextUtils.isEmpty(address_note_ed.text.toString().trim {it <= ' '}) ->{

                showerrorsnackbar("please enter your address note!",true)
                false
            }

            else -> {
                // showerrorsnackbar("your details are valid.",false)
                true
            }
        }
    }
}