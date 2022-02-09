package com.example.eflorisity.ui.account

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.eflorisity.R
import com.example.eflorisity.SharedPref
import com.squareup.picasso.Picasso

class AccountInformationActivity : AppCompatActivity() {

    private lateinit var tvFirstname:TextView
    private lateinit var tvMiddlename:TextView
    private lateinit var tvLastname:TextView
    private lateinit var tvEmail:TextView
    private lateinit var tvContactNumber:TextView
    private lateinit var tvBirthday:TextView
    private lateinit var ivPhoto:ImageView
    private lateinit var memberDetailsSp:SharedPref


//    private lateinit var accountViewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_information)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        memberDetailsSp = SharedPref(this,getString(R.string.spMemberDetails))

        tvFirstname = findViewById(R.id.tv_accountinfo_firstname_id)
        tvMiddlename = findViewById(R.id.tv_accountinfo_middlename_id)
        tvLastname = findViewById(R.id.tv_accountinfo_lastname_id)
        tvEmail = findViewById(R.id.tv_accountinfo_email_id)
        tvContactNumber = findViewById(R.id.tv_accountinfo_contactnumber_id)
        tvBirthday = findViewById(R.id.tv_accountinfo_birthday_id)
        ivPhoto = findViewById(R.id.iv_accountinfo_photo)

        val firstname = memberDetailsSp.getValueString(getString(R.string.spKeyFirstName))
        val middlename = memberDetailsSp.getValueString(getString(R.string.spKeyMiddleName))
        val lastname = memberDetailsSp.getValueString(getString(R.string.spKeyLastName))
        val email = memberDetailsSp.getValueString(getString(R.string.spKeyEmail))
        val contactNumber = memberDetailsSp.getValueString(getString(R.string.spKeyContactNumber))
        val birthday = memberDetailsSp.getValueString(getString(R.string.spKeyBirthday))
        val photo = memberDetailsSp.getValueString(getString(R.string.spKeyPhotoUrl))

        if (!photo.isNullOrEmpty()){
            Picasso.get().load(photo).resize(dpToPx(130),dpToPx(130)).into(ivPhoto)
        }
        else{
            ivPhoto.setImageResource(R.drawable.baseline_person_24)
        }

        tvFirstname.text = firstname.orEmpty()
        tvMiddlename.text = middlename.orEmpty()
        tvLastname.text = lastname.orEmpty()
        tvEmail.text = email.orEmpty()
        tvContactNumber.text = contactNumber.orEmpty()
        tvBirthday.text = birthday.orEmpty()
    }

//    private fun initViewModel(){
//        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
//
//    }

    fun dpToPx(dp:Int): Int{
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}