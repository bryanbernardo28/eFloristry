package com.example.eflorisity.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.eflorisity.Config
import com.example.eflorisity.HomeActivity
import com.example.eflorisity.R
import com.example.eflorisity.ui.home.data.ProductDetails
import com.squareup.picasso.Picasso

class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
    private var productDataSet: List<ProductDetails>? = null
    private lateinit var context: Context

    fun ItemsAdapter(context: Context, productInfos:List<ProductDetails>){
        this.productDataSet = productInfos
        this.context = context
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView
        val productImage: ImageView
        val productPrice: TextView
        val constraintLayout: ConstraintLayout
        init {
            // Define click listener for the ViewHolder's View.
            productName = view.findViewById(R.id.tv_home_products_name_id)
            productImage = view.findViewById(R.id.iv_home_product_image_id)
            productPrice = view.findViewById(R.id.tv_home_product_price_id)
            constraintLayout = view.findViewById(R.id.cl_home_product_row_id)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.rv_home_products_row, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that
        val config = Config()
        val id = productDataSet?.get(position)!!.id
        val supplierId = productDataSet?.get(position)!!.supplier_id
        val photoUrl = productDataSet?.get(position)!!.photo_url
        val imageUrl = "${photoUrl}"
        val productName = productDataSet?.get(position)!!.name
        val productPrice = productDataSet?.get(position)!!.price
        val productDiscount = productDataSet?.get(position)!!.discount
        val productPriceLabaled = "₱$productPrice"
        val productDescription = productDataSet?.get(position)!!.description
        val productIsDiscounted = productDataSet?.get(position)!!.is_discounted


        Picasso.get().load(imageUrl).placeholder(R.drawable.no_image_available).resize(450, 450).into(holder.productImage);
        holder.productName.text = productName
        holder.productPrice.text = productPriceLabaled
        holder.constraintLayout.setOnClickListener {
            val viewItemActivity = Intent(context, ViewItemActivity::class.java)
            viewItemActivity.putExtra("image_url",imageUrl)
            viewItemActivity.putExtra("photo_url",photoUrl)
            viewItemActivity.putExtra("id",id)
            viewItemActivity.putExtra("supplier_id",supplierId)
            viewItemActivity.putExtra("product_name",productName)
            viewItemActivity.putExtra("product_price",productPrice)
            viewItemActivity.putExtra("product_discount",productDiscount)
            viewItemActivity.putExtra("product_price_labaled",productPriceLabaled)
            viewItemActivity.putExtra("product_description",productDescription)
            viewItemActivity.putExtra("product_is_discounted",productIsDiscounted)
            context.startActivity(viewItemActivity)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount():Int = if (productDataSet.isNullOrEmpty()) 0 else productDataSet?.size!!

}