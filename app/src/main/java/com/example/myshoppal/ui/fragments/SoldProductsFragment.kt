package com.example.myshoppal.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.adapters.OrderAdapter
import com.example.myshoppal.models.Order
import com.example.myshoppal.models.SoldProduct
import com.example.myshoppal.ui.activites.OrderdDetailsActivity
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.fragment_sold_products.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [SoldProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SoldProductsFragment : BaseFragment() {
    // TODO: Rename and change types of parameters


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold_products, container, false)
    }

    override fun onResume() {
        getsoldproducts()
        super.onResume()
    }

    private fun getsoldproducts (){

        showprogressdialog("please waite")
        firebaseClass().getsoldproductsfromstore(this)

    }

    fun getproductssuccess(soldproducts:ArrayList<SoldProduct>){

        val order:ArrayList<Order> = ArrayList()
        val orderfragment :OrderFragment = OrderFragment()


        val adapter = OrderAdapter(requireContext(),order,-1,orderfragment,soldproducts,true)

        if (soldproducts.size > 0 ){

            sold_products_recycler.visibility = View.VISIBLE
            sold_no_found.visibility = View.GONE
            sold_products_recycler.layoutManager = LinearLayoutManager(requireActivity())
            sold_products_recycler.adapter = adapter

        }else{
            sold_products_recycler.visibility = View.GONE
            sold_no_found.visibility = View.VISIBLE
        }

        hideprogressdialog()

    }
}