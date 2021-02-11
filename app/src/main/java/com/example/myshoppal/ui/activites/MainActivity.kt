package com.example.myshoppal.ui.activites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settoolbar()

        // make instance from SharedPreferences...
        val sharedPreferences = getSharedPreferences(Constant.MYSHOPPAL_PREFERENCE,
            Context.MODE_PRIVATE)

        // get data from SharedPreferences...
        val username =sharedPreferences.getString(Constant.LOGGED_IN_USERNAME,"")!!

        Log.i("username",username)
        text_main.text = "your user name $username"

    }

    // set toolbar method...
   private fun settoolbar(){

        setSupportActionBar(main_toolbar)

    }

    // inflate menu to layout...
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.signout,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {

        doubleBackToExit()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){


            R.id.signout ->{

                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, loginActivity::class.java))
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}