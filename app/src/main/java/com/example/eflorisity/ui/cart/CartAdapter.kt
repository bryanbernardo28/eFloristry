package com.example.eflorisity.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.eflorisity.Config
import com.example.eflorisity.R
import com.example.eflorisity.ui.home.ItemsAdapter
import com.example.eflorisity.ui.home.data.ProductDetails
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class CartAdapter: RecyclerView.Adapter<CartAdapter.ViewHolder>() {


    private var cartList: List<Cart>? = null
    private lateinit var context: Context

    fun CartAdapter(context: Context, cartList:List<Cart>){
        this.cartList = cartList
        this.context = context
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView
        val productImage: ImageView
        val productPrice: TextView
        val productQuantity:TextView
        val productTotalPrice:TextView
        val constraintLayout: ConstraintLayout
        init {
            // Define click listener for the ViewHolder's View.
            productName = view.findViewById(R.id.tv_cart_product_name_id)
            productImage = view.findViewById(R.id.iv_cart_image_id)
            productPrice = view.findViewById(R.id.tv_cart_product_price_id)
            productQuantity = view.findViewById(R.id.tv_cart_product_quantity_id)
            productTotalPrice = view.findViewById(R.id.tv_cart_product_totalprice_id)
            constraintLayout = view.findViewById(R.id.cl_cart_row_id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_cart_products_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val config = Config()
        val photoUrl = cartList?.get(position)!!.product_photo_url
        val imageUrl = "${config.startUrl}${config.baseUrl}${photoUrl}"
        val productName = cartList?.get(position)!!.product_name
        val productPrice = cartList?.get(position)!!.product_price
        val productPriceLabaled = "Price: ₱$productPrice"
        val productQuantity:Int = cartList?.get(position)!!.product_quantity
        val productQuantityLabeled = "Quantity: $productQuantity"
        val productTotalPrice = "Total Price: ₱${productPrice!!.toInt() * productQuantity}"

                Picasso.get().load(imageUrl).placeholder(R.drawable.no_image_available).resize(450, 450).into(holder.productImage);
        holder.productName.text = productName
        holder.productPrice.text = productPriceLabaled
        holder.productQuantity.text = productQuantityLabeled
        holder.productTotalPrice.text = productTotalPrice

    }

    override fun getItemCount(): Int = if (cartList.isNullOrEmpty()) 0 else cartList?.size!!

}