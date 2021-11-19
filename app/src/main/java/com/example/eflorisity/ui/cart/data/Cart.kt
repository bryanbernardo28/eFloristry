package com.example.eflorisity.ui.cart.data

import android.os.Parcelable
import com.example.eflorisity.ui.home.data.ProductDetails
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart(
    val id:String,
    val member_id:String?,
    val quantity:String,
    val product:ProductDetails
): Parcelable

data class DeleteItemFromCartResponse(
    val success:Boolean,
)
