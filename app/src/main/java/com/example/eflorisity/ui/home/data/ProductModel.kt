package com.example.eflorisity.ui.home.data

import org.json.JSONObject

//data class Products(val products:List<ProductDetails>)
data class ProductDetails(
        val id:String?,
        val supplier_id:String?,
        val name:String?,
        val description:String?,
        val price:String?,
        val discount:String?,
        val photo_url:String?,
        val is_discounted:Int?
    )

data class ProductFechError(val errorCode:String)
