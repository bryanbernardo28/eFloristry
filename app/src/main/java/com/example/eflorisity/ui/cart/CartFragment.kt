package com.example.eflorisity.ui.cart

import android.R.attr
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eflorisity.R
import com.example.eflorisity.SharedPref
import com.example.eflorisity.databinding.FragmentCartBinding
import com.example.eflorisity.login.LoginActivity
import com.example.eflorisity.login.LoginLoadingDialog
import com.example.eflorisity.ui.cart.data.Cart
import com.example.eflorisity.ui.cart.recyclerview_adapter.CartAdapter
import com.example.eflorisity.ui.checkout.CheckoutActivity
import com.example.eflorisity.ui.home.data.ProductDetails
import android.R.attr.data




class CartFragment : Fragment() {

    private lateinit var cartViewModel: CartViewModel
    private var _binding: FragmentCartBinding? = null
//    lateinit var sqliteDbHelper: SQLiteDbHelper
    private lateinit var rvAdapter: CartAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingDialog : LoginLoadingDialog
    private lateinit var tvSubtotal:TextView
    private lateinit var btnCheckout:Button
    private lateinit var memberDetailsSp:SharedPref
    private lateinit var swipeRefresh:SwipeRefreshLayout

    private lateinit var cartList:ArrayList<Cart>

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

        swipeRefresh = binding.srlCartItemcartId

        memberDetailsSp = SharedPref(requireContext(),getString(R.string.spMemberDetails))
        val isLoggedIn = memberDetailsSp.getValueBoolean(getString(R.string.spKeyIsLoggedIn),false)
        val description = getString(R.string.label_loading)
        loadingDialog = LoginLoadingDialog(description,requireActivity())

        if (isLoggedIn) {
            tvSubtotal = binding.tvCartProductSubtotalPrice
            recyclerView = binding.rvCartProductId
            btnCheckout = binding.btnCartCheckoutId

            val member_id = memberDetailsSp.getValueString(getString(R.string.spKeyId))
            if (!loadingDialog.isShowing()){
                loadingDialog.startLoading()
                getCart(member_id!!)
            }


            initRecyclerView()
            cartViewModel.getMyCartObservable().observe(viewLifecycleOwner, {
                if (!it.isNullOrEmpty()){
//                    Log.d("cart-result","CartFragment: ${it.toString()}")
                    resetTextViewPrice()
                    recyclerView.visibility = View.VISIBLE
                    rvAdapter.CartAdapter(requireContext(),cartViewModel,it,loadingDialog,tvSubtotal)
                    rvAdapter.notifyDataSetChanged()
                    btnCheckout.isEnabled = true
                    cartList = it
                }
                else{
                    Log.d("cart-result","CartFragment: Else $it")
                    cartIsEmpty("Cart is empty")
                }
                stopRefreshLoading()
            })

            cartViewModel.getDeleteFromCartResponseObservable().observe(viewLifecycleOwner, Observer {
                if (it.success){
                    Log.d("cart-result","CartFragment: $it")
                    cartViewModel.getMyCartFun(member_id!!)
                }
                else{
                    Log.d("cart-result","CartFragment: Else $it")
                }
                stopRefreshLoading()
            })

            cartViewModel.getErrorMyCartObservable().observe(viewLifecycleOwner, Observer {
                if (it != 200){
                    if(it == 429){
                        cartIsEmpty("Too many request, try again later.")
                    }
                    Log.d("cart-result","Error Code: $it")
                }
                stopRefreshLoading()
            })

            swipeRefresh.setOnRefreshListener {
                getCart(member_id!!)
            }


            btnCheckout.setOnClickListener {
                val goToCheckoutActivity = Intent(activity, CheckoutActivity::class.java)
                goToCheckoutActivity.putExtra("cartList",cartList)
                activity?.startActivityForResult(goToCheckoutActivity,getString(R.string.checkout_request_code).toInt())
            }
        }

        return root
    }

    class LongClickedItem(val member_id: String,val product:ProductDetails,val cartViewModel: CartViewModel,val loadingDialog:LoginLoadingDialog) : DialogFragment(){
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Choose Action")
                    .setItems(R.array.long_clicked_row_action,
                        DialogInterface.OnClickListener { _, which ->
                            if(which == 0){
                                loadingDialog.startLoading()
                                cartViewModel.deleteItemFromCartFun(member_id,product.id!!)
                            }
                        })
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("cart-result","On Resume Called")
    }

    fun getCart(member_id:String){
        cartViewModel.getMyCartFun(member_id!!)
    }

    fun stopRefreshLoading(){
        swipeRefresh.isRefreshing = false
        loadingDialog.dismissLoading()
    }

    fun cartIsEmpty(customText:String?){
        recyclerView.visibility = View.GONE
        tvSubtotal.text = customText
        tvSubtotal.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tvSubtotal.setTextColor(Color.parseColor("#808080"))
        tvSubtotal.setTextSize(TypedValue.COMPLEX_UNIT_SP,30f)
        stopRefreshLoading()
    }

    fun resetTextViewPrice(){
        tvSubtotal.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
        tvSubtotal.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f)
        tvSubtotal.setTextColor(Color.parseColor("#F44336"))
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
        stopRefreshLoading()
        Log.d("cart-result","onDestroyView")
    }
}