package com.example.eflorisity.ui.orders.recyclerview_adapter

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.eflorisity.R
import com.example.eflorisity.ui.cart.recyclerview_adapter.CartAdapter
import com.example.eflorisity.ui.orders.data.OrdersWithProduct
import com.squareup.picasso.Picasso

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    private var orderList:ArrayList<OrdersWithProduct>? = null
    private lateinit var context:Context
    fun ordersAdapter(context: Context,orderList:ArrayList<OrdersWithProduct>){
        this.orderList = orderList
        this.context = context
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvOrderNumber:TextView
        val tvProductName:TextView
        val tvProductQuantity:TextView
        val tvProductPrice:TextView
        val tvProductTotalPrice:TextView
        val ivImage:ImageView

        init {
            tvOrderNumber = view.findViewById(R.id.tv_order_number_id)
            tvProductName = view.findViewById(R.id.tv_order_product_name_id)
            tvProductQuantity = view.findViewById(R.id.tv_order_product_quantity_id)
            tvProductPrice = view.findViewById(R.id.tv_order_product_price_id)
            tvProductTotalPrice = view.findViewById(R.id.tv_order_product_totalprice_id)
            ivImage = view.findViewById(R.id.iv_order_image_id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_orders_list_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = orderList?.get(position)?.product
        val orderNumber = orderList?.get(position)?.order_number.toString()
        val productName = product?.name
        val productPrice = product?.price
        val productPriceLabaled = "Price: ₱$productPrice"
        val productQuantity: Int = product?.quantity!!.toInt()
        val productQuantityLabeled = "Quantity: $productQuantity"
        val productTotalPriceComputed = productPrice!!.toInt() * productQuantity
        val productTotalPrice = "Total Price: ₱$productTotalPriceComputed"
        val photoUrl = product.photo_url

        Picasso.get().load(photoUrl).placeholder(R.drawable.no_image_available).resize(450, 450).into(holder.ivImage)
        holder.tvOrderNumber.text = "Order Number: $orderNumber"
        holder.tvProductName.text = "Product: $productName"
        holder.tvProductQuantity.text = productQuantityLabeled
        holder.tvProductPrice.text = productPriceLabaled
        holder.tvProductTotalPrice.text = productTotalPrice
    }

    override fun getItemCount(): Int = if (orderList.isNullOrEmpty()) 0 else orderList?.size!!

    fun updateData(orderList:ArrayList<OrdersWithProduct>){
        this.orderList?.clear()
        this.orderList?.addAll(orderList)
        notifyDataSetChanged()
    }
}