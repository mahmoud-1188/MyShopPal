package com.example.myshoppal.ui.activites

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R

class SplashActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        // this code to use full screen with out actionBar...
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // handler to delay splash activity & chick if user logged in or not...
        Handler().postDelayed({


            val currentuser = firebaseClass().getcurrentuserid()


            if (currentuser.isEmpty()) {

                startActivity(Intent(this, loginActivity::class.java))

            }else{

                startActivity(Intent(this, DashboardActivity::class.java))

            }

            finish()

        },2500)

    }
}