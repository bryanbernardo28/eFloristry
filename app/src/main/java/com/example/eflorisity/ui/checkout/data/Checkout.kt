package com.example.eflorisity.ui.checkout.data

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class Checkout(
    val member_id:String,
    val contact_number:String,
    val email_address:String,
    val region:String,
    val state:String,
    val city:String,
    val barangay:String,
    val postal_code:String,
    val address:String,
    val payment_method:String,
    val notes:String,
    val total_amount:String,
    val shipping_fee:String,
    val status:Int,
    val paypalOrderID:String?,
    val paypalPayerID:String?,
    val carts: ArrayList<ProductCheckout>
)

data class ProductCheckout(
    val id:String?,
    val price:String?,
    val weight:String?,
    val qty:Int?
)

data class CheckoutResponse(
    val success:Boolean
)

data class GetShippingFee(
    val carts: ArrayList<ProductCheckout>,
    val state: String
)
data class ShippingFeeResponse(
    val rate:String
)
