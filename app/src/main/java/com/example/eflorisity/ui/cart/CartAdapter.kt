package com.example.eflorisity.ui.cart

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.eflorisity.Config
import com.example.eflorisity.R
import com.example.eflorisity.ui.cart.data.Cart
import com.example.eflorisity.ui.home.ItemsAdapter
import com.example.eflorisity.ui.home.data.ProductDetails
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import androidx.fragment.app.FragmentActivity
import com.example.eflorisity.login.LoginLoadingDialog


class CartAdapter: RecyclerView.Adapter<CartAdapter.ViewHolder>() {


    private var cartList: ArrayList<Cart>? = null
    private lateinit var context: Context
    private lateinit var cartViewModel: CartViewModel
    private lateinit var loadingDialog: LoginLoadingDialog

    fun CartAdapter(context: Context, cartViewModel: CartViewModel,cartList:ArrayList<Cart>,loadingDialog:LoginLoadingDialog){
        this.cartList = cartList
        this.context = context
        this.cartViewModel = cartViewModel
        this.loadingDialog = loadingDialog
    }

    fun get(pos:Int):String?{
        return cartList?.get(pos)!!.product.id
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
        val product:ProductDetails = cartList?.get(position)!!.product
        val photoUrl = product.photo_url
        val imageUrl = "${photoUrl}"
        val productName = product.name
        val productPrice = product.price
        val productPriceLabaled = "Price: ₱$productPrice"
        val productQuantity: Int = cartList?.get(position)!!.quantity.toInt()
        val productQuantityLabeled = "Quantity: $productQuantity"
        val productTotalPrice = "Total Price: ₱${productPrice!!.toInt() * productQuantity}"

        Picasso.get().load(imageUrl).placeholder(R.drawable.no_image_available).resize(450, 450).into(holder.productImage);
        holder.productName.text = productName
        holder.productPrice.text = productPriceLabaled
        holder.productQuantity.text = productQuantityLabeled
        holder.productTotalPrice.text = productTotalPrice
        holder.constraintLayout.setOnLongClickListener{
            val newFragment = CartFragment.LongClickedItem(product,cartViewModel,loadingDialog)
            newFragment.show((context as FragmentActivity).supportFragmentManager, "LongClicked")
            true
        }

    }

    override fun getItemCount(): Int = if (cartList.isNullOrEmpty()) 0 else cartList?.size!!

}