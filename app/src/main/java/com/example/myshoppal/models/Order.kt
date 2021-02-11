package com.example.myshoppal.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order (

    val user_id :String = "",
    var id :String = "",
    val items :ArrayList<CartItem> = ArrayList(),
    val address: Address = Address(),
    val sub_total_amount :String = "",
    val shipping_charge :String = "",
    val total_amount :String = "",
    val title :String = "",
    val image :String = "",
    val order_date :Long = 0L
):Parcelable