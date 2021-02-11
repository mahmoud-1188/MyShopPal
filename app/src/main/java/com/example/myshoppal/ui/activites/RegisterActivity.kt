package com.example.myshoppal.ui.activites

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R
import com.example.myshoppal.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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

        // login button on click listener..
        register_text_login.setOnClickListener {

           onBackPressed()
        }

        // register button on click listener...
        register_btn.setOnClickListener {

            registeruser ()
        }

        settoolbar()

    }

    // set toolbar method...
    private fun settoolbar(){

        setSupportActionBar(register_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)
        register_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    // register user success function...firestore class.
    fun Registerusersucces (){

        hideprogressdialog()
        FirebaseAuth.getInstance().signOut() // sign out user...
        finish()

    }

    // function to register a user...
    private fun registeruser (){

        val email = register_ed_emile_id.text.toString()
        val password = register_ed_password.text.toString()
        val firstname = register_ed_first_name.text.toString()
        val lastname = register_ed_last_name.text.toString()

        if (CheckItemsifEmpty()){ // check if any field is empty...

            // show progress dialog...
            showprogressdialog(resources.getString(R.string.dialog_progress))
            // register user with email and password...
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener {

                    taske ->


                if (taske.isSuccessful){

                    val firebaseuser : FirebaseUser? = taske.result!!.user
                    val registeremail = firebaseuser!!.email

                    val user = User(firebaseuser.uid,firstname,lastname,registeremail!!)

                    showerrorsnackbar("you are registered successfully. your user is ${firebaseuser.uid}",false)

                    // function to register user info in database...
                    firebaseClass().registeruser(this,user)

                }else{
                    hideprogressdialog()
                    showerrorsnackbar(taske.exception!!.message.toString(),true)
                }
            }
        }
    }


    // this function to check the validation of register fields....
    private fun CheckItemsifEmpty() :Boolean{


        return when {

            TextUtils.isEmpty(register_ed_first_name.text.toString().trim {it <= ' '}) ->{

                showerrorsnackbar("please enter your first name!",true)
                false
            }

            TextUtils.isEmpty(register_ed_last_name.text.toString().trim {it <= ' '}) ->{

                showerrorsnackbar("please enter your last name!",true)
                false
            }

            TextUtils.isEmpty(register_ed_emile_id.text.toString().trim {it <= ' '}) ->{

                showerrorsnackbar("please enter your first email id!",true)
                false
            }

            TextUtils.isEmpty(register_ed_password.text.toString().trim {it <= ' '}) ->{

                showerrorsnackbar("please enter your password!",true)
                false
            }

            register_ed_password.text.toString().trim { it <= ' ' }
                 != register_ed_confirm_password.text.toString().trim { it <= ' '} ->{

                showerrorsnackbar("please confirm your password!",true)
                false
            }

             !register_checkbox.isChecked  ->{

                showerrorsnackbar("please read and confirm on the terms and condition!",true)
                false
            }

            else -> {
               // showerrorsnackbar("your details are valid.",false)
                true
            }

        }

    }
}