package com.example.eflorisity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.example.eflorisity.login.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var memberDetailsSp = SharedPref(this,getString(R.string.spMemberDetails))
        if (memberDetailsSp.getValueBoolean(getString(R.string.spKeyIsLoggedIn),false) && !TextUtils.isEmpty(memberDetailsSp.getValueString(getString(R.string.spKeyToken)))){
            val goToHomeActivity = Intent(this, HomeActivity::class.java)
            startActivity(goToHomeActivity)
        }
        else{
            val goToLoginActivity = Intent(this, LoginActivity::class.java)
            startActivity(goToLoginActivity)
        }
        finish()
    }
}