package com.example.myshoppal.ui.activites

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.progress_dialog.*

open class BaseActivity : AppCompatActivity() {

    private var doublebacktoexitpressedone = false
    private var mprogressdaialog :Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    // function to show Snack bar message..
    fun showerrorsnackbar(message:String,errormesage:Boolean){

        val snackbar = Snackbar.make(findViewById(android.R.id.content)
            ,message, Snackbar.LENGTH_LONG)

        if (errormesage) {
            val snackbarview = snackbar.view
            snackbarview.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.snackbar_error_color
                )
            )
        }else{

            val snackbarview = snackbar.view
            snackbarview.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.green
                )
            )

        }
        snackbar.show()

    }

    // function to show progress dialog...
    fun showprogressdialog (text:String){

        if (mprogressdaialog == null) {
            mprogressdaialog = Dialog(this)

            mprogressdaialog!!.setContentView(R.layout.progress_dialog)

            mprogressdaialog!!.tv_dialog.text = text

            mprogressdaialog!!.show()
        }
    }

    fun hideprogressdialog (){

        if (mprogressdaialog != null) {
            mprogressdaialog!!.dismiss()
        }
    }

    //function to return current user id..
    fun getcurrentuserid ():String{

        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    // function to manage back button to exit...
    fun doubleBackToExit (){

        if (doublebacktoexitpressedone){
            super.onBackPressed()
            return
        }

        this.doublebacktoexitpressedone = true
        Toast.makeText(this,"please click back again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({doublebacktoexitpressedone = false},2000)

    }

}