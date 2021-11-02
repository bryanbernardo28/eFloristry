package com.example.eflorisity.ui.checkout.data

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
    val status:Int,
    val paypalOrderID:String?,
    val paypalPayerID:String?,
    val carts: ArrayList<ProductCheckout>
)

data class ProductCheckout(
    val id:String?,
    val price:String?,
    val qty:Int?
)

data class CheckoutResponse(
    val success:Boolean
)

