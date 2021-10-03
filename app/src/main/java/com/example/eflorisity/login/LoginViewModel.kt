package com.example.eflorisity.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eflorisity.login.data.MemberLoginDetails
import com.example.eflorisity.login.data.MemberResponse
import com.example.eflorisity.login.retrofit.LoginRetroInstance
import com.example.eflorisity.login.retrofit.LoginRetroServiceInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONObject
import java.lang.StringBuilder


class LoginViewModel: ViewModel() {
    var loginLiveData: MutableLiveData<MemberResponse?> = MutableLiveData()

    fun getLoginMemberObservable(): MutableLiveData<MemberResponse?> {
        return loginLiveData
    }

    fun loginMember(memberLoginDetails: MemberLoginDetails){
        val retroInstance = LoginRetroInstance.getInstance()
        val retroService = retroInstance.create(LoginRetroServiceInterface::class.java)
        val call = retroService.loginMember(memberLoginDetails)
        call.enqueue(object:Callback<MemberResponse?>{
            override fun onResponse(
                call: Call<MemberResponse?>,
                response: Response<MemberResponse?>
            ) {
                if (response.isSuccessful){
                    Log.d("login-result","isSuccessful")
                    loginLiveData.postValue(response.body())
                }
                else{
                    val error = JSONObject(response.errorBody()!!.string())
                    val errorMessageString = getError(error.getJSONObject("errors"))
                    val memberResponse = MemberResponse(false,null,null,errorMessageString)
                    loginLiveData.postValue(memberResponse)
                }
            }

            override fun onFailure(call: Call<MemberResponse?>, t: Throwable) {
                loginLiveData.postValue(null)
                Log.d("login-result",t.message.toString())
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
}