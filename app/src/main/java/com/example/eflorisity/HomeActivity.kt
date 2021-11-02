package com.example.eflorisity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.eflorisity.databinding.ActivityHomeBinding
import com.example.eflorisity.login.LoginActivity

class HomeActivity : AppCompatActivity(){

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView


        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,R.id.navigation_cart, R.id.navigation_orders, R.id.navigation_account
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setOnNavigationItemReselectedListener {

        }

        navView.setOnNavigationItemSelectedListener{
            checkLoggedIn(it)
            false
        }
    }

    fun checkLoggedIn(it:MenuItem){
        val memberDetailsSp = SharedPref(this,getString(R.string.spMemberDetails))
        val isLoggedIn = memberDetailsSp.getValueBoolean(getString(R.string.spKeyIsLoggedIn),false)
        if (!isLoggedIn && it.itemId == R.id.navigation_account){
            intentToLoginActivity()
        }
        else if (!isLoggedIn && it.itemId == R.id.navigation_cart){
            intentToLoginActivity()
        }
        else if (!isLoggedIn && it.itemId == R.id.navigation_orders){
            intentToLoginActivity()
        }
        else{
            navigate(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        for (fragment in supportFragmentManager.primaryNavigationFragment!!.childFragmentManager.fragments) {
//            fragment.onActivityResult(requestCode, resultCode, data)
//        }
        if (resultCode != Activity.RESULT_CANCELED){
            when(requestCode){
                getString(R.string.checkout_request_code).toInt() -> {
                    Log.d("homeactivity-result","onActivityResult Called")
                    Navigation.findNavController(this,R.id.nav_host_fragment_activity_home).navigate(R.id.navigation_orders)
//                    it.isChecked = true
                }
            }
        }
    }

    fun intentToLoginActivity(){
        val goToLoginActivity = Intent(this, LoginActivity::class.java)
        startActivity(goToLoginActivity)
        finish()
    }

    fun navigate(it:MenuItem){
        Navigation.findNavController(this,R.id.nav_host_fragment_activity_home).popBackStack()
        Navigation.findNavController(this,R.id.nav_host_fragment_activity_home).navigate(it.itemId)
        it.isChecked = true

    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        finish()
//    }


}