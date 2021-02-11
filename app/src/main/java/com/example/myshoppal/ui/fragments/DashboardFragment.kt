package com.example.myshoppal.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.Firebase.firebaseClass

import com.example.myshoppal.R
import com.example.myshoppal.Utils.Constant
import com.example.myshoppal.adapters.DashboardAdapter
import com.example.myshoppal.adapters.ProductsAdapter
import com.example.myshoppal.models.product
import com.example.myshoppal.ui.activites.MycartActivity
import com.example.myshoppal.ui.activites.ProductDetailsActivity
import com.example.myshoppal.ui.activites.SettingActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_products.*

class DashboardFragment : BaseFragment() {

    var menuItem = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

          setHasOptionsMenu(true)

    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  dashboardViewModel =ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }

    override fun onResume() {
        getproductsdetails()

        super.onResume()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when(item.itemId){

            R.id.setting ->{

                startActivity(Intent(activity, SettingActivity::class.java))

                return true
            }

            R.id.dashboard_cart ->{

                startActivity(Intent(activity, MycartActivity::class.java))

            }
        }

        return super.onOptionsItemSelected(item)
    }


    fun loadAllproductsdetailssuccess(products: ArrayList<product>){

        var list = true

        if (products.size > 0){

            dashboard_recycle.visibility = View.VISIBLE
            text_dashboard.visibility = View.GONE

            val adapter = DashboardAdapter(requireActivity(), products)

            dashboard_recycle.layoutManager = LinearLayoutManager(requireActivity())
            dashboard_recycle.setHasFixedSize(true)
            dashboard_recycle.adapter = adapter

            flc_button.setOnClickListener {
              if (list){

                  dashboard_recycle.layoutManager = GridLayoutManager(requireActivity(),2)
                  dashboard_recycle.setHasFixedSize(true)
                  dashboard_recycle.adapter = adapter

                  flc_button.setImageDrawable(
                     ContextCompat.getDrawable(requireContext(),R.drawable.ic_list_recycle_24))

                 list = false

              }else{

                  dashboard_recycle.layoutManager = LinearLayoutManager(requireActivity())
                  dashboard_recycle.setHasFixedSize(true)
                  dashboard_recycle.adapter = adapter

                  flc_button.setImageDrawable(
                      ContextCompat.getDrawable(requireContext(),R.drawable.ic_grid_recycle_24))

                 list = true
             }

        }

            adapter.setonclicklistener(object :DashboardAdapter.OnClickListener{
                override fun onclick(position: Int, product: product) {

                    val intent = Intent(requireActivity(), ProductDetailsActivity::class.java)
                    intent.putExtra(Constant.PRODUCT_DETAILS,product)
                    intent.putExtra(Constant.MENU_ITEM_DISPLAY,menuItem)
                    startActivity(intent)

                }

            })

        }else{

            dashboard_recycle.visibility = View.GONE
            text_dashboard.visibility = View.VISIBLE
        }

        hideprogressdialog()
    }

    private fun getproductsdetails(){

        showprogressdialog("please wait")

        firebaseClass().getAllproductsdetails(this)

    }


}