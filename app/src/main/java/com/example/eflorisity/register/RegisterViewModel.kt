package com.example.eflorisity.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eflorisity.login.data.*
import com.example.eflorisity.register.retrofit.RegisterRetroServiceInterface
import com.example.eflorisity.retrofit.RetroInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel:ViewModel() {
    var registerLiveData: MutableLiveData<RegisterMemberResponse?> = MutableLiveData()
    fun getRegisteredMemberObservable(): MutableLiveData<RegisterMemberResponse?> {
        return registerLiveData
    }

    var resendVerificationLiveData: MutableLiveData<EmailResponse> = MutableLiveData()
    fun getResendVerificationData():MutableLiveData<EmailResponse> {
        return resendVerificationLiveData
    }

    fun registerMember(memberDetails: MemberDetails){
        val retroInstance = RetroInstance.getInstance()
        val retroService = retroInstance.create(RegisterRetroServiceInterface::class.java)
        val call = retroService.registerMember(memberDetails)
        call.enqueue(object: Callback<RegisterMemberResponse?> {
            override fun onResponse(
                call: Call<RegisterMemberResponse?>,
                response: Response<RegisterMemberResponse?>
            ) {
                if (response.isSuccessful){
                    registerLiveData.postValue(response.body())
                    Log.d("register-result","isSuccessful")
                }
                else{
                    Log.d("register-result","Error")
                    registerLiveData.postValue(null)
//                    val error = JSONObject(response.errorBody()!!.string())
//                    val errorMessageString = getError(error.getJSONObject("errors"))
//                    val memberResponse = MemberResponse(false,null,null,errorMessageString)
//                    registerLiveData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<RegisterMemberResponse?>, t: Throwable) {
                registerLiveData.postValue(null)
                Log.d("register-result","onFailure: ${t.message.toString()}")
            }

        })
    }

    fun resendVerificationCode(email:Email){
        val retroInstance = RetroInstance.getInstance()
        val retroService = retroInstance.create(RegisterRetroServiceInterface::class.java)
        val call = retroService.resendVerificationCode(email)
        call.enqueue(object:Callback<EmailResponse>{
            override fun onResponse(call: Call<EmailResponse>, response: Response<EmailResponse>) {
                if (response.isSuccessful){
                    resendVerificationLiveData.postValue(response.body())
                    Log.d("register-result","isSuccessful")
                }
                else{
                    Log.d("register-result","Error Resending Verification")
                    resendVerificationLiveData.postValue(null)
                }
            }

            override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                resendVerificationLiveData.postValue(null)
            }

        })
    }
}