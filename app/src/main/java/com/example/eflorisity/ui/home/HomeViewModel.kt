package com.example.eflorisity.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eflorisity.login.retrofit.HomeRetroServiceInterface
import com.example.eflorisity.ui.home.data.ProductCart
import com.example.eflorisity.ui.home.data.ProductCartResponse
import com.example.eflorisity.ui.home.data.ProductDetails
import com.example.eflorisity.retrofit.RetroInstance
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class HomeViewModel: ViewModel() {
    var productsLiveData: MutableLiveData<List<ProductDetails>> = MutableLiveData()
    var errorProducts : MutableLiveData<Int> = MutableLiveData()
    var addToCartReponse: MutableLiveData<ProductCartResponse> = MutableLiveData()

    fun getAddToCartResponse(): MutableLiveData<ProductCartResponse> {
        return addToCartReponse
    }
    fun getProductsObservable(): MutableLiveData<List<ProductDetails>> {
        return productsLiveData
    }
    fun getErrorProductsObservable(): MutableLiveData<Int>{
        return errorProducts
    }

    fun getProducts(token:String){
        val retroInstance = RetroInstance.getInstance()
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

    fun addToCart(token:String, productCart:ProductCart){
        val retroInstance = RetroInstance.getInstance()
        val retroService = retroInstance.create(HomeRetroServiceInterface::class.java)
        val call = retroService.addToCart(token,productCart)
        call.enqueue(object:Callback<ProductCartResponse>{
            override fun onResponse(
                call: Call<ProductCartResponse>,
                response: Response<ProductCartResponse>
            ) {
                if (response.isSuccessful){
                    Log.d("product-cart-result","isSuccessful")
                    Log.d("product-cart-result","Body: ${response.body()}")
                    addToCartReponse.postValue(response.body())
                }
                else{
                    val error = JSONObject(response.errorBody()!!.string())
                    Log.d("product-cart-result","Error: $error")
//                    val errorMessageString = getError(error.getJSONObject("errors"))
//                    val memberResponse = ProductCartResponse(false,errorMessageString)
//                    addToCartReponse.postValue(memberResponse)
                }
            }

            override fun onFailure(call: Call<ProductCartResponse>, t: Throwable) {
                addToCartReponse.postValue(null)
                Log.d("product-cart-result",t.message.toString())
            }

        })
    }

    fun getError(jsonObjectErrors: JSONObject) : String{
        val keys = jsonObjectErrors.names()
        var errorString = StringBuilder()
        for (i in 0 until keys.length()) {
            val key = keys.getString(i)
            var value = jsonObjectErrors.getJSONArray(key)
            errorString.append(value[0]).append("\n")
        }
        return errorString.toString()
    }

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text

}