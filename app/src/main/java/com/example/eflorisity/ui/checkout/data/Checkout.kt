package com.example.eflorisity.ui.checkout.data

import com.example.eflorisity.login.data.MemberDetails
import com.example.eflorisity.ui.home.data.ProductDetails
import org.json.JSONArray
import org.json.JSONObject

data class Checkout(
    val member_id:String,
    val total_amount:String,
    val products: ArrayList<ProductCheckout>
)

data class ProductCheckout(
    val product_id:String?,
    val quantity:Int?
)

data class CheckoutResponse(
    val success:Boolean
)
