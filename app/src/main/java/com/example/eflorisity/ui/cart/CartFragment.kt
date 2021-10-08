package com.example.eflorisity.ui.cart

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eflorisity.HomeActivity
import com.example.eflorisity.R
import com.example.eflorisity.SQLiteDbHelper
import com.example.eflorisity.SharedPref
import com.example.eflorisity.databinding.FragmentCartBinding
import com.example.eflorisity.login.LoginLoadingDialog
import com.example.eflorisity.ui.checkout.CheckoutActivity

class CartFragment : Fragment() {

    private lateinit var cartViewModel: CartViewModel
    private var _binding: FragmentCartBinding? = null
    lateinit var sqliteDbHelper: SQLiteDbHelper
    private lateinit var rvAdapter: CartAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingDialog : LoginLoadingDialog
    private lateinit var tvSubtotal:TextView
    private lateinit var btnCheckout:Button
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        _binding = FragmentCartBinding.inflate(inflater, container, false)

        val root: View = binding.root
        tvSubtotal = binding.tvCartProductSubtotalPrice
        recyclerView = binding.rvCartProductId
        btnCheckout = binding.btnCartCheckoutId

        val description = getString(R.string.label_loading)
        loadingDialog = LoginLoadingDialog(description,requireActivity())
        loadingDialog.startLoading()

        sqliteDbHelper = SQLiteDbHelper(requireContext())
        val carts = sqliteDbHelper.getCart()

        if (!carts.isEmpty()){
            initRecyclerView()

            var priceSubTotal = 0
            for (cart in carts){
                var itemTotal = cart.product_price!!.toInt() * cart.product_quantity
                priceSubTotal += itemTotal
            }
            rvAdapter.CartAdapter(requireContext(),carts)
            rvAdapter.notifyDataSetChanged()

            tvSubtotal.text = "Total Price: ₱$priceSubTotal"
            btnCheckout.isEnabled = true
        }
        else{
            recyclerView.visibility = View.GONE
            tvSubtotal.text = "Cart is empty"
            tvSubtotal.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tvSubtotal.setTextColor(Color.BLACK)
            tvSubtotal.setTextSize(TypedValue.COMPLEX_UNIT_SP,30f)
        }

        loadingDialog.dismissLoading()


        btnCheckout.setOnClickListener {
            val goToCheckoutActivity = Intent(requireContext(), CheckoutActivity::class.java)
            startActivity(goToCheckoutActivity)
        }

        return root
    }


    fun initRecyclerView() {
        rvAdapter = CartAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
        recyclerView.adapter = rvAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}