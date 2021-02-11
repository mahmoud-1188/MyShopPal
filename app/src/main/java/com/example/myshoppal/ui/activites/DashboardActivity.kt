package com.example.myshoppal.ui.activites

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.myshoppal.R
import com.example.myshoppal.ui.fragments.DashboardFragment
import com.example.myshoppal.ui.fragments.OrderFragment
import com.example.myshoppal.ui.fragments.ProductsFragment
import com.example.myshoppal.ui.fragments.SoldProductsFragment
import kotlinx.android.synthetic.main.activity_dashboard.*


class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


     /*   val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_products, R.id.navigation_dashboard, R.id.navigation_order
            )
        )*/


        val transactionfrgm = supportFragmentManager.beginTransaction()

        if (intent.getIntExtra("add product", 0)==1) {

            transactionfrgm.replace(R.id.nav_host_fragment, ProductsFragment())

        }else{
            transactionfrgm.replace(R.id.nav_host_fragment, DashboardFragment())

        }
        transactionfrgm.commit()



        nav_view.setOnNavigationItemSelectedListener { item ->


                var selectedFragment: Fragment? = null
                when (item.itemId) {
                    R.id.navigation_order -> selectedFragment = OrderFragment()
                    R.id.navigation_dashboard -> selectedFragment = DashboardFragment()
                    R.id.navigation_products -> selectedFragment = ProductsFragment()
                    R.id.navigation_sold -> selectedFragment = SoldProductsFragment()

                }
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                if (selectedFragment != null) {
                    transaction.replace(R.id.nav_host_fragment, selectedFragment)
                }
                transaction.commit()
                true
            }

        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this, R.drawable.toolbar_background
            )
        )


      /*  setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (intent.getIntExtra("add product", 0)==1) {

            val mainFragment: ProductsFragment = ProductsFragment()
            supportFragmentManager.beginTransaction().replace(R.id.frm_container, mainFragment)
                .commit()
        }*/

        }



    override fun onBackPressed() {

        doubleBackToExit()
    }
}