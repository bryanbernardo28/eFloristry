package com.example.eflorisity.login

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.eflorisity.HomeActivity
import com.example.eflorisity.R
import com.example.eflorisity.register.RegisterActivity
import com.example.eflorisity.SharedPref
import com.example.eflorisity.login.data.MemberLoginDetails
import com.example.eflorisity.login.data.MemberResponse
import com.example.eflorisity.register.ResendVerificationActivity
import okhttp3.internal.format

class LoginActivity : AppCompatActivity() {
    private lateinit var plaintextEmail : EditText
    private lateinit var plaintextPassword : EditText
    private lateinit var loginButton : Button
    private lateinit var errorMessageTextView: TextView
    private lateinit var loadingDialog : LoginLoadingDialog
    lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        plaintextEmail = findViewById(R.id.et_login_email_id)
        plaintextPassword = findViewById(R.id.et_login_password_id)
        loginButton = findViewById(R.id.btn_login_login_id)
        errorMessageTextView = findViewById(R.id.tv_login_errormessage_id)


        val description = getString(R.string.label_loading_login)
        loadingDialog = LoginLoadingDialog(description,this)
        initViewModel()
        loginButton.setOnClickListener {
            loadingDialog.startLoading()
            submitLogin()
        }

        val fromActivity = intent.getStringExtra("fromActivity")

        if (!fromActivity.isNullOrEmpty()){
            Log.d("login-result","From Activity: ${fromActivity.toString()}")
            var message: String? = null
            errorMessageTextView.setTextColor(Color.BLACK)
            if (fromActivity.equals("register")){
                message = "Register Successful.\nWe sent verification to your email."
                errorMessageTextView.visibility = View.VISIBLE
            }
            else if(fromActivity.equals("resendVerification")){
                if (intent.getBooleanExtra("isVerified",false)){
                    message = "Your account is already verified."
                }
                else{
                    message = "Re-send verification to email successful."
                }
                errorMessageTextView.visibility = View.VISIBLE
            }
            errorMessageTextView.text = message
        }
    }

    private fun submitLogin(){
        var email = plaintextEmail.text.toString()
        var password = plaintextPassword.text.toString()
        val member = MemberLoginDetails(email,password,"Android")
        viewModel.loginMember(member)
    }

    fun goToRegister(view:View){
        val goToRegisterActivity = Intent(this, RegisterActivity::class.java)
        startActivity(goToRegisterActivity)
        finish()
    }

    fun goToResendverification(view:View){
        val goToResendVerificationActivity = Intent(this, ResendVerificationActivity::class.java)
        startActivity(goToResendVerificationActivity)
        finish()
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.getLoginMemberObservable().observe(this, Observer<MemberResponse?>{
            if (it != null){
                handleResponse(it)
                Log.d("login-result",it.toString())
            }
            else{
                Toast.makeText(this,"Failed to Log In.",Toast.LENGTH_LONG).show()
                Log.d("login-result",it.toString())
            }
            loadingDialog.dismissLoading()
        })
    }

    fun handleResponse(it:MemberResponse){
        if(it.success == true){
            if (errorMessageTextView.isVisible){
                errorMessageTextView.text = String()
                errorMessageTextView.visibility = View.GONE
            }
            getMemberDetails(it)
        }
        else{
            errorMessageTextView.setTextColor(Color.parseColor("#FF2C2C"))
            if (!errorMessageTextView.isVisible){
                errorMessageTextView.visibility = View.VISIBLE
            }
            errorMessageTextView.setText(it.errors.toString())
        }
        loadingDialog.dismissLoading()
    }


    fun getMemberDetails(memberDetails:MemberResponse){
        var memberDetailsSp = SharedPref(this,getString(R.string.spMemberDetails))
        memberDetailsSp.save(getString(R.string.spKeyId),memberDetails.member?.id)
        memberDetailsSp.save(getString(R.string.spKeyFirstName), memberDetails.member?.first_name)
        memberDetailsSp.save(getString(R.string.spKeyLastName),memberDetails.member?.last_name)
        memberDetailsSp.save(getString(R.string.spKeyMiddleName),memberDetails.member?.middle_name)
        memberDetailsSp.save(getString(R.string.spKeyEmail),memberDetails.member?.email)
        memberDetailsSp.save(getString(R.string.spKeyContactNumber),memberDetails.member?.contact_number)
        memberDetailsSp.save(getString(R.string.spKeyPhotoUrl),memberDetails.member?.photo_url)
        memberDetailsSp.save(getString(R.string.spKeyToken),memberDetails.token)
        memberDetailsSp.save(getString(R.string.spKeyIsLoggedIn),true)
//            memberDetailsSp.save(getString(R.string.spKeyRole),memberDetailsJson.getString("role"))

        val goToHomeActivity = Intent(this, HomeActivity::class.java)
        startActivity(goToHomeActivity)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val goToHomeActivity = Intent(this, HomeActivity::class.java)
        startActivity(goToHomeActivity)
        finish()
    }
}