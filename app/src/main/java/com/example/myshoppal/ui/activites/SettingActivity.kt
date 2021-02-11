package com.example.myshoppal.ui.activites

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.edit
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.Utils.Glideloader
import com.example.myshoppal.models.Address
import com.example.myshoppal.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity(),View.OnClickListener {

    lateinit var muserdetails :User
    private var maddress :String =""
    private var maddressactivity =""
    private var maddresseslist :ArrayList<Address> = ArrayList()

    private lateinit var msharedpreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)


        msharedpreferences = getSharedPreferences(Constant.SHARED_PREFERENCE, MODE_PRIVATE)

        settoolbar()

        setting_btn.setOnClickListener(this)
        edit_text.setOnClickListener(this)
        setting_layout.setOnClickListener(this)

        if (intent.hasExtra(Constant.INTENT_ADDRESS)){

            maddress = intent.getStringExtra(Constant.INTENT_ADDRESS)!!
        }
        if (intent.hasExtra("addressesActivity")){

            maddressactivity = intent.getStringExtra("addressesActivity")!!

        }

        if (intent.hasExtra("addresseslist") ){

            maddresseslist = intent.getParcelableArrayListExtra("addresseslist")!!
        }

        if (maddressactivity == "address") {
            if (maddresseslist.size < 1) {

              msharedpreferences.edit().remove(firebaseClass().getcurrentuserid()).apply()
               /* editor.remove(firebaseClass().getcurrentuserid())
                editor.commit()*/
            }

            if(maddress.isNotEmpty()){
                val editor = msharedpreferences.edit()
                editor.putString(firebaseClass().getcurrentuserid(), maddress)
                editor.apply()
            }
        }

        val SharedPreferenceAddress = msharedpreferences.getString(firebaseClass().getcurrentuserid(),"Addresses")

        setting_address_text.text = SharedPreferenceAddress
       }
      override fun onResume() {

        getuserdata()
        super.onResume()
      }

    // load user data success..
    fun loaduaerdatasuccess (user: User){

        muserdetails = user

        hideprogressdialog()

        // use glide library to load user image...
        Glideloader(this).loaduserprofileimage(user.image,image_setting)

        name_text.text = "${user.first_name} ${user.last_name}"
        gender.text = user.gander
        email_text.text = user.email
        mobil_text.text = user.mobile.toString()
     }

    // load user data and put data in setting activity..
    private fun getuserdata(){

        showprogressdialog("please wait")

        firebaseClass().LouduserData(this)

     }

     // set toolbar method...
     private fun settoolbar(){

        setSupportActionBar(setting_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_arrow_back_24)
        setting_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

     override fun onClick(view: View?) {
        when(view!!.id){

            R.id.edit_text ->{

                val intent = Intent(this,CompleteProfile::class.java)
                intent.putExtra(Constant.EXTRA_USER_DETAILS,muserdetails)
                startActivity(intent)
            }

            R.id.setting_btn ->{

                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, loginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }

            R.id.setting_layout ->{

                val intent = Intent(this, AddressesActivity::class.java)
                intent.putExtra(Constant.SHARED_ADDRESS,setting_address_text.text.toString())
                startActivity(intent)
                finish()

            }
        }
    }
}