package com.example.myshoppal.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.Firebase.firebaseClass
import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.adapters.OrderAdapter
import com.example.myshoppal.models.Order
import com.example.myshoppal.ui.activites.OrderdDetailsActivity
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment : BaseFragment() {

    //private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_order, container, false)

        return root
    }

    override fun onResume() {
        getorderplacedfromstore()
        super.onResume()
    }

    private fun getorderplacedfromstore (){

        showprogressdialog("please waite")

        firebaseClass().getAllorderItems(this)

    }
    fun getorderplacedsuccess(orderlist:ArrayList<Order>){

        val adapter = OrderAdapter(requireContext(),orderlist,-1,this)

        if (orderlist.size > 0 ){

            order_recycle.visibility = View.VISIBLE
            order_no_order_found.visibility = View.GONE
            order_recycle.layoutManager = LinearLayoutManager(requireActivity())
            order_recycle.adapter = adapter

            adapter.setonclicklistener(object :OrderAdapter.OnClickListener{

                override fun onclick(position: Int, orderlist: ArrayList<Order>) {

                    val intent = Intent(context, OrderdDetailsActivity::class.java)
                    intent.putExtra(Constant.ORDER_DETAILS, orderlist)
                    intent.putExtra(Constant.ORDER_POSITION, position)
                    startActivity(intent)
                }
            })

        }else{
            order_recycle.visibility = View.GONE
            order_no_order_found.visibility = View.VISIBLE
        }
        hideprogressdialog()

    }
    fun deleteordersuccess(){

        Toast.makeText(requireActivity(),"order deleted successfully",Toast.LENGTH_LONG).show()

        getorderplacedfromstore()

    }
}