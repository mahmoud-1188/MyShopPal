package com.example.myshoppal.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem (

    val product_owner_id :String = "",
    val product_id :String = "",
    val title :String = "",
    val price :String = "",
    var cart_quantity :String = "",
    var stock_quantity :String = "",
    val image :String = "",
    val user_id :String = "",
    var id :String = ""

) : Parcelable