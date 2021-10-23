package com.example.eflorisity.ui.checkout

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eflorisity.R
import com.example.eflorisity.SharedPref
import com.example.eflorisity.login.LoginLoadingDialog
import com.example.eflorisity.ui.cart.CartAdapter
import com.example.eflorisity.ui.cart.data.Cart
import com.example.eflorisity.ui.checkout.data.Checkout
import com.example.eflorisity.ui.checkout.data.CheckoutResponse
import com.example.eflorisity.ui.checkout.data.ProductCheckout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson

class CheckoutActivity : AppCompatActivity() {
    //static
    val sfValue = 0

    private lateinit var rvAdapter: CheckoutListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingDialog : LoginLoadingDialog

    //TextViews
    private lateinit var tvTotalAmount:TextView
    private lateinit var tvPaymentMethod:TextView
    private lateinit var tvSf:TextView

    //TextInput
    private lateinit var etAddress:TextInputEditText
    private lateinit var tilAddress:TextInputLayout
    private lateinit var etNotes:TextInputEditText
    private lateinit var tilNotes:TextInputLayout


    //Button
    private lateinit var btnPlaceOrder: Button
    private lateinit var btnPaymentMethod: Button

    //ConstraintLayout
    private lateinit var clCourier:ConstraintLayout

    //ViewModel
    lateinit var viewModel: CheckoutViewModel

    //Checkout
    private lateinit var productCheckout:ArrayList<ProductCheckout>

    //Carts
    private var priceSubTotal = 0

    //Address
    private var region: String? = null
    private var province:String? = null
    private var city:String? = null
    private var barangay:String? = null
    private var postalCode:String? = null
    private var detailedAddress:String? = null

    private var notes:String? = null
    private var paymentMethod:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        recyclerView = findViewById(R.id.rv_checkout_items_id)
        btnPlaceOrder = findViewById(R.id.btn_checkout_place_order_id)
        btnPaymentMethod = findViewById(R.id.btn_checkout_payment_method_id)
        clCourier = findViewById(R.id.cl_checkout_courier_id)
        tvTotalAmount = findViewById(R.id.tv_checkout_total_amount_id)
        tvPaymentMethod = findViewById(R.id.tv_checkout_payment_method_id)
        tvSf = findViewById(R.id.tv_checkout_sf_id)


        etAddress = findViewById(R.id.et_checkout_address_id)
        tilAddress = findViewById(R.id.til_checkout_address_id)
        etNotes = findViewById(R.id.et_checkout_notes_id)
        tilNotes = findViewById(R.id.til_checkout_notes_id)

        val description = getString(R.string.label_loading)
        loadingDialog = LoginLoadingDialog(description,this)
        loadingDialog.startLoading()


        initRecyclerView()
        productCheckout = ArrayList<ProductCheckout>()
        val cartList = intent.getSerializableExtra("cartList") as ArrayList<Cart>
        for (cart in cartList){
            val product = cart.product
            var itemTotal = product.price!!.toInt() * cart.quantity.toInt()
            priceSubTotal += itemTotal

            var productDetails = ProductCheckout(id = product.id,price = product.price,qty = cart.quantity.toInt())
            productCheckout.add(productDetails)
        }

        rvAdapter.CheckoutListAdapter(this,cartList)
        rvAdapter.notifyDataSetChanged()


        val paymentMethod = "Cash On Delivery"
        tvPaymentMethod.text = paymentMethod

        val addressValue = "Address: Your Address Here"
//        tvAddress.text = addressValue

        etAddress.setOnClickListener {
            val goToAddressActivity = Intent(this, AddressActivity::class.java)
            startActivityForResult(goToAddressActivity,getString(R.string.address_inforation_request_code).toInt())
        }


        tvSf.text = "₱$sfValue"

        priceSubTotal += sfValue

        tvTotalAmount.text = "Total Amount: ₱${priceSubTotal}"

        btnPaymentMethod.setOnClickListener {
            val choosePaymentMethod = ChoosePaymentMethod(tvPaymentMethod)
            choosePaymentMethod.show(supportFragmentManager,"ChoosePaymentMethod")
        }
        loadingDialog.dismissLoading()
        initViewModel()

    }

    fun placeOrder(view:View){
        val address:String = etAddress.text.toString()
        if (!checkInputAddressHasError(address)){
            var memberDetailsSp = SharedPref(this,getString(R.string.spMemberDetails))
            val memberId = memberDetailsSp.getValueString(getString(R.string.spKeyId))
            val memberContactNumber = memberDetailsSp.getValueString(getString(R.string.spKeyContactNumber))
            val memberEmailAddress = memberDetailsSp.getValueString(getString(R.string.spKeyEmail))
            notes = etNotes.text?.toString()
            paymentMethod = tvPaymentMethod.text?.toString()

            val orderProducts = Checkout(
                member_id = memberId.toString(),
                contact_number = memberContactNumber.toString(),
                email_address = memberEmailAddress.toString(),
                region = region.toString(),
                state = province.toString(),
                city = city.toString(),
                barangay = barangay.toString(),
                postal_code = postalCode.toString(),
                address = detailedAddress.toString(),
                payment_method = paymentMethod.toString(),
                notes = notes.toString(),
                total_amount = priceSubTotal.toString(),
                carts = productCheckout
            )
            val jsonString = Gson().toJson(orderProducts)
            Log.d("order-product-result",jsonString)
            viewModel.addOrderProducts(orderProducts,"Bearer " + memberDetailsSp.getValueString(getString(R.string.spKeyToken)))
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(CheckoutViewModel::class.java)
        viewModel.addOrderProductsObservable().observe(this,Observer<CheckoutResponse?>{
            if (it != null){
                Log.d("order-product-result",it.toString())
                if (it.success){
                    Toast.makeText(this,getString(R.string.place_order_success),Toast.LENGTH_LONG).show()
                }
            }
            else{
                Log.d("order-product-result",it.toString())
            }
        })
    }

    class ChoosePaymentMethod(val tvPaymentMethod: TextView) : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Payment Method")
                    .setItems(R.array.str_payment_method_array,
                    DialogInterface.OnClickListener{ dialog, which ->
                        var chosePaymentMethod:String = getResources().getStringArray(R.array.str_payment_method_array)[which]
                        tvPaymentMethod.text = chosePaymentMethod
                    })
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }

    fun initRecyclerView() {
        rvAdapter = CheckoutListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)
        )
        recyclerView.adapter = rvAdapter
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
                    etAddress.setText(detailedAddress)
                }
            }
        }
    }

    fun TextInputLayout.clearError() {
        isErrorEnabled = false
        error = null
    }

    fun checkInputAddressHasError(completeAddress:String):Boolean{
        var hasError = false
        if (checkNullOrEmptyString(completeAddress)){
            if (checkNullOrEmptyString(completeAddress)){
                tilAddress.error = "The Complete Address field is required."
                hasError = true
            } else{ tilAddress.clearError() }
        }
        else{
            tilAddress.clearError()
        }

        return hasError
    }

    fun checkNullOrEmptyString(value:String):Boolean{
        return value.trim().isNullOrEmpty()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}