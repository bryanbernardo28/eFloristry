package com.example.eflorisity.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.eflorisity.R
import com.example.eflorisity.SharedPref
import com.example.eflorisity.login.LoginActivity
import com.example.eflorisity.login.LoginLoadingDialog
import com.example.eflorisity.ui.home.data.ProductCart
import com.example.eflorisity.ui.home.data.ProductCartResponse
import com.squareup.picasso.Picasso


class ViewItemActivity : AppCompatActivity() {
    lateinit var productDetailsSp:SharedPref
    lateinit var viewModel: HomeViewModel
    private lateinit var loadingDialog : LoginLoadingDialog
    private lateinit var memberDetailsSp : SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)


        memberDetailsSp = SharedPref(this,getString(R.string.spMemberDetails))
        val isLoggedIn = memberDetailsSp.getValueBoolean(getString(R.string.spKeyIsLoggedIn),false)

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
            if(isLoggedIn){
                loadingDialog.startLoading()
                val productCart = ProductCart(
                    member_id = memberId!!,
                    product_id = productId!!
                )
                addToCart(productCart)
            }
            else{
                val goToLoginActivity = Intent(this, LoginActivity::class.java)
                startActivity(goToLoginActivity)
                finish()
            }
        }

        val buyNow:Button = findViewById(R.id.btn_vi_gobacktolist_id)
        buyNow.setOnClickListener {
//            sqliteDbHelper.truncateCart()
            finish()
        }
    }

    fun addToCart(productCart: ProductCart){
        val token = "Bearer " + memberDetailsSp.getValueString(getString(R.string.spKeyToken));
        viewModel.addToCart(token,productCart)
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
    }


    fun handleResponse(it:ProductCartResponse){
        if(it.success == true){
            val toast = Toast.makeText(applicationContext,"Item successfully added to cart.", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 700)
            toast.show()
        }
        else{
            Toast.makeText(this,"Item add to cart failed.",Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}