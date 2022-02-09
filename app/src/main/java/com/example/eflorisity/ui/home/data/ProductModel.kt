package com.example.eflorisity.ui.home.data

import android.os.Parcelable
import com.example.eflorisity.ui.orders.data.Product
import kotlinx.android.parcel.Parcelize

//data class Products(val products:List<ProductDetails>)
@Parcelize
data class ProductDetails(
        val id:String?,
        val supplier_id:String?,
        val name:String?,
        val description:String?,
        val price:String?,
        val discount:String?,
        val weight:String?,
        val photo_url:String?,
        val is_discounted:Int?,
        val stocks:String?
        ): Parcelable

data class ProductCart(
        val member_id:String,
        val product_id:String,
        val quantity:Int,
        val isAdd:Int?
)

data class ProductCartNotIn(
        val product_id:String,
        var quantity:Int
)

data class ProductCartListNotIn(
        val carts:ArrayList<ProductCartNotIn>
)

data class ProductCartResponse(
        val member_id:String?,
        val product_id: String?,
        val success:Boolean?,
        val errorString:String?,
        val isAdd:Int?
)

data class ProductFechError(val errorCode:String)
