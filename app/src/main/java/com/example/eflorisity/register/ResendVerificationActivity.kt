package com.example.eflorisity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.eflorisity.HomeActivity
import com.example.eflorisity.R
import com.example.eflorisity.login.LoginActivity
import com.example.eflorisity.login.LoginLoadingDialog
import com.example.eflorisity.login.data.Email
import com.google.android.material.textfield.TextInputEditText

class ResendVerificationActivity : AppCompatActivity() {
    private lateinit var tieEmail:TextInputEditText
    private lateinit var btnResend:Button

    //ViewModel
    lateinit var viewModel: RegisterViewModel

    //Loading Dialog
    private lateinit var loadingDialog : LoginLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resend_verification)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val description = getString(R.string.label_loading_resend)
        loadingDialog = LoginLoadingDialog(description,this)
        initViewModel()

        tieEmail = findViewById(R.id.tie_resend_email_id)
        btnResend = findViewById(R.id.btn_resend_resend_id)
        btnResend.setOnClickListener {
            loadingDialog.startLoading()
            reSend()
        }

    }

    fun reSend(){
        val emailText = tieEmail.text.toString()
        val email = Email(email = emailText)
        viewModel.resendVerificationCode(email)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        viewModel.getResendVerificationData().observe(this, androidx.lifecycle.Observer {
            if (it != null){
                val emailText = tieEmail.text.toString()

                if (it.success == true){
                    if (it.is_verified == true){
                        val goToLoginActivity = Intent(this, LoginActivity::class.java)
                        goToLoginActivity.putExtra("fromActivity","resendVerification")
                        goToLoginActivity.putExtra("isVerified",true)
                        startActivity(goToLoginActivity)
                    }
                    else{
//                        Toast.makeText(this,"Resend Verification Successful", Toast.LENGTH_LONG).show()
                        val goToVerificationCodeActivity = Intent(this, VerificationCode::class.java)
                        goToVerificationCodeActivity.putExtra("email",emailText)
                        startActivity(goToVerificationCodeActivity)
                    }
                }
                finish()
            }
            else{
                Toast.makeText(this,"Failed to resend verification.", Toast.LENGTH_LONG).show()
            }
            loadingDialog.dismissLoading()
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val goToLoginActivity = Intent(this, LoginActivity::class.java)
        startActivity(goToLoginActivity)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}