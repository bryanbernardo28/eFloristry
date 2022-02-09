package com.example.eflorisity.ui.cart.recyclerview_adapter

import android.widget.ImageButton

interface CartClickListener {
    fun onCartItemClicked(newQty:Int,productId:String,position:Int,isIncrement:Int)
}