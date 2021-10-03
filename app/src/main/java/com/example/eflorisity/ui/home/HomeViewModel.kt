package com.example.eflorisity.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eflorisity.login.retrofit.HomeRetroInstance
import com.example.eflorisity.login.retrofit.HomeRetroServiceInterface
import com.example.eflorisity.ui.home.data.ProductDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {
    var productsLiveData: MutableLiveData<List<ProductDetails>> = MutableLiveData()
    var errorProducts : MutableLiveData<Int> = MutableLiveData()

    fun getProductsObservable(): MutableLiveData<List<ProductDetails>> {
        return productsLiveData
    }
    fun getErrorProductsObservable(): MutableLiveData<Int>{
        return errorProducts
    }

    fun getProducts(token:String){
        val retroInstance = HomeRetroInstance.getInstance()
        val retroService = retroInstance.create(HomeRetroServiceInterface::class.java)
        val call = retroService.getProducts(token)
        call.enqueue(object: Callback<List<ProductDetails>> {
            override fun onResponse(
                call: Call<List<ProductDetails>>,
                response: Response<List<ProductDetails>>
            ) {
                if (response.isSuccessful){
                    Log.d("product-result","HomeViewModel isSuccessful")
                    productsLiveData.postValue(response.body())
                }
                else{
                    Log.d("product-result","isNotSuccessful,${response.code()}")
                    errorProducts.postValue(response.code())
                }
            }

            override fun onFailure(call: Call<List<ProductDetails>>, t: Throwable) {
                productsLiveData.postValue(null)
                Log.d("product-result","HomeViewModel: ${t.message.toString()}")
            }

        })

    }

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text

}