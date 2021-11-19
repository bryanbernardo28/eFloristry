package com.example.eflorisity.ui.cart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eflorisity.ui.cart.data.Cart
import com.example.eflorisity.ui.cart.data.DeleteItemFromCartResponse
import com.example.eflorisity.ui.cart.retrofit.CartRetroServiceInterface
import com.example.eflorisity.retrofit.RetroInstance
import com.example.eflorisity.ui.home.data.ProductCartListNotIn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel : ViewModel() {
    var myCartLiveData: MutableLiveData<ArrayList<Cart>> = MutableLiveData()
    var errorMyCart : MutableLiveData<Int> = MutableLiveData()
    var deleteFromCartResponse : MutableLiveData<DeleteItemFromCartResponse> = MutableLiveData()

    fun getMyCartObservable(): MutableLiveData<ArrayList<Cart>> {
        return myCartLiveData
    }

    fun getDeleteFromCartResponseObservable(): MutableLiveData<DeleteItemFromCartResponse>{
        return deleteFromCartResponse
    }

    fun getErrorMyCartObservable(): MutableLiveData<Int>{
        return errorMyCart
    }


    fun getMyCartFun(id:String){
        val retroInstance = RetroInstance.getInstance()
        val retroService = retroInstance.create(CartRetroServiceInterface::class.java)
        val call = retroService.getMyCart(id)
        call.enqueue(object:Callback<ArrayList<Cart>>{
            override fun onResponse(call: Call<ArrayList<Cart>>, response: Response<ArrayList<Cart>>) {
                if (response.isSuccessful){
                    Log.d("cart-result","CartViewModel isSuccessful")
                    myCartLiveData.postValue(response.body())
                    errorMyCart.postValue(response.code())
                }
                else{
                    Log.d("cart-result","CartViewModel isNotSuccessful, ${response.code()}")
                    errorMyCart.postValue(response.code())
                }
            }

            override fun onFailure(call: Call<ArrayList<Cart>>, t: Throwable) {
                myCartLiveData.postValue(null)
                Log.d("cart-result","CartViewModel: ${t.message.toString()}")
            }

        })
    }

    fun getMyCartNotIn(productCartListNotIn: ProductCartListNotIn){
        val retroInstance = RetroInstance.getInstance()
        val retroService = retroInstance.create(CartRetroServiceInterface::class.java)
        val call = retroService.getMyCartNotIn(productCartListNotIn)
        call.enqueue(object:Callback<ArrayList<Cart>>{
            override fun onResponse(call: Call<ArrayList<Cart>>, response: Response<ArrayList<Cart>>) {
                if (response.isSuccessful){
                    Log.d("cart-result","CartViewModel isSuccessful")
                    myCartLiveData.postValue(response.body())
                    errorMyCart.postValue(response.code())
                }
                else{
                    Log.d("cart-result","CartViewModel isNotSuccessful, ${response.message()}")
                    errorMyCart.postValue(response.code())
                }
            }

            override fun onFailure(call: Call<ArrayList<Cart>>, t: Throwable) {
                myCartLiveData.postValue(null)
                Log.d("cart-result","CartViewModel: ${t.message.toString()}")
            }

        })
    }

    fun deleteItemFromCartFun(member_id:String,id:String){
        val retroInstance = RetroInstance.getInstance()
        val retroService = retroInstance.create(CartRetroServiceInterface::class.java)
        val call = retroService.deleteFromMyCart(member_id,id)
        call.enqueue(object:Callback<DeleteItemFromCartResponse>{
            override fun onResponse(
                call: Call<DeleteItemFromCartResponse>,
                response: Response<DeleteItemFromCartResponse>
            ) {
                Log.d("cart-result","CartViewModel isSuccessful")
                deleteFromCartResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<DeleteItemFromCartResponse>, t: Throwable) {
                deleteFromCartResponse.postValue(null)
                Log.d("cart-result","CartViewModel: ${t.message.toString()}")
            }

        })
    }
}