package com.example.myshoppal.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class product (

        val user_name :String = "",
        val title :String = "",
        val price :String = "",
        val description :String = "",
        val quantity :String = "",
        val image :String = "",
        val user_id :String = "",
        var id :String = ""

):Parcelable
