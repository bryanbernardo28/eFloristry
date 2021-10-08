package com.example.eflorisity.ui.checkout

import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
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
import com.example.eflorisity.Config
import com.example.eflorisity.R
import com.example.eflorisity.SQLiteDbHelper
import com.example.eflorisity.SharedPref
import com.example.eflorisity.login.LoginLoadingDialog
import com.example.eflorisity.login.data.MemberLoginDetails
import com.example.eflorisity.ui.cart.Cart
import com.example.eflorisity.ui.cart.CartAdapter
import com.example.eflorisity.ui.checkout.data.Checkout
import com.example.eflorisity.ui.checkout.data.CheckoutResponse
import com.example.eflorisity.ui.checkout.data.ProductCheckout
import com.example.eflorisity.ui.home.data.ProductDetails
import com.google.gson.Gson

class CheckoutActivity : AppCompatActivity() {
    //static
    val sfValue = 0

    lateinit var sqliteDbHelper: SQLiteDbHelper
    private lateinit var rvAdapter: CartAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingDialog : LoginLoadingDialog

    //TextViews
    private lateinit var tvTotalAmount:TextView
    private lateinit var tvPaymentMethod:TextView
    private lateinit var tvSf:TextView
    private lateinit var tvAddress:TextView

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
    private lateinit var carts:ArrayList<Cart>
    private var priceSubTotal = 0


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
        tvAddress = findViewById(R.id.tv_checkiut_address_id)

        val description = getString(R.string.label_loading)
        loadingDialog = LoginLoadingDialog(description,this)
        loadingDialog.startLoading()

        sqliteDbHelper = SQLiteDbHelper(this)
        carts = sqliteDbHelper.getCart()



        initRecyclerView()
        productCheckout = ArrayList<ProductCheckout>()
        for (cart in carts){
            var itemTotal = cart.product_price!!.toInt() * cart.product_quantity
            priceSubTotal += itemTotal

            var productDetails = ProductCheckout(product_id = cart.product_id,quantity = cart.product_quantity)
            productCheckout.add(productDetails)
        }
        rvAdapter.CartAdapter(this,carts)
        rvAdapter.notifyDataSetChanged()


        val paymentMethod = "Cash On Delivery"
        tvPaymentMethod.text = paymentMethod

        val addressValue = "Address: Your Address Here"
        tvAddress.text = addressValue


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
        var memberDetailsSp = SharedPref(this,getString(R.string.spMemberDetails))
        val member_id = memberDetailsSp.getValueString(getString(R.string.spKeyId))
        val orderProducts = Checkout(
            member_id = member_id!!,
            total_amount = priceSubTotal.toString(),
            products = productCheckout
        )
        val jsonString = Gson().toJson(orderProducts)
        Log.d("order-product-result",jsonString)
        viewModel.addOrderProducts(orderProducts,"Bearer " + memberDetailsSp.getValueString(getString(R.string.spKeyToken)))
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
        rvAdapter = CartAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)
        )
        recyclerView.adapter = rvAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}