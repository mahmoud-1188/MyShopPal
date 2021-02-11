package com.example.myshoppal.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(

    var id :String = "",
    val first_name :String = "",
    val phone_number :Int = 0,
    val address :String = "",
    val zibcode :Int = 0,
    val address_note :String = "",
    val place :String = "",
    val user_id :String = "",
    val other_details :String = ""

) : Parcelable
