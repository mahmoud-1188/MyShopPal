package com.example.myshoppal.models


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (

        val id:String = "",
        val first_name :String = "",
        val last_name :String = "",
        val email :String = "",
        val mobile :Long = 0,
        val fcmtoken:String = "",
        val image :String = "",
        val gander :String = "",
        val profilcomplet :Int = 0


): Parcelable
