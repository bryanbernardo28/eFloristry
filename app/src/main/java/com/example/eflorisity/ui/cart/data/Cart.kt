package com.example.eflorisity.ui.cart

import java.util.*


data class Cart(
        val product_id:String,
        val product_name:String,
        val product_price:String?,
        val product_quantity:Int,
        val product_discount:String?,
        val product_is_discounted:Int,
        val product_photo_url:String?
    )
