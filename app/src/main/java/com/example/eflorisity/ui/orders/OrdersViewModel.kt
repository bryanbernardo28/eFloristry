package com.example.eflorisity.ui.orders

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eflorisity.retrofit.RetroInstance
import com.example.eflorisity.ui.orders.data.OrdersWithProduct
import com.example.eflorisity.ui.orders.retrofit.OrdersRetroServiceInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersViewModel : ViewModel() {
    var ordersWithProductLiveData: MutableLiveData<ArrayList<OrdersWithProduct>> = MutableLiveData()
    var getErrorCodeLiveData: MutableLiveData<Int> = MutableLiveData()

    fun getOrdersWithProductObservable(): MutableLiveData<ArrayList<OrdersWithProduct>>{
        return ordersWithProductLiveData
    }



    fun getErrorCodeObservable(): MutableLiveData<Int>{
        return getErrorCodeLiveData
    }

    fun getOrders(member_id:String,status:Int){
        val retroInstance = RetroInstance.getInstance()
        val retroService = retroInstance.create(OrdersRetroServiceInterface::class.java)
        val call = retroService.getOrders(member_id,status)
        call.enqueue(object:Callback<ArrayList<OrdersWithProduct>>{
            override fun onResponse(
                call: Call<ArrayList<OrdersWithProduct>>,
                response: Response<ArrayList<OrdersWithProduct>>
            ) {
                if (response.isSuccessful){
                    Log.d("orders-result","Orders isSuccessful")
                    ordersWithProductLiveData.postValue(response.body())
                }
                else{
                    Log.d("orders-result","Orders isNotSuccessful,${response.code()}")
                    getErrorCodeLiveData.postValue(response.code())
                }
            }

            override fun onFailure(call: Call<ArrayList<OrdersWithProduct>>, t: Throwable) {
                Log.d("orders-result","Orders isNotSuccessful,${t.message.toString()}")
                ordersWithProductLiveData.postValue(null)
            }

        })
    }

}