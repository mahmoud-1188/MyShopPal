package com.example.myshoppal.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.adapters.ProductsAdapter
import com.example.myshoppal.models.product
import com.example.myshoppal.ui.activites.AddProduct
import com.example.myshoppal.ui.activites.ProductDetailsActivity
import kotlinx.android.synthetic.main.fragment_products.*

open class ProductsFragment : BaseFragment() {

   lateinit var productsList :ArrayList<product>
   private val productFragment = "product fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        //add menu to tool bar...
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        getproductsdetails()

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
      //  homeViewModel =ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_products, container, false)

        return root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.add -> {
                val intent = Intent(activity, AddProduct::class.java)
                intent.putExtra(Constant.PRODUCT_FRAGMENT,productFragment)
                startActivity(intent)

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun loadproductsdetailssuccess(products: ArrayList<product>){

        productsList = products

        if (products.size > 0){

            product_recycler.visibility = View.VISIBLE
            product_text.visibility = View.GONE

            val adapter = ProductsAdapter(this, products)

            product_recycler.layoutManager = LinearLayoutManager(requireActivity())
            product_recycler.adapter = adapter

            adapter.setonclicklistener(object :ProductsAdapter.OnClickListener{
                override fun onclick(position: Int, product: product) {

                    val intent = Intent(requireActivity(),ProductDetailsActivity::class.java)
                        intent.putExtra(Constant.PRODUCT_DETAILS,product)
                        intent.putExtra("PRODUCT_FRAGMENT","product fragment2")
                        startActivity(intent)

                }

            })

        }else{

            product_recycler.visibility = View.GONE
            product_text.visibility = View.VISIBLE

        }


        hideprogressdialog()
    }

    private fun getproductsdetails(){

        showprogressdialog("please wait")

        firebaseClass().getproductsdetails(this)

    }

    fun deleteproductsuccess(){

        hideprogressdialog()

        firebaseClass().getproductsdetails(this)

        showprogressdialog("please waite")
    }

    fun deleteproduct (position:Int){

       showprogressdialog("please waite")

       firebaseClass().deleteproduct(this,productsList[position])

    }
}