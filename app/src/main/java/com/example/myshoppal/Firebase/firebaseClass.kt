package com.example.myshoppal.Firebase

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.models.*
import com.example.myshoppal.ui.activites.*
import com.example.myshoppal.ui.fragments.DashboardFragment
import com.example.myshoppal.ui.fragments.OrderFragment
import com.example.myshoppal.ui.fragments.ProductsFragment
import com.example.myshoppal.ui.fragments.SoldProductsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class firebaseClass {

    // create firebase fire store instance variable...
    private val mFirestore = FirebaseFirestore.getInstance()

    // function to create collection and document in database...
    fun registeruser(activity: RegisterActivity, userinfo: User) {

        mFirestore.collection(Constant.USERS)
                .document(getcurrentuserid())
                .set(userinfo, SetOptions.merge())
                .addOnCompleteListener {

                    activity.Registerusersucces()

                }.addOnFailureListener {
                    activity.hideprogressdialog()
                    Log.e(activity.javaClass.simpleName, "error writing document")
                }
    }

    // get user information from data base...
    fun LouduserData(activity: Activity) {

        //get user document from fire store collection...
        mFirestore.collection(Constant.USERS).document(getcurrentuserid()).get()
                .addOnSuccessListener { document ->

                    val user = document.toObject(User::class.java)
                    when (activity) {

                        // load user information success method...
                        is loginActivity -> {
                            activity.signinusersucces(user!!)
                        }

                        is SettingActivity -> {

                            activity.loaduaerdatasuccess(user!!)
                        }
                    }

                }.addOnFailureListener {       // on failure function...


                    when (activity) {

                        is loginActivity -> {

                            activity.hideprogressdialog()
                        }
                        is SettingActivity -> {

                            activity.hideprogressdialog()
                        }
                    }
                }
    }

    // update user profile data...
    fun updateuserprofiledata(activity: CompleteProfile, userhashmap: HashMap<String, Any>) {

        mFirestore.collection(Constant.USERS)
                .document(getcurrentuserid())
                .update(userhashmap)
                .addOnSuccessListener {

                    Toast.makeText(activity, "profile updated successfully", Toast.LENGTH_SHORT).show()

                    activity.profilupdatesucces()

                }.addOnFailureListener { e ->

                    activity.hideprogressdialog()
                    Log.i("error", e.message.toString())
                }
    }

    // store product and profile image in data base...
    fun uploadimagetofirestore(activity: Activity, selectedimag: Uri ,imagetype:String) {

        val sref: StorageReference = FirebaseStorage.getInstance().reference.child(
                imagetype + System.currentTimeMillis() + "." +
                        Constant.getimageExtention(selectedimag, activity)
        )

        sref.putFile(selectedimag).addOnSuccessListener{ tasksnapshot ->

                Log.i("prodim",imagetype)
                tasksnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { Uri ->

                        val image_uri = Uri.toString()

                    when(activity){

                      is AddProduct ->{

                          activity.uploadimagesuccess(image_uri)
                      }
                      is CompleteProfile ->{

                          activity.profileimageuploadesuccess(image_uri)

                      }

                    }
                }

        }.addOnFailureListener {
            exeption ->

            when(activity){

                is CompleteProfile ->{
                    activity.hideprogressdialog()
                    Toast.makeText(activity, exeption.message, Toast.LENGTH_LONG).show()
                }
                is AddProduct ->{
                    activity.hideprogressdialog()
                    Toast.makeText(activity, exeption.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun getproductsdetails(fragment: Fragment) {

        mFirestore.collection(Constant.PRODUCTS)
                .whereEqualTo(Constant.USER_ID, getcurrentuserid())
                .get()
                .addOnSuccessListener { documents ->

                    val products: ArrayList<product> = ArrayList()

                    for (i in documents.documents) {

                        val product = i.toObject(product::class.java)
                        product!!.id = i.id
                        products.add(product)
                    }

                    when (fragment) {

                        is ProductsFragment -> {

                            fragment.loadproductsdetailssuccess(products)

                        }
                    }
                }.addOnFailureListener {

                    when (fragment) {

                        is ProductsFragment -> {

                            fragment.hideprogressdialog()
                        }
                    }
                }
         }


    fun getAllproductsdetails(fragment: Fragment){

        mFirestore.collection(Constant.PRODUCTS)
                .get()
                .addOnSuccessListener {documents ->

                    val products :ArrayList<product> = ArrayList()

                    for (i in documents.documents){

                       val  product = i.toObject(product::class.java)
                        products.add(product!!)
                    }
                    when(fragment){

                        is DashboardFragment ->{

                            fragment.loadAllproductsdetailssuccess(products)
                        }
                    }
                }.addOnFailureListener {

                    when(fragment){

                        is DashboardFragment ->{

                            fragment.hideprogressdialog()
                        }
                    }
                }
           }

    // delete product function...
    fun deleteproduct (fragment: ProductsFragment, products:product) {

        mFirestore.collection(Constant.PRODUCTS)
                .document(products.id)
                .delete()
                .addOnSuccessListener { documents ->

                    fragment.deleteproductsuccess()

                }.addOnFailureListener {

                    fragment.hideprogressdialog()
                }
    }


    fun updateproduct(activity: AddProduct,hashMap: HashMap<String,String>,productid:String){

        mFirestore.collection(Constant.PRODUCTS)
            .document(productid)
            .update(hashMap as Map<String, Any>)
            .addOnSuccessListener {

                activity.productUpdatesuccess()

            }.addOnFailureListener {
                activity.hideprogressdialog()
                Log.e(activity.javaClass.simpleName, "error updating product")
            }


    }
    // function to create collection and document in database...
    fun createproduct(activity: AddProduct, product: product) {

        val ref = mFirestore.collection(Constant.PRODUCTS).document().id

        product.id = ref

        mFirestore.collection(Constant.PRODUCTS)
                .document(ref)
                .set(product, SetOptions.merge())
                .addOnCompleteListener {

                    activity.createproductesuccess()

                }.addOnFailureListener {
                    activity.hideprogressdialog()
                    Log.e(activity.javaClass.simpleName, "error writing document")
                }
    }


    fun addCartItem (activity: ProductDetailsActivity, cartitem:CartItem){

        val ref = mFirestore.collection(Constant.CART_ITEM).document().id
        cartitem.id = ref

        mFirestore.collection(Constant.CART_ITEM)
            .document(ref)
            .set(cartitem, SetOptions.merge())
            .addOnSuccessListener {document ->

                activity.addtocartsuccess()

            }.addOnFailureListener {
                activity.hideprogressdialog()
                Log.e(activity.javaClass.simpleName, "error writing document")
            }
    }


    fun checkifitemExistincart(activity: ProductDetailsActivity,productid:String){

        mFirestore.collection(Constant.CART_ITEM)
            .whereEqualTo(Constant.PRODUCT_USER_ID , getcurrentuserid())
            .whereEqualTo(Constant.CART_ITEM_ID,productid)
            .get()
            .addOnSuccessListener { documents ->

                if (documents.documents.size > 0){

                   val cartitem = documents.documents[0].toObject(CartItem::class.java)

                    activity.getitemsfromcartsuccess(cartitem!!)

                }else{

                //    Toast.makeText(activity,"problem",Toast.LENGTH_LONG).show()

                    activity.setcartbuttonshow()
                    activity.hideprogressdialog()
                }
            }.addOnFailureListener {

                activity.hideprogressdialog()
            }
    }

    fun getAllItemsInCart(activity: Activity){

        mFirestore.collection(Constant.CART_ITEM)
            .whereEqualTo(Constant.PRODUCT_USER_ID , getcurrentuserid())
            .get()
            .addOnSuccessListener { documents ->

                val cartitems :ArrayList<CartItem> = ArrayList()

                if (documents.documents.size > 0){

                    for (i in documents.documents) {

                        val item = i.toObject(CartItem::class.java)

                        cartitems.add(item!!)
                    }

                    when (activity) {
                        is MycartActivity -> {

                            activity.getcartitemssuccess(cartitems)
                        }
                        is checkoutActivity -> {

                            activity.getcartitemssuccess(cartitems)

                        }
                        else -> {  Toast.makeText(activity,"problem",Toast.LENGTH_LONG).show()  }
                    }

                }else {

                    //    Toast.makeText(activity,"problem",Toast.LENGTH_LONG).show()
                    when (activity) {
                        is MycartActivity -> {

                            activity.getcartitemssuccess(cartitems)
                        }

                        else -> {
                            Toast.makeText(activity, "problem", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }.addOnFailureListener {

                when (activity) {
                    is MycartActivity -> {

                       activity.hideprogressdialog()
                    }

                    is checkoutActivity ->{

                        activity.hideprogressdialog()
                    }
                }
            }
    }


    fun UpdateCartQuantity (activity: Activity,hashmap:HashMap<String,Any>,cartitemid:String){

        mFirestore.collection(Constant.CART_ITEM)
            .document(cartitemid)
            .update(hashmap)
            .addOnSuccessListener {

                Log.i("success","item added successfully")


            }.addOnFailureListener {

                Log.i("error","error adding item")
            }
    }


    fun deletecartitem (activity: MycartActivity,itemid:String){

        mFirestore.collection(Constant.CART_ITEM)
            .document(itemid)
            .delete()
            .addOnSuccessListener {

                activity.getcartitems()
                //Toast.makeText(activity, "product deleted successfully",Toast.LENGTH_LONG).show()

            }.addOnFailureListener {

                Log.i("error","error deleting product")
            }
    }

    fun getallproducts (activity: Activity) {

        mFirestore.collection(Constant.PRODUCTS)
            .get()
            .addOnSuccessListener { documents ->

                val productslist: ArrayList<product> = ArrayList()

                for (i in documents.documents) {

                    val product = i.toObject(product::class.java)

                    productslist.add(product!!)
                }

                when (activity) {
                    is MycartActivity -> {

                        activity.getallproductssuccess(productslist)
                    }
                    is checkoutActivity -> {

                        activity.getallproductssuccess(productslist)

                    }
                }

            }.addOnFailureListener {

                when (activity) {
                    is MycartActivity -> {

                        Log.i("error", "error loading products")

                        activity.hideprogressdialog()
                    }
                    is checkoutActivity -> {

                        activity.hideprogressdialog()

                    }
                }
            }
    }
    fun addAddress (activity: Add_Edit_AddressActivity,address:Address){

        val ref = mFirestore.collection(Constant.ADDRESS).document().id
        address.id = ref

        mFirestore.collection(Constant.ADDRESS)
            .document(ref)
            .set(address, SetOptions.merge())
            .addOnSuccessListener {document ->

                activity.addAddressSuccess()

            }.addOnFailureListener {
                activity.hideprogressdialog()
                Log.e(activity.javaClass.simpleName, "error writing document")
            }

    }

    fun getalladdresses(activity: AddressesActivity){

        mFirestore.collection(Constant.ADDRESS)
            .whereEqualTo(Constant.ADDRESS_USER_ID,getcurrentuserid())
            .get()
            .addOnSuccessListener {documents ->

                val addresseslist:ArrayList<Address> = ArrayList()

                for (i in documents.documents){

                    val address = i.toObject(Address::class.java)

                    addresseslist.add(address!!)
                }

                activity.getaddressessuccess(addresseslist)

            }.addOnFailureListener {

                activity.hideprogressdialog()
                Log.i("error","error loading products")

            }
    }

    fun deleteaddress (activity: AddressesActivity,addressid:String){

        mFirestore.collection(Constant.ADDRESS)
            .document(addressid)
            .delete()
            .addOnSuccessListener {

                activity.deleteaddresssuccess()
                //Toast.makeText(activity, "product deleted successfully",Toast.LENGTH_LONG).show()

            }.addOnFailureListener {

                Log.i("error","error deleting product")
            }

    }


    fun updateAddressdata(activity: Add_Edit_AddressActivity, userhashmap: HashMap<String, Any>,addressId :String) {

        mFirestore.collection(Constant.ADDRESS)
            .document(addressId)
            .update(userhashmap)
            .addOnSuccessListener {

                activity.updateaddresssuccess()

            }.addOnFailureListener { e ->

                activity.hideprogressdialog()
                Log.i("error", e.message.toString())
            }
    }

    fun createplaceoredercoolection (activity: checkoutActivity, order: Order){

        val ref = mFirestore.collection(Constant.ADDRESS).document().id
        order.id = ref

        mFirestore.collection(Constant.PLACE_ORDER)
            .document(ref)
            .set(order, SetOptions.merge())
            .addOnSuccessListener {document ->

                activity.placeordersuccess()

            }.addOnFailureListener {
                activity.hideprogressdialog()
                Log.e(activity.javaClass.simpleName, "error writing document")
            }
    }

    fun updatealldetails(activity: checkoutActivity,cartlist:ArrayList<CartItem>,order:Order){

        val writebatch = mFirestore.batch()

        for (cartitem in cartlist){

           val producthashmap = HashMap<String,Any>()
               producthashmap[Constant.PRODUCT_STOCK] =
                   (cartitem.stock_quantity.toInt() - cartitem.cart_quantity.toInt()).toString()

            val productdocumentreference = mFirestore.collection(Constant.PRODUCTS)
                .document(cartitem.product_id)

            writebatch.update(productdocumentreference,producthashmap)

            val id = mFirestore.collection(Constant.SOLD_PRODUCT).document().id
            val documentreference = mFirestore.collection(Constant.SOLD_PRODUCT)
                .document(id)

            val soldproduct = SoldProduct(

                cartitem.product_owner_id,
                cartitem.title,
                cartitem.price,
                cartitem.cart_quantity,
                cartitem.image,
                order.id,
                order.order_date,
                order.title,
                order.sub_total_amount,
                order.shipping_charge,
                order.total_amount,
                order.address,
                id

            )

            writebatch.set(documentreference,soldproduct)
        }

        for (cartitem in cartlist){

            val documentreference = mFirestore.collection(Constant.CART_ITEM)
                .document(cartitem.id)
            writebatch.delete(documentreference)
        }

        writebatch.commit().addOnSuccessListener {

            activity.alldetailsupdatedsuccessfully()

        }.addOnFailureListener {

            activity.hideprogressdialog()
        }
    }

    fun getAllorderItems(fragment : OrderFragment){

        mFirestore.collection(Constant.PLACE_ORDER)
            .whereEqualTo(Constant.PRODUCT_USER_ID , getcurrentuserid())
            .get()
            .addOnSuccessListener { documents ->

                val orderlist :ArrayList<Order> = ArrayList()

                if (documents.documents.size > 0){

                    for (i in documents.documents) {

                        val item = i.toObject(Order::class.java)

                        orderlist.add(item!!)
                    }

                    fragment.getorderplacedsuccess(orderlist)


                }else {

                    fragment.getorderplacedsuccess(orderlist)
                    fragment.hideprogressdialog()
                }
            }.addOnFailureListener {


                        fragment.hideprogressdialog()

            }
    }


    fun deleteorder (fragment: OrderFragment, orderid:String){

        mFirestore.collection(Constant.PLACE_ORDER)
            .document(orderid)
            .delete()
            .addOnSuccessListener {

                fragment.deleteordersuccess()
                //Toast.makeText(activity, "product deleted successfully",Toast.LENGTH_LONG).show()

            }.addOnFailureListener {

                fragment.hideprogressdialog()
                Log.i("error","error deleting product")
            }

    }

    fun getsoldproductsfromstore(fragment : SoldProductsFragment){

        mFirestore.collection(Constant.SOLD_PRODUCT)
            .whereEqualTo(Constant.PRODUCT_OWNER_ID , getcurrentuserid())
            .get()
            .addOnSuccessListener { documents ->

                val soldproducts :ArrayList<SoldProduct> = ArrayList()

                    for (i in documents.documents) {

                        val item = i.toObject(SoldProduct::class.java)

                        soldproducts.add(item!!)
                    }

                    fragment.getproductssuccess(soldproducts)


            }.addOnFailureListener {


                fragment.hideprogressdialog()

            }
    }
    // get current user id to use it as document name...
    fun getcurrentuserid(): String {

        val currentuser = FirebaseAuth.getInstance().currentUser
        var currentuserid = ""

        if (currentuser != null) {

            currentuserid = currentuser.uid
        }
        return currentuserid

    }

}