package com.example.eflorisity.ui.home.data

import org.json.JSONObject

//data class Products(val products:List<ProductDetails>)
data class ProductDetails(val name:String?,val description:String?,val price:String?,val discount:String?,val photo_url:String?)
data class ProductFechError(val errorCode:String)
