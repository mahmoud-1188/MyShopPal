package com.example.myshoppal.Utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class MSPbutton (context: Context, attres: AttributeSet): AppCompatButton(context,attres) {

    init {

        applayfont()
    }

    private fun applayfont(){

        val Boldtypeface : Typeface = Typeface.createFromAsset(
            context.assets,"montserrat-Bold.ttf")

        typeface = Boldtypeface
    }


}