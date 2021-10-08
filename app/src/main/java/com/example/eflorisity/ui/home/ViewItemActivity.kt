package com.example.eflorisity.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.eflorisity.R
import com.example.eflorisity.SQLiteDbHelper
import com.example.eflorisity.SharedPref
import com.example.eflorisity.ui.cart.Cart
import com.example.eflorisity.ui.home.data.ProductDetails
import com.squareup.picasso.Picasso
import android.view.Gravity




class ViewItemActivity : AppCompatActivity() {
    lateinit var productDetailsSp:SharedPref
    lateinit var sqliteDbHelper:SQLiteDbHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)

        sqliteDbHelper = SQLiteDbHelper(this)

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

        val addToCart:Button = findViewById(R.id.btn_vi_addtocart_id)
        addToCart.setOnClickListener {
            val productDetails = ProductDetails(
                id = productId,
                supplier_id = supplierId,
                name = productName,
                description = productDescription,
                price = productPrice,
                discount = productDiscount,
                photo_url = photoUrl,
                is_discounted = productIsDiscounted,
            )
            addToCart(productDetails)
        }

        val buyNow:Button = findViewById(R.id.btn_vi_gobacktolist_id)
        buyNow.setOnClickListener {
//            sqliteDbHelper.truncateCart()
            finish()
        }
    }

    fun addToCart(productDetails: ProductDetails){
        val productQuantity = sqliteDbHelper.getProductQuantity(productDetails.id!!)

        val cart = Cart(
            product_id = productDetails.id!!,
            product_name = productDetails.name!!,
            product_price = productDetails.price,
            product_quantity = productQuantity,
            product_discount = productDetails.discount,
            product_is_discounted = productDetails.is_discounted!!,
            product_photo_url = productDetails.photo_url
        )

        val status = sqliteDbHelper.addToCart(cart)
        if (status > -1){
            val toast = Toast.makeText(applicationContext,"Item successfully added to cart.", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 700)
            toast.show()
            Log.d("product-cart-result","Added To Cart")
        }
        else{
            Toast.makeText(this,"Item add to cart failed.",Toast.LENGTH_LONG).show()
            Log.d("product-cart-result","Not Added To Cart \n $cart")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}