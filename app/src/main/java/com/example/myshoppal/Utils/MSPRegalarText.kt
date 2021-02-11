package com.example.myshoppal.Utils


import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MSPRegalarText (context:Context, attres:AttributeSet): AppCompatTextView(context,attres) {



    init {

        applayfont()
    }

    private fun applayfont(){

        val Boldtypeface :Typeface = Typeface.createFromAsset(
            context.assets,"montserrat-Regular.ttf")

        typeface = Boldtypeface
    }


}