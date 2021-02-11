package com.example.myshoppal.ui.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myshoppal.R
import kotlinx.android.synthetic.main.progress_dialog.*


open class BaseFragment : Fragment() {

    lateinit var mprogressdaialog :Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    // function to show progress dialog...
    fun showprogressdialog (text:String){

        mprogressdaialog = Dialog(requireActivity())
        mprogressdaialog.setContentView(R.layout.progress_dialog)
        mprogressdaialog.setCancelable(false)
        mprogressdaialog.setCanceledOnTouchOutside(false)
        mprogressdaialog.tv_dialog.text = text
        mprogressdaialog.show()
    }

    fun hideprogressdialog (){

        mprogressdaialog.dismiss()
    }

}