package com.example.myshoppal.Utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MSPediteText (context: Context, attres: AttributeSet): AppCompatEditText(context,attres) {

    init {

        applayfont()
    }

    private fun applayfont(){

        val Boldtypeface : Typeface = Typeface.createFromAsset(
            context.assets,"montserrat-Regular.ttf")

        typeface = Boldtypeface
    }


}