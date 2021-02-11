package com.example.myshoppal.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoldProduct (

    val product_owner_id :String = "",
    val title :String = "",
    val price :String = "",
    val sold_quantity :String = "",
    val image :String = "",
    val order_id :String = "",
    val order_date :Long = 0L,
    val order_title :String = "",
    val sub_total_amount :String = "",
    val shipping_charge :String = "",
    val total_amount :String = "",
    val address :Address = Address(),
    val id :String = "",

) : Parcelable