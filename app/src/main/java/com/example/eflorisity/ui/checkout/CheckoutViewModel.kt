package com.example.eflorisity.ui.checkout

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eflorisity.ui.checkout.data.Checkout
import com.example.eflorisity.ui.checkout.data.CheckoutResponse
import com.example.eflorisity.ui.checkout.retrofit.CheckoutRetroServiceInterface
import com.example.eflorisity.retrofit.RetroInstance
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutViewModel:ViewModel() {
    var checkoutLiveData: MutableLiveData<CheckoutResponse?> = MutableLiveData()

    fun addOrderProductsObservable(): MutableLiveData<CheckoutResponse?> {
        return checkoutLiveData
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
                    Log.d("order-product-result","Response: ${error}")
//
//                    val checkoutResponse = CheckoutResponse(List<error>)
//                    checkoutLiveData.postValue(checkoutResponse)
                }
            }

            override fun onFailure(call: Call<CheckoutResponse?>, t: Throwable) {
                Log.d("order-product-result","Error: ${t.message}")
            }

        })
    }
}