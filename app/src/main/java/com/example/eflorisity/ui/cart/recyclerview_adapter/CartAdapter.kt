package com.example.eflorisity.ui.cart.recyclerview_adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.eflorisity.R
import com.example.eflorisity.ui.cart.data.Cart
import com.example.eflorisity.ui.home.data.ProductDetails
import com.squareup.picasso.Picasso
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.example.eflorisity.SharedPref
import com.example.eflorisity.login.LoginLoadingDialog
import com.example.eflorisity.ui.cart.CartFragment
import com.example.eflorisity.ui.cart.CartViewModel
import com.example.eflorisity.ui.home.data.ProductCartListNotIn
import com.google.gson.Gson


class CartAdapter: RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private var cartList: ArrayList<Cart>? = null
    private lateinit var context: Context
    private lateinit var cartViewModel: CartViewModel
    private lateinit var loadingDialog: LoginLoadingDialog
    private var totalPrice:Int = 0
    private var isLoggedIn:Boolean = false
    private lateinit var spCart: SharedPref
    private lateinit var cartClickListener: CartClickListener


    fun CartAdapter(context: Context, cartViewModel: CartViewModel, cartList:ArrayList<Cart>, loadingDialog:LoginLoadingDialog,isLoggedIn:Boolean,cartClickListener: CartClickListener,totalPrice:Int){
        this.cartList = cartList
        this.context = context
        this.cartViewModel = cartViewModel
        this.loadingDialog = loadingDialog
        this.totalPrice = totalPrice
        this.isLoggedIn = isLoggedIn
        this.spCart = SharedPref(context,context.getString(R.string.spMyCart))
        this.cartClickListener = cartClickListener
    }

    fun get(pos:Int):String?{
        return cartList?.get(pos)!!.product.id
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView
        val productImage: ImageView
        val productPrice: TextView
        val productQuantity:TextView
        val productQty:TextView
        val productTotalPrice:TextView
        val constraintLayout: ConstraintLayout
        val clQty:ConstraintLayout
        val btnMinus:ImageButton
        val btnAdd:ImageButton
        init {
            // Define click listener for the ViewHolder's View.
            productName = view.findViewById(R.id.tv_cart_product_name_id)
            productImage = view.findViewById(R.id.iv_cart_image_id)
            productPrice = view.findViewById(R.id.tv_cart_product_price_id)
            productQuantity = view.findViewById(R.id.tv_cart_product_quantity_id)
            productTotalPrice = view.findViewById(R.id.tv_cart_product_totalprice_id)
            constraintLayout = view.findViewById(R.id.cl_cart_row_id)
            productQty = view.findViewById(R.id.tv_cart_qty_id)
            clQty = view.findViewById(R.id.cl_cart_qty_id)
            btnMinus = view.findViewById(R.id.ib_cart_minusqty_id)
            btnAdd = view.findViewById(R.id.ib_cart_addqty_id)
            clQty.visibility = View.VISIBLE

            productQuantity.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_cart_products_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product:ProductDetails = cartList?.get(position)!!.product
        val member_id = cartList!![position].member_id.toString();
        val photoUrl = product.photo_url
        val imageUrl = "${photoUrl}"
        val productName = product.name
        val productPrice = product.price
        val productPriceLabaled = "Price: ₱$productPrice"
        var productQuantity: Int = cartList?.get(position)!!.quantity.toInt()
        val productQuantityLabeled = "Quantity: $productQuantity"
        val productTotalPriceComputed = productPrice!!.toInt() * productQuantity
        val productTotalPrice = "Total Item Price: ₱$productTotalPriceComputed"
        val stocks = product.stocks!!.toInt()

//        holder.totalPrice += productTotalPriceComputed

        Picasso.get().load(imageUrl).placeholder(R.drawable.no_image_available).resize(450, 450).into(holder.productImage)
        holder.productName.text = productName
        holder.productPrice.text = productPriceLabaled
        holder.productQuantity.text = productQuantityLabeled
        holder.productTotalPrice.text = productTotalPrice
        holder.productQty.text = productQuantity.toString()

        holder.constraintLayout.setOnLongClickListener{
            val newFragment = CartFragment.LongClickedItem(member_id,product, cartViewModel, loadingDialog,isLoggedIn,cartList!!)
            newFragment.show((context as FragmentActivity).supportFragmentManager, "LongClicked")
            true
        }

        holder.btnAdd.isEnabled = stocks!!.toInt() > productQuantity
        holder.btnMinus.isEnabled = productQuantity > 1

        holder.btnAdd.setOnClickListener {
            var qtyVal = productQuantity
            var isStocksGreaterThanQty = stocks > productQuantity
            if (isStocksGreaterThanQty) qtyVal++
            cartClickListener.onCartItemClicked(qtyVal, product.id.toString(),position,0)
//            holder.btnAdd.isEnabled = isStocksGreaterThanQty
        }

        holder.btnMinus.setOnClickListener {
            var qtyVal = productQuantity
            var isGreaterThanOne = productQuantity > 1
            if (isGreaterThanOne) qtyVal--
            cartClickListener.onCartItemClicked(qtyVal,product.id.toString(),position,1)
//            holder.btnMinus.isEnabled = isGreaterThanOne
        }
    }


    override fun getItemCount(): Int = if (cartList.isNullOrEmpty()) 0 else cartList?.size!!

}