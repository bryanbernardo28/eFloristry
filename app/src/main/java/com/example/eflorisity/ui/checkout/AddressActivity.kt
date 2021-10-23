package com.example.eflorisity.ui.checkout

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.eflorisity.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text

class AddressActivity : AppCompatActivity() {
    private lateinit var tilRegion:TextInputLayout
    private lateinit var tilProvince:TextInputLayout
    private lateinit var tilCity:TextInputLayout
    private lateinit var tilBarangay:TextInputLayout
    private lateinit var tilPostalCode:TextInputLayout
    private lateinit var tilDetailedAddress:TextInputLayout

    private lateinit var etRegion:TextInputEditText
    private lateinit var etProvince:TextInputEditText
    private lateinit var etCity:TextInputEditText
    private lateinit var etBarangay:TextInputEditText
    private lateinit var etPostalCode:TextInputEditText
    private lateinit var etDetailedAddress:TextInputEditText

    private lateinit var btnSubmit:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)


        tilRegion = findViewById(R.id.til_address_region_id)
        tilProvince = findViewById(R.id.til_address_province_id)
        tilCity = findViewById(R.id.til_address_city_id)
        tilBarangay = findViewById(R.id.til_address_barangay_id)
        tilPostalCode = findViewById(R.id.til_address_postal_code_id)
        tilDetailedAddress = findViewById(R.id.til_address_detailed_address_id)

        etRegion = findViewById(R.id.et_address_region_id)
        etProvince = findViewById(R.id.et_address_province_id)
        etCity = findViewById(R.id.et_address_city_id)
        etBarangay = findViewById(R.id.et_address_barangay_id)
        etPostalCode = findViewById(R.id.et_address_postal_code_id)
        etDetailedAddress = findViewById(R.id.et_address_detailed_address_id)
        btnSubmit = findViewById(R.id.btn_address_submit_id)




        etRegion.setOnClickListener {
            val selectRegion = SelectRegionDialogFragment(etRegion)
            selectRegion.show(supportFragmentManager,"SelectRegion")
        }


        btnSubmit.setOnClickListener {
            val region:String = etRegion.text.toString()
            val province:String = etProvince.text.toString()
            val city:String = etCity.text.toString()
            val barangay:String = etBarangay.text.toString()
            val postalCode:String = etPostalCode.text.toString()
            val detailedAddress:String = etDetailedAddress.text.toString()

            if (!checkInputAddressHasError(region,province,city,barangay,postalCode,detailedAddress)){
                val completeAddress = "${detailedAddress} \n$barangay,$city \n$region,$province $postalCode"

                val sendAddress = Intent()
                sendAddress.putExtra("region",region)
                sendAddress.putExtra("province",province)
                sendAddress.putExtra("city",city)
                sendAddress.putExtra("barangay",barangay)
                sendAddress.putExtra("postalCode",postalCode)
                sendAddress.putExtra("detailedAddress",detailedAddress)
                sendAddress.putExtra("completeAddress",completeAddress)
                setResult(Activity.RESULT_OK, sendAddress)
                finish()
            }
        }
    }

    class SelectRegionDialogFragment(val etRegion:TextInputEditText) : DialogFragment(){
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Select Region")
                    .setItems(R.array.region_list,
                        DialogInterface.OnClickListener { dialog, which ->
                            var selected_region:String = getResources().getStringArray(R.array.region_list)[which]
                            etRegion.setText(selected_region)
                        })
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }

    fun checkNullOrEmptyString(value:String):Boolean{
        return value.trim().isNullOrEmpty()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val sendAddress = Intent()
        setResult(Activity.RESULT_CANCELED, sendAddress)
        finish()
    }

    fun checkInputAddressHasError(region:String,province:String,city:String,barangay:String,postalCode:String,detailedAddress:String):Boolean{
        var hasError = false
        if (
            checkNullOrEmptyString(region) ||
            checkNullOrEmptyString(province) ||
            checkNullOrEmptyString(city) ||
            checkNullOrEmptyString(barangay) ||
            checkNullOrEmptyString(postalCode) ||
            checkNullOrEmptyString(detailedAddress)
        ){
            if (checkNullOrEmptyString(region)){
                tilRegion.error = "The Region field is required."
                hasError = true
            } else{ tilRegion.clearError() }

            if (checkNullOrEmptyString(province)){
                tilProvince.error = "The Province field is required."
                hasError = true
            } else{ tilProvince.clearError() }

            if (checkNullOrEmptyString(city)){
                tilCity.error = "The City field is required."
                hasError = true
            } else{ tilCity.clearError() }

            if (checkNullOrEmptyString(barangay)){
                tilBarangay.error = "The Barangay field is required."
                hasError = true
            } else{ tilBarangay.clearError() }

            if (checkNullOrEmptyString(postalCode)){
                tilPostalCode.error = "The Postal Code field is required."
                hasError = true
            } else{ tilPostalCode.clearError() }

            if (checkNullOrEmptyString(detailedAddress)){
                tilDetailedAddress.error = "The Detailed Address field is required."
                hasError = true
            } else{ tilDetailedAddress.clearError() }
        }
        else{
            tilRegion.clearError()
            tilProvince.clearError()
            tilCity.clearError()
            tilBarangay.clearError()
            tilPostalCode.clearError()
            tilDetailedAddress.clearError()
        }

        return hasError
    }

    fun TextInputLayout.clearError() {
        isErrorEnabled = false
        error = null
    }
}