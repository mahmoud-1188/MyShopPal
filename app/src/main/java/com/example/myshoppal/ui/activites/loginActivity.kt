package com.example.myshoppal.ui.activites

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class loginActivity : BaseActivity(),View.OnClickListener{

    lateinit var auth: FirebaseAuth
    lateinit var sharpreferences:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        auth = Firebase.auth
        sharpreferences = getSharedPreferences(Constant.MYSHOPPAL_PREFERENCE, MODE_PRIVATE)

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

        // on click listener event...
        login_register_txt.setOnClickListener(this)
        login_btn.setOnClickListener(this)
        login_forget_password.setOnClickListener(this)

    }

    // login success method and get user information...
    fun signinusersucces (user :User){

        hideprogressdialog()

        // check if the user first time use the application..
        // and based on that the application will determines which activity should directs user to.
       // start
        if (user.profilcomplet == 0){

            // start intent to complete activity and put extra to intent...
            val intent = Intent(this, CompleteProfile::class.java)
            intent.putExtra(Constant.EXTRA_USER_DETAILS,user)
            startActivity(intent)

        }else{
            startActivity(Intent(this, DashboardActivity::class.java))

        }
        // end

        // store user data to SharedPreferences after we get it from data base ...
        // start
        val sharedPreferences :SharedPreferences = getSharedPreferences(Constant.MYSHOPPAL_PREFERENCE,
            Context.MODE_PRIVATE)
        val editor :SharedPreferences.Editor = sharedPreferences.edit()

        editor.putString(Constant.LOGGED_IN_USERNAME,"${user.first_name} ${user.last_name}")
        editor.apply()
        // end

        finish()

    }

    // function to sign in the user...
    private fun signin(){

        val email = login_email_text.text.toString().trim{ it <= ' '}
        val password = login_password_text.text.toString().trim{ it <= ' '}

        if (CheckItemsifEmpty()){

            showprogressdialog(resources.getString(R.string.dialog_progress))
            // login user by email and password...
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {

                    task ->

                if (task.isSuccessful){

                    //get user info from firestore...
                    firebaseClass().LouduserData(this)

                }else{
                    hideprogressdialog()
                    Toast.makeText(this,"authentication Failed", Toast.LENGTH_SHORT).show()


                }
            }
        }

    }

    // this function to check the validation of login fields....
    private fun CheckItemsifEmpty() :Boolean{


        return when {

            TextUtils.isEmpty(login_email_text.text.toString().trim {it <= ' '}) ->{

                showerrorsnackbar("please enter your email!",true)
                false
            }

            TextUtils.isEmpty(login_password_text.text.toString().trim {it <= ' '}) ->{

                showerrorsnackbar("please enter your password!",true)
                false
            }
            else -> {
               // showerrorsnackbar("your details are valid.",false)
                true
            }
        }
    }

    // on click method for login clickable items. login button , forget password , register text.
    override fun onClick(v: View?) {
        if (v != null){

            when(v.id){

                R.id.login_register_txt ->{
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }
                R.id.login_btn ->{
                    signin()

                }

                R.id.login_forget_password ->{

                    val intent = Intent(this, ForgetPasswordActivity::class.java)
                    startActivity(intent)
                }

            }

        }
    }
}