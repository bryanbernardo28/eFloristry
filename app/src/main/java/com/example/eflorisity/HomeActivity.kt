package com.example.eflorisity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.eflorisity.databinding.ActivityHomeBinding
import com.example.eflorisity.login.LoginActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
//        memberDetailsSp = SharedPref(requireContext(),getString(R.string.spMemberDetails))
//        val isLoggedIn = memberDetailsSp.getValueBoolean(getString(R.string.spKeyIsLoggedIn),false)
//        if (!isLoggedIn){
//            val goToLoginActivity = Intent(activity, LoginActivity::class.java)
//            startActivity(goToLoginActivity)
//        }
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


//        navView.setOnNavigationItemSelectedListener{
//
//            when(it.itemId){
//                R.id.navigation_account -> {
//                    Toast.makeText(this,"Account Selected",Toast.LENGTH_LONG).show()
//
//                    navView.selectedItemId = R.id.navigation_account
//                    navView.performClick()
//                }
//                R.id.navigation_cart -> {
//                    Toast.makeText(this,"Cart Selected",Toast.LENGTH_LONG).show()
////                    navView.selectedItemId = R.id.navigation_cart
//                }
//            }
//            true
//        }
    }
}