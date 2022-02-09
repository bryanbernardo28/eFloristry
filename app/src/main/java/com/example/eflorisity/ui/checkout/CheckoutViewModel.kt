package com.example.eflorisity.ui.checkout

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eflorisity.ui.checkout.data.Checkout
import com.example.eflorisity.ui.checkout.data.CheckoutResponse
import com.example.eflorisity.ui.checkout.retrofit.CheckoutRetroServiceInterface
import com.example.eflorisity.retrofit.RetroInstance
import com.example.eflorisity.ui.checkout.data.GetShippingFee
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutViewModel:ViewModel() {
    var checkoutLiveData: MutableLiveData<CheckoutResponse?> = MutableLiveData()
    var addressLiveData: MutableLiveData<JsonObject?> = MutableLiveData()
    var shippingFeeLiveData:MutableLiveData<String?> = MutableLiveData()

    fun addOrderProductsObservable(): MutableLiveData<CheckoutResponse?> {
        return checkoutLiveData
    }

    fun getStatesObservable(): MutableLiveData<JsonObject?> {
        return addressLiveData
    }

    fun getShippingFeeObservable(): MutableLiveData<String?> {
        return shippingFeeLiveData
    }

    fun addOrderProducts(orderProducts:Checkout,bearer:String){
        val retroInstance = RetroInstance.getInstance()
        val retroService = retroInstance.create(CheckoutRetroServiceInterface::class.java)
        val jsonString = Gson().toJson(orderProducts)
        val call = retroService.addOrderProducts(token = bearer,params = orderProducts)
        call.enqueue(object: Callback<CheckoutResponse?> {
            override fun onResponse(
                call: Call<CheckoutResponse?>,
                response: Response<CheckoutResponse?>
            ) {
                if (response.isSuccessful){
                    Log.d("order-product-result","isSuccessful - Body : ${response.body()} - Message: ${response.message()} ")
                    checkoutLiveData.postValue(response.body())
                }
                else{
                    val error = JSONObject(response.errorBody()!!.string())
                    Log.d("order-product-result","Response: $error")

//                    val checkoutResponse = CheckoutResponse(List<error>)
//                    checkoutLiveData.postValue(checkoutResponse)
                }
            }

            override fun onFailure(call: Call<CheckoutResponse?>, t: Throwable) {
                Log.d("order-product-result","Error: ${t.message}")
            }

        })
    }

    fun getStates(){
        val retroInstance = RetroInstance.getInstance()
        val retroService = retroInstance.create(CheckoutRetroServiceInterface::class.java)
        val call = retroService.getStates()
        call.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>
            ) {
                if (response.isSuccessful){
                    Log.d("address-result","isSuccessful - Body : ${response.body()} - Message: ${response.message()} ")
                    addressLiveData.postValue(response.body())
                }
                else{
                    val error = JSONObject(response.errorBody()!!.string())
                    Log.d("address-result","Response: $error")
                    addressLiveData.postValue(null)
//                    val checkoutResponse = CheckoutResponse(List<error>)
//                    checkoutLiveData.postValue(checkoutResponse)
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                Log.d("address-result","Error: ${t.message.toString()}")
                addressLiveData.postValue(null)
            }
        })
    }

    fun getShippingFee(getShippingFee:GetShippingFee){
        val retroInstance = RetroInstance.getInstance()
        val retroService = retroInstance.create(CheckoutRetroServiceInterface::class.java)
        val call = retroService.getShippingFee(params = getShippingFee)
        call.enqueue(object:Callback<String?>{
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful){
                    Log.d("sf-result","isSuccessful - Body : ${response.body()} - Message: ${response.message()} ")
                    shippingFeeLiveData.postValue(response.body())
                }
                else{
                    val error = JSONObject(response.errorBody()!!.string())
                    Log.d("sf-result","Response: $error")
                    shippingFeeLiveData.postValue(null)
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.d("sf-result","Error: ${t.message}")
            }

        })
    }
}