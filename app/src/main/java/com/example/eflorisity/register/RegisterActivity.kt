package com.example.eflorisity.register

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.eflorisity.HomeActivity
import com.example.eflorisity.R
import com.example.eflorisity.login.LoginActivity
import com.example.eflorisity.login.LoginLoadingDialog
import com.example.eflorisity.login.LoginViewModel
import com.example.eflorisity.login.data.MemberDetails
import com.example.eflorisity.login.data.MemberResponse
import com.example.eflorisity.login.data.RegisterMemberResponse
import com.example.eflorisity.ui.checkout.AddressActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*




class RegisterActivity : AppCompatActivity() {
    private lateinit var calendar: Calendar

    //TextInputLayout
    private lateinit var tilFirstName:TextInputLayout
    private lateinit var tilMiddleName:TextInputLayout
    private lateinit var tilLastName:TextInputLayout
    private lateinit var tilEmailAddress:TextInputLayout
    private lateinit var tilContactNumber:TextInputLayout
    private lateinit var tilMemberType:TextInputLayout
    private lateinit var tilBirthday:TextInputLayout
    private lateinit var tilPassword:TextInputLayout
    private lateinit var tilConfirmPassword:TextInputLayout
    private lateinit var tilAddress:TextInputLayout

    //EditText
    private lateinit var tieFirstName:TextInputEditText
    private lateinit var tieMiddleName:TextInputEditText
    private lateinit var tieLastName:TextInputEditText
    private lateinit var tieEmailAddress:TextInputEditText
    private lateinit var tieContactNumber:TextInputEditText
    private lateinit var tieMemberType:AutoCompleteTextView
    private lateinit var tieBirthday:TextInputEditText
    private lateinit var tiePassword:TextInputEditText
    private lateinit var tieConfirmPassword:TextInputEditText
    private lateinit var tieAddress:TextInputEditText


    //String
    private var type:String? = null

    //Button
    private lateinit var submitBtn:Button

    //ViewModel
    lateinit var viewModel: RegisterViewModel

    //Loading Dialog
    private lateinit var loadingDialog : LoginLoadingDialog

    //Address
    private var region: String? = null
    private var province:String? = null
    private var city:String? = null
    private var barangay:String? = null
    private var postalCode:String? = null
    private var detailedAddress:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val description = getString(R.string.label_loading_register)
        loadingDialog = LoginLoadingDialog(description,this)

        //TextInputEditText Initalization
        tieFirstName = findViewById(R.id.tie_register_firstname_id)
        tieMiddleName = findViewById(R.id.tie_register_middlename_id)
        tieLastName = findViewById(R.id.tie_register_lastname_id)
        tieEmailAddress = findViewById(R.id.tie_register_emailaddress_id)
        tieContactNumber = findViewById(R.id.tie_register_contactnumber_id)
        tieMemberType = findViewById(R.id.actv_register_membertype_id)
        tieBirthday = findViewById(R.id.tie_register_firstname_id)
        tiePassword = findViewById(R.id.tie_register_password_id)
        tieConfirmPassword = findViewById(R.id.tie_register_confirmpassword_id)
        tieAddress = findViewById(R.id.tie_register_address_id)

        //TextInputLayout Initalization
        tilFirstName = findViewById(R.id.til_register_firstname_id)
        tilMiddleName = findViewById(R.id.til_register_middlename_id)
        tilLastName = findViewById(R.id.til_register_lastname_id)
        tilEmailAddress = findViewById(R.id.til_register_emailaddress_id)
        tilContactNumber = findViewById(R.id.til_register_contactnumber_id)
        tilMemberType = findViewById(R.id.til_register_membertype_id)
        tilBirthday = findViewById(R.id.til_register_birthday_id)
        tilPassword = findViewById(R.id.til_register_password_id)
        tilAddress = findViewById(R.id.til_register_address_id)

        //Button Initialize
        submitBtn = findViewById(R.id.btn_register_submit_id)

        val memberType = resources.getStringArray(R.array.member_type_choice)
        val arrayAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,memberType)
        tieMemberType.setAdapter(arrayAdapter)
        tieMemberType.setOnItemClickListener { parent, view, position, id ->
            type = parent.getItemAtPosition(position).toString()
        }

        tieAddress.setOnClickListener {
            val goToAddressActivity = Intent(this, AddressActivity::class.java)
            startActivityForResult(goToAddressActivity,getString(R.string.address_inforation_request_code).toInt())
        }

        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Manila"))
        calendar.set(Calendar.AM_PM, 0)
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        tieBirthday = findViewById(R.id.tie_register_birthday_id)
        tieBirthday.setOnClickListener{
            val datePickerDialog = DatePickerDialog(this,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateCalendar()
            },year,month,day)
            datePickerDialog.show()
        }
        initViewModel()
        submitBtn.setOnClickListener {
            submitRegister()
        }
    }

    fun submitRegister(){
        var firstname = tieFirstName.text.toString()
        var middlename = tieMiddleName.text.toString()
        var lastname = tieLastName.text.toString()
        var email_address = tieEmailAddress.text.toString()
        var contact_number = tieContactNumber.text.toString()
        var member_type = tieMemberType.text.toString()
        var birthdayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        var birthdayText = tieBirthday.text.toString()
        var newBirthday:String? = if(!TextUtils.isEmpty(birthdayText)) birthdayFormat.format(calendar.time) else null
        var password = tiePassword.text.toString()
        var confirmPassword = tieConfirmPassword.text.toString()

        val memberDetails = MemberDetails(
                                id=null,
                                first_name = firstname,
                                last_name = lastname,
                                middle_name = middlename,
                                email = email_address,
                                contact_number = contact_number,
                                type = "buyer",
                                birthday = newBirthday,
                                state = province.toString(),
                                city = city.toString(),
                                postal_code = postalCode.toString(),
                                address = detailedAddress.toString(),
                                photo_url = null,
                                password = password,
                                password_confirmation = confirmPassword,
                                photo = null
        )

        loadingDialog.startLoading()
//        Log.d("register-result","Submit: ${memberDetails}")
        viewModel.registerMember(memberDetails)

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        viewModel.getRegisteredMemberObservable().observe(this, {
            if (it != null){
//                handleResponse(it)
                if (it.success == true){
                    clearAllErrors()
                    Toast.makeText(this,"Register Successful",Toast.LENGTH_LONG).show()
                    Log.d("register-result","Register Successful")
                    Log.d("register-result","$it")
                    val goToVerificationCodeActivity = Intent(this, VerificationCode::class.java)
                    goToVerificationCodeActivity.putExtra("email",it.member!!.email)
                    startActivity(goToVerificationCodeActivity)
                }
                else{
                    displayErrors(it)
                }
//                Log.d("register-result",it.toString())
            }
            else{
                Toast.makeText(this,"Failed to Register.",Toast.LENGTH_LONG).show()
                clearAllErrors()
//                Log.d("register-result",it.toString())
            }
            loadingDialog.dismissLoading()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED){

            when(requestCode){
                getString(R.string.address_inforation_request_code).toInt() -> {
                    region = data?.getStringExtra("region").toString()
                    province = data?.getStringExtra("province").toString()
                    city = data?.getStringExtra("city").toString()
                    barangay = data?.getStringExtra("barangay").toString()
                    postalCode = data?.getStringExtra("postalCode").toString()
                    detailedAddress = data?.getStringExtra("completeAddress").toString()
                    tilAddress.clearError()
                    tieAddress.setText(detailedAddress)
                }
            }
        }
    }

    fun displayErrors(response: RegisterMemberResponse){
        val error :JSONObject = JSONObject(response.errors.toString())
        if (error.has("first_name")){
            tilFirstName.error = error!!.getJSONArray("first_name")[0].toString()
        }
        else{ tilFirstName.clearError() }

        if (error.has("last_name")){
            tilLastName.error = error!!.getJSONArray("last_name")[0].toString()
        }
        else{ tilLastName.clearError() }

        if (error.has("email")){
            tilEmailAddress.error = error!!.getJSONArray("email")[0].toString()
        }
        else{ tilEmailAddress.clearError() }

        if (error.has("contact_number")){
            tilContactNumber.error = error!!.getJSONArray("contact_number")[0].toString()
        }
        else{ tilContactNumber.clearError() }

        if (error.has("birthday")){
            tilBirthday.error = error!!.getJSONArray("birthday")[0].toString()
        }
        else{ tilBirthday.clearError() }

        if (error.has("type")){
            tilMemberType.error = error!!.getJSONArray("type")[0].toString()
        }
        else{ tilMemberType.clearError() }

        if (error.has("password")){
            tilPassword.error = error!!.getJSONArray("password")[0].toString()
        }
        else{ tilPassword.clearError() }
//        Log.d("register-result",error!!.get("first_name").asJsonArray[0].toString())
    }

    fun clearAllErrors(){
        tilFirstName.clearError()
        tilLastName.clearError()
        tilEmailAddress.clearError()
        tilContactNumber.clearError()
        tilBirthday.clearError()
        tilMemberType.clearError()
        tilPassword.clearError()
    }

    fun TextInputLayout.clearError() {
        isErrorEnabled = false
        error = null
    }

    fun updateCalendar(){
        val myFormat = "MMMM d, yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val newDate = sdf.format(calendar.getTime())
        tieBirthday.setText(newDate)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val goTohomeActivity = Intent(this, HomeActivity::class.java)
        startActivity(goTohomeActivity)
        finish()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}