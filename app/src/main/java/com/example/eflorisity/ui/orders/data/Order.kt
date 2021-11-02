package com.example.eflorisity.ui.orders.data


data class OrdersWithProduct(
    val id:String?,
    val member_id:String?,
    val order_number:String?,
    val contact_number:String?,
    val email:String?,
    val region:String?,
    val state:String?,
    val city:String?,
    val barangay:String?,
    val postal_code:String?,
    val address:String?,
    val payment_method:String?,
    val notes:String?,
    val total_amount:String?,
    val status:Int?,
    val product: Product?,
)

data class Products(
    val id:String?,
    val order_id:String?,
    val product_id: String?,
    val price:String?,
    val quantity:Int?
)

data class Product(
    val id:String?,
    val supplier_id:String?,
    val name:String?,
    val description:String?,
    val price:String?,
    val discount:String?,
    val photo_url:String?,
    val is_discounted:String?,
    val quantity:String?
)

data class ToPay(
    val member_id:String,
    val product_id:String,
)
