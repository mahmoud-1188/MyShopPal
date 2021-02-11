package com.example.myshoppal.ui.activites

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.Utils.Glideloader
import com.example.myshoppal.models.User
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_complete_profile.*
import kotlinx.android.synthetic.main.activity_complete_profile.title_complete

class CompleteProfile : BaseActivity(), View.OnClickListener {

    private val PICK_IMAGE_FROM_Galary = 5
    private val READ_STORAGE_REQUEST_CODE = 4
    private var mselectedimageuri: Uri? = null
    private var mprofileimageuri: String = ""
    lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)


        if (intent.hasExtra(Constant.EXTRA_USER_DETAILS)) {

            user = intent.getParcelableExtra(Constant.EXTRA_USER_DETAILS)!!
        }


        complete_btn.setOnClickListener(this)
        image_complete.setOnClickListener(this)

        setUiitems()
    }


    //retrieve data from data base and put it in complete profile activity...
    private fun setUiitems (){

        showprogressdialog("please wait")

        complete_ed_first_name.setText(user.first_name)
        complete_ed_last_name.setText(user.last_name)
        complete_ed_emile_id.setText(user.email)
        complete_ed_emile_id.isEnabled = false

        if (user.profilcomplet == 0) {

            title_complete.text = "COMPLETE PROFILE"
            complete_ed_first_name.isEnabled = false
            complete_ed_last_name.isEnabled = false

        }else{

            settoolbar()

            title_complete.text = "EDIT PROFILE"
            Glideloader(this).loaduserprofileimage(user.image, image_complete)

            if (user.mobile != 0L){

                complete_ed_mobile.setText(user.mobile.toString())
            }

            when(user.gander){

                "male" ->{

                    gender_male.isChecked = true
                }

                "female" ->{

                    gender_female.isChecked = true
                }
            }
        }

        hideprogressdialog()
    }

    // this override function to recive request permission result...
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == READ_STORAGE_REQUEST_CODE) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                choosingProfileimage()  // if we have the right permission we execute choose image function..
            }
        } else {  // this toast appear if the user denied the permission...
            Toast.makeText(
                this, "oops, you just denied the permission for storage," +
                        " you can also allow it from setting", Toast.LENGTH_SHORT
            ).show()
        }

    }

    // this function to recive result from activity...
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_FROM_Galary && resultCode == Activity.RESULT_OK) {

            mselectedimageuri = data!!.data

            // use glide function to set user image...
            Glideloader(this).loaduserprofileimage(mselectedimageuri!!, image_complete)
        }
    }


    // this function to upload user image to fire store storage and update user data...
    private fun uploaduserimage() {

        // show dialog..
        showprogressdialog(resources.getString(R.string.dialog_progress))

        // check uri variable is not null...
        if (mselectedimageuri != null) {

            firebaseClass().uploadimagetofirestore(this, mselectedimageuri!!,Constant.USER_PROFILE_TYPE)

        }else{
            //update user profile data..
            updateuserprofiledata()
        }
    }

    fun profileimageuploadesuccess(image:String){

        // show dialog..
        showprogressdialog(resources.getString(R.string.dialog_progress))

        mprofileimageuri = image
        hideprogressdialog()

        updateuserprofiledata()

    }
    //this function call when user profile update successfully...
    fun profilupdatesucces() {

        hideprogressdialog()

        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    // this function to update user profile data..firebase storage..
    private fun updateuserprofiledata() {

        val hashmap = HashMap<String, Any>()  // create hash map variable..
        Log.i("imag",mprofileimageuri)
        if (mprofileimageuri.isNotEmpty()) {
            hashmap[Constant.USER_IMAGE] = mprofileimageuri
        }
        val mobile = complete_ed_mobile.text.toString().trim { it <= ' ' }
        // check mobile field is empty or not..and add to hash map..
        if (mobile.isNotEmpty() && mobile != user.mobile.toString()){
            hashmap[Constant.USER_MOBILE] = mobile.toLong()
        }
        // chang value of profile complete to 1 in data base...
        hashmap[Constant.PROFIL_COMPLETE] = 1

        // check which gander is selected...male or female..
        //start
        val gander = if (gender_male.isChecked){

            Constant.MALE
        }else{

            Constant.FEMALE
        }
        if (complete_ed_first_name.text.toString() != user.first_name){

            hashmap[Constant.FIRST_NAME] = complete_ed_first_name.text.toString()
        }

        if (complete_ed_last_name.text.toString() != user.last_name){

            hashmap[Constant.LAST_NAME] = complete_ed_last_name.text.toString()
        }
        hashmap[Constant.GANDER] = gander
        //end

        // call firebase class  ..
        firebaseClass().updateuserprofiledata(this, hashmap)
    }


    // this function to pick an image from galary..
    private fun choosingProfileimage() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_FROM_Galary)

    }

    // this function to check if mobile field is empty or not....
    private fun CheckItemsifEmpty(): Boolean {


        return when {

            TextUtils.isEmpty(complete_ed_mobile.text.toString().trim { it <= ' ' }) -> {

                showerrorsnackbar("please enter your mobile number!", true)
                false
            }

            else -> {
                true
            }
        }
    }


    // set toolbar method...
    private fun settoolbar(){

        setSupportActionBar(complete_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_white_arrow_back_24)
        complete_toolbar.setNavigationOnClickListener { onBackPressed() }
    }


    override fun onClick(view: View?) {
        when(view!!.id){

            R.id.image_complete ->{

                //check if the user have permission or not..
                if (ContextCompat.checkSelfPermission(
                        this, Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {

                    choosingProfileimage()

                } else {    // ask the user for permission..
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        READ_STORAGE_REQUEST_CODE
                    )
                }
            }

            R.id.complete_btn ->{

              if (CheckItemsifEmpty()) {

                  uploaduserimage()
              }
            }
        }
    }
}