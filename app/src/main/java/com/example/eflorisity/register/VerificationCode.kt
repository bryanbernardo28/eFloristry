package com.example.eflorisity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.eflorisity.R
import com.example.eflorisity.login.LoginActivity
import com.example.eflorisity.login.LoginLoadingDialog
import com.example.eflorisity.login.data.Code
import com.example.eflorisity.login.data.Email
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class VerificationCode : AppCompatActivity() {
    private lateinit var tieVerificationCode: TextInputEditText
    private lateinit var tilVerificationCode: TextInputLayout
    private lateinit var btnSubmit: Button

    //ViewModel
    lateinit var viewModel: RegisterViewModel

    //Loading Dialog
    private lateinit var loadingDialog : LoginLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val description = getString(R.string.label_loading_register)
        loadingDialog = LoginLoadingDialog(description,this)
        initViewModel()

        tieVerificationCode = findViewById(R.id.tie_verification_code_id)
        tilVerificationCode = findViewById(R.id.til_verification_code_id)
        btnSubmit = findViewById(R.id.btn_verification_code_id)
        btnSubmit.setOnClickListener {
            loadingDialog.startLoading()
            submit()
        }
    }

    fun submit(){
        val codeText = tieVerificationCode.text.toString()
        if (codeText.isNullOrEmpty() || codeText.isNullOrBlank()){
            tilVerificationCode.error = "Verification code field is required."
            loadingDialog.dismissLoading()
        }
        else{
            tilVerificationCode.clearError()
            val email = intent.getStringExtra("email")
            val code = Code(code = codeText,email = email.toString())
            viewModel.submitVerificationCode(code)
        }


    }

    fun TextInputLayout.clearError() {
        isErrorEnabled = false
        error = null
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        viewModel.getVerificationCodeData().observe(this, {
            if (it != null){
                val goToVerificationCodeActivity = Intent(this, LoginActivity::class.java)
                if (it.success == true){
                    if (it.is_verified == true){
                        goToVerificationCodeActivity.putExtra("fromActivity","verificationCode")
                        goToVerificationCodeActivity.putExtra("isVerified",true)
                        startActivity(goToVerificationCodeActivity)
                        finish()
                    }
                    else{
                        if (it.code_success){
                            goToVerificationCodeActivity.putExtra("fromActivity","verificationCode")
                            startActivity(goToVerificationCodeActivity)
                            finish()
                        }
                        else{
                            tieVerificationCode.text?.clear()
                            val toast = Toast.makeText(this,"Verification failed, verify your code.", Toast.LENGTH_SHORT)
                            toast.setGravity(Gravity.CENTER, 0, 0)
                            toast.show()
                        }
                    }
                }

            }
            else{
                Toast.makeText(this,"Verification Failed.", Toast.LENGTH_LONG).show()
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