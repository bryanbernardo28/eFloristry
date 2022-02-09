package com.example.eflorisity.ui.home

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.eflorisity.R
import com.example.eflorisity.SharedPref
import com.example.eflorisity.login.LoginActivity
import com.example.eflorisity.login.LoginLoadingDialog
import com.example.eflorisity.ui.home.data.ProductCart
import com.example.eflorisity.ui.home.data.ProductCartListNotIn
import com.example.eflorisity.ui.home.data.ProductCartNotIn
import com.example.eflorisity.ui.home.data.ProductCartResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso

import android.text.TextUtils





class ViewItemActivity : AppCompatActivity() {
    lateinit var productDetailsSp:SharedPref
    lateinit var viewModel: HomeViewModel
    private lateinit var loadingDialog : LoginLoadingDialog
    private lateinit var memberDetailsSp : SharedPref
    private lateinit var productStock:String
    private var quantity:Int = 1
    private lateinit var etQuantity:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)


        memberDetailsSp = SharedPref(this,getString(R.string.spMemberDetails))


        val memberId = memberDetailsSp.getValueString(getString(R.string.spKeyId))
        val productId = intent.getStringExtra("id")
        val supplierId = intent.getStringExtra("supplier_id")
        val photoUrl = intent.getStringExtra("photo_url")
        val imageUrl = intent.getStringExtra("image_url")
        val productName = intent.getStringExtra("product_name")
        val productPrice = intent.getStringExtra("product_price")
        val productDiscount = intent.getStringExtra("product_discount")
        val productPriceLabaled = intent.getStringExtra("product_price_labeled")
        val productDescription = intent.getStringExtra("product_description")
        val productIsDiscounted = intent.getIntExtra("product_is_discounted",0)
        productStock = intent.getStringExtra("product_stock").toString()

        val tvProductName:TextView = findViewById(R.id.tv_vi_item_name_id)
        val tvProductPrice:TextView = findViewById(R.id.tv_vi_item_price_id)
        val tvProductDescription:TextView = findViewById(R.id.tv_vi_item_desc_id)
        val ivProductImage:ImageView = findViewById(R.id.iv_vi_product_image_id)

        Picasso.get().load(imageUrl).placeholder(R.drawable.no_image_available).resize(450, 450).into(ivProductImage);
        tvProductName.text = productName
        tvProductPrice.text = productPriceLabaled
        tvProductDescription.text = productDescription
        tvProductDescription.movementMethod = ScrollingMovementMethod()

        initViewModel()
        loadingDialog = LoginLoadingDialog(getString(R.string.label_loading),this)


        val addToCart:Button = findViewById(R.id.btn_vi_addtocart_id)
        addToCart.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater

            builder.setTitle("Quantity")
            var dialogLayout = inflater.inflate(R.layout.quantity_dialog, null)
//                val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
            etQuantity = dialogLayout.findViewById<EditText>(R.id.et_product_quantity_dialog_id)
            val ibPlus = dialogLayout.findViewById<ImageButton>(R.id.ib_product_quantity_dialog_plus_id)
            val ibMinus = dialogLayout.findViewById<ImageButton>(R.id.ib_product_quantity_dialog_minus_id)
            Log.d("cart-result","Quantity: $quantity")
            etQuantity.setText(quantity.toString())
            etQuantity.filters = arrayOf<InputFilter>(InputFilterMinMax("1", productStock))
            builder.setView(dialogLayout)
            builder.setPositiveButton("Proceed") { _, _ ->
                loadingDialog.startLoading()
                if(checkIsLoggedIn()){
                    val productCart = ProductCart(
                        member_id = memberId!!,
                        product_id = productId!!,
                        quantity = quantity,
                        isAdd = null
                    )
                    addToCart(productCart)
                }
                else{
                    val spMyCart = SharedPref(this,getString(R.string.spMyCart))
                    val spKeyMyCart = getString(R.string.spKeyCartStringSet)
                    var getMyCart = spMyCart.getValueString(spKeyMyCart)
                    Log.d("cart-result","Result: ${getMyCart.isNullOrEmpty()}")

                    if (getMyCart.isNullOrEmpty()){
                        saveToCartWithoutAccount(spMyCart,spKeyMyCart,productId!!,quantity,true,false)
                    }
                    else{
                        val productInMyCartList = Gson().fromJson(getMyCart.toString(),ProductCartListNotIn::class.java).carts
                        val isExisting = productInMyCartList.any { it.product_id == productId }
                        saveToCartWithoutAccount(spMyCart,spKeyMyCart,productId!!,quantity,false,isExisting)
                    }
                }
            }
            ibPlus.setOnClickListener{
                var qtyValue:String = if (etQuantity.text.isNullOrEmpty()){
                    "0"
                } else{
                    etQuantity.text.toString()
                }
                Log.d("product-result","Plus Clicked")
                viewModel.computeQuantity(qtyValue.toInt(),productStock.toInt(),true)
            }
            ibMinus.setOnClickListener {
                var qtyValue:String = if (etQuantity.text.isNullOrEmpty()){
                    "0"
                } else{
                    etQuantity.text.toString()
                }

                viewModel.computeQuantity(qtyValue.toInt(),productStock.toInt(),false)
            }

            var dialog = builder.create()

//            etQuantity.doAfterTextChanged {
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = !TextUtils.isEmpty(it)
////                Log.d("product-result","Result: ${TextUtils.isEmpty(it)}")
//            }
            builder.show()


//                val res = dialog.context.resources
//                val dividerId = res.getIdentifier("titleDivider", "id", "android")
//                val divider: View? = dialog.window?.decorView?.findViewById(dividerId)
//                Toast.makeText(this,"Not Null $divider",Toast.LENGTH_LONG).show()
//                divider.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.black))


        }

        val backButton:Button = findViewById(R.id.btn_vi_gobacktolist_id)
        backButton.setOnClickListener {
//            val spMyCart = SharedPref(this,getString(R.string.spMyCart))
//            spMyCart.clearSharedPreference()
            finish()
        }
    }

    inner class InputFilterMinMax():InputFilter{
        private var intMin: Int = 0
        private var intMax: Int = 0
        constructor(minValue: String, maxValue: String) : this() {
            this.intMin = Integer.parseInt(minValue)
            this.intMax = Integer.parseInt(maxValue)
        }
        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            try {
                val input = Integer.parseInt(dest.toString() + source.toString())
                if (isInRange(intMin, intMax, input))
                    return null
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return ""
        }

        private fun isInRange(a: Int, b: Int, c: Int): Boolean {
            return if (b > a) c in a..b else c in b..a
        }

    }

    private fun addToCart(productCart: ProductCart){
        val token = "Bearer " + memberDetailsSp.getValueString(getString(R.string.spKeyToken));
        viewModel.addToCart(token,productCart)
    }

    private fun saveToCartWithoutAccount(spMyCart:SharedPref, spKeyMyCart:String, productId:String,qty:Int, isNew:Boolean,isExisting:Boolean){
        if (isNew){
            val productCart = ProductCartNotIn(
                product_id = productId,
                quantity = qty
            )
            val productCartList = ArrayList<ProductCartNotIn>()
            productCartList.add(productCart)
            val productCartListNotIn = ProductCartListNotIn(
                carts = productCartList
            )
            spMyCart.save(spKeyMyCart,Gson().toJson(productCartListNotIn).toString())
        }
        else{
            var getMyCart = spMyCart.getValueString(spKeyMyCart)
            var productInMyCartList = Gson().fromJson(getMyCart,ProductCartListNotIn::class.java)
            if(isExisting){
                val productIndex = productInMyCartList.carts.indexOfFirst { it.product_id == productId }
                productInMyCartList.carts[productIndex].quantity+=qty
                spMyCart.save(spKeyMyCart,Gson().toJson(productInMyCartList).toString())
                getMyCart = spMyCart.getValueString(spKeyMyCart)
                Log.d("cart-result","Data: $getMyCart, Existing")
            }
            else{
                val productCart = ProductCartNotIn(
                    product_id = productId,
                    quantity = qty
                )
                val productCartList = productInMyCartList.carts
                productCartList.add(productCart)
                val productCartListNotIn = ProductCartListNotIn(
                    carts = productCartList
                )
                spMyCart.save(spKeyMyCart,Gson().toJson(productCartListNotIn).toString())
                getMyCart = spMyCart.getValueString(spKeyMyCart)
                Log.d("cart-result","Data: $getMyCart, Not Existing")
            }
        }
        successAddToCart()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.getAddToCartResponse().observe(this, Observer<ProductCartResponse>{

            if (it != null){
                handleResponse(it)
                Log.d("product-cart-result",it.toString())
            }
            else{
                Toast.makeText(this,"Failed to add to cart.",Toast.LENGTH_LONG).show()
                Log.d("product-cart-result","$it")
            }
            loadingDialog.dismissLoading()
        })

        viewModel.getComputedQuantityObservable().observe(this,{
            loadingDialog.dismissLoading()
            etQuantity.setText(it.toString())
            quantity = it
            Log.d("cart-result","Res: $it")
        })

    }

    fun checkIsLoggedIn(): Boolean {
        return memberDetailsSp.getValueBoolean(getString(R.string.spKeyIsLoggedIn), false)
    }


    fun handleResponse(it:ProductCartResponse){
        if(it.success == true){
            successAddToCart()
        }
        else{
            Toast.makeText(this,"Item add to cart failed.",Toast.LENGTH_LONG).show()
        }
    }

    fun successAddToCart(){
        val toast = Toast.makeText(applicationContext,"Item successfully added to cart.", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 700)
        toast.show()
        quantity = 1
        loadingDialog.dismissLoading()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}