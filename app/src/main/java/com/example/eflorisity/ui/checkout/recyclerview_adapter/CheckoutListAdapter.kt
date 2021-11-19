package com.example.eflorisity.ui.checkout.recyclerview_adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.example.eflorisity.R
import com.example.eflorisity.ui.cart.data.Cart
import com.example.eflorisity.ui.home.data.ProductDetails
import com.squareup.picasso.Picasso

class CheckoutListAdapter: RecyclerView.Adapter<CheckoutListAdapter.ViewHolder>() {
    private var cartList: ArrayList<Cart>? = null
    private lateinit var context: Context

    fun CheckoutListAdapter(context: Context,cartList:ArrayList<Cart>){
        this.context = context
        this.cartList = cartList
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView
        val productImage: ImageView
        val productPrice: TextView
        val productQuantity: TextView
        val productTotalPrice: TextView
        val clQty:ConstraintLayout
        init {
            productName = view.findViewById(R.id.tv_cart_product_name_id)
            productImage = view.findViewById(R.id.iv_cart_image_id)
            productPrice = view.findViewById(R.id.tv_cart_product_price_id)
            productQuantity = view.findViewById(R.id.tv_cart_product_quantity_id)
            productTotalPrice = view.findViewById(R.id.tv_cart_product_totalprice_id)
            clQty = view.findViewById(R.id.cl_cart_qty_id)
            clQty.visibility = View.GONE

        }
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_cart_products_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product: ProductDetails = cartList?.get(position)!!.product
        val photoUrl = product.photo_url
        val imageUrl = "$photoUrl"
        val productName = product.name
        val productPrice = product.price
        val productPriceLabaled = "Price: ₱$productPrice"
        val productQuantity: Int = cartList?.get(position)!!.quantity.toInt()
        val productQuantityLabeled = "Quantity: $productQuantity"
        val productTotalPrice = "Total Price: ₱${productPrice!!.toInt() * productQuantity}"
        holder.productImage.updateLayoutParams {
            height = (100 * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
            width = (100 * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        }


        Picasso.get().load(imageUrl).placeholder(R.drawable.no_image_available).resize(450, 450).into(holder.productImage);
        holder.productName.text = productName
        holder.productPrice.text = productPriceLabaled
        holder.productQuantity.text = productQuantityLabeled
        holder.productTotalPrice.text = productTotalPrice
    }

    fun Int.toPx(context: Context) = this * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
    override fun getItemCount(): Int = if (cartList.isNullOrEmpty()) 0 else cartList?.size!!
}