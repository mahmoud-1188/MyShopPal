package com.example.myshoppal.Utils

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap

object Constant {


    const val USERS = "users"
    const val PLACE_ORDER = "order"
    const val SOLD_PRODUCT = "sold_product"
    const val MYSHOPPAL_PREFERENCE = "myshop preference"
    const val LOGGED_IN_USERNAME = "logged username"
    const val EXTRA_USER_DETAILS = "extra_user_details"
    const val USER_IMAGE = "image"
    const val USER_MOBILE = "mobile"
    const val GANDER = "gander"
    const val MALE = "male"
    const val FEMALE = "female"
    const val PROFIL_COMPLETE = "profilcomplet"
    const val FIRST_NAME = "first_name"
    const val LAST_NAME = "last_name"
    const val PRODUCTS = "products"
    const val USER_PROFILE_TYPE = "profile_image"
    const val USER_PRODUCT_TYPE = "product_image"
    const val USER_ID = "user_id"
    const val PRODUCT_DETAILS = "product details"
    const val PRODUCT_EDIT = "product edit"

    const val PRODUCT_DESCRIPTION = "description"
    const val PRODUCT_ID = "id"
    const val PRODUCT_IMAGE = "image"
    const val PRODUCT_PRICE = "price"
    const val PRODUCT_QUANTITY = "quantity"
    const val PRODUCT_TITLE = "title"

    const val MENU_ITEM_DISPLAY = "menu item"
    const val PRODUCT_FRAGMENT = "product fragment"

    const val CART_QUANTITY = "1"
    const val CART_ITEM = "cart item"
    const val PRODUCT_USER_ID = "user_id"
    const val CART_ITEM_ID = "product_id"

    const val CART_QUANTITY_EDIT = "cart_quantity"

    const val ADDRESS = "addresses"
    const val INTENT_ADDRESS = "address"

    const val SHARED_PREFERENCE = "shared preference"
    const val SHARED_ADDRESS = "shared address"

    const val ADDRESS_USER_ID = "user_id"
    const val ADDRESS_EDIT_DETAILS = "edit address details"

    const val ADDRESS_UPDATE = "address"
    const val ADDRESS_NOTE = "address_note"
    const val FIRST_NAME_update = "first_name"
    const val OTHER_DETAILS = "other_details"
    const val PHONE_NUMBER = "phone_number"
    const val PLACE = "place"
    const val ZIBCODE = "zibcode"

    const val EXTRA_SELECT_ADDRESS = "extra_select_address"
    const val ADD_ADDRESS_REQUEST_CODE = 122

    const val CART_ITEMS_LIST = "cart items"
    const val ADDRESS_DETAILS = "address details"

    const val PRODUCT_STOCK = "quantity"

    const val ORDER_DETAILS = "order details"
    const val ORDER_POSITION = "position"

    const val PRODUCT_OWNER_ID = "product_owner_id"
    const val SOLD_PRODUCT_DETAILS = "sold product details"







    // this function to return file Extention type...
    fun getimageExtention(uri: Uri?, activity: Activity): String? {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }


}