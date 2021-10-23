package com.example.eflorisity.ui.home.data

import android.os.Parcelable
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
        val photo_url:String?,
        val is_discounted:Int?
        ): Parcelable

data class ProductCart(
        val member_id:String,
        val product_id:String,
)

data class ProductCartResponse(
        val member_id:String?,
        val product_id: String?,
        val success:Boolean?,
        val errorString:String?
)

data class ProductFechError(val errorCode:String)
