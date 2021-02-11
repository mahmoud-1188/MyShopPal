package com.example.myshoppal.ui.activites

import android.os.Bundle
import android.widget.Toast
import com.example.myshoppal.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget_password.*

class ForgetPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)


        settoolbar()

        // send reset email to reset the password...
        submit_btn.setOnClickListener {

            val email = forgot_email_text.text.toString().trim {it <= ' '}

            if (email.isEmpty()){

                Toast.makeText(this,
                    "please enter your email or password",
                    Toast.LENGTH_SHORT).show()
            }else{

                showprogressdialog(resources.getString(R.string.dialog_progress))

                // send reset email...
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {task ->

                        if (task.isSuccessful){

                            hideprogressdialog()
                            Toast.makeText(this,
                                "email sent successfully to reset yor password",
                                Toast.LENGTH_SHORT).show()
                            finish()
                        }else{

                            hideprogressdialog()
                            Toast.makeText(this,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }


    }



    private fun settoolbar(){

        setSupportActionBar(forgot_password_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_arrow_back_24)
        forgot_password_toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}