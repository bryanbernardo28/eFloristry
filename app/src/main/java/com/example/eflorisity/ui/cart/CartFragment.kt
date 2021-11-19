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
import com.example.eflorisity.ui.home.HomeViewModel
import com.example.eflorisity.ui.home.data.ProductCartListNotIn
import com.example.eflorisity.ui.home.data.ProductCartNotIn
import com.google.gson.Gson


class CartFragment : Fragment() {

    private lateinit var cartViewModel: CartViewModel
    private lateinit var homeViewModel:HomeViewModel
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
    private var isLoggedIn:Boolean = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        swipeRefresh = binding.srlCartItemcartId
        tvSubtotal = binding.tvCartProductSubtotalPrice
        recyclerView = binding.rvCartProductId
        btnCheckout = binding.btnCartCheckoutId

        memberDetailsSp = SharedPref(requireContext(),getString(R.string.spMemberDetails))
        isLoggedIn = memberDetailsSp.getValueBoolean(getString(R.string.spKeyIsLoggedIn),false)
        val description = getString(R.string.label_loading)
        loadingDialog = LoginLoadingDialog(description,requireActivity())

        initRecyclerView()
        val member_id = memberDetailsSp.getValueString(getString(R.string.spKeyId))
        initViewModel(member_id)


        swipeRefresh.setOnRefreshListener {
            if(isLoggedIn){
                getCart(member_id!!)
            }
            else{
                getMyCartNotIn()
            }
        }

        btnCheckout.setOnClickListener {
            if (isLoggedIn){
                val goToCheckoutActivity = Intent(activity, CheckoutActivity::class.java)
                goToCheckoutActivity.putExtra("cartList",cartList)
                activity?.startActivityForResult(goToCheckoutActivity,getString(R.string.checkout_request_code).toInt())
            }
            else{
                val goToLoginActivity = Intent(requireContext(), LoginActivity::class.java)
                startActivity(goToLoginActivity)
                activity?.finish()
            }
        }
        if (isLoggedIn) {
            if (!loadingDialog.isShowing()){
                loadingDialog.startLoading()
                getCart(member_id.toString())
            }
        }
        else{
            getMyCartNotIn()
        }
        return root
    }


    fun getMyCartNotIn(){
        val spMyCart = SharedPref(requireContext(),getString(R.string.spMyCart))
        val spKeyMyCart = getString(R.string.spKeyCartStringSet)
        val productsInCart = spMyCart.getValueString(spKeyMyCart)
        if (!productsInCart.isNullOrEmpty()){
            val productInMyCartList = Gson().fromJson(productsInCart.toString(),ProductCartListNotIn::class.java)
            if (!loadingDialog.isShowing()){
                loadingDialog.startLoading()
                cartViewModel.getMyCartNotIn(productInMyCartList)
            }
        }
        else{
            cartIsEmpty("Cart is empty")
        }
    }

    class LongClickedItem(val member_id: String?,val productDetails:ProductDetails,val cartViewModel: CartViewModel,val loadingDialog:LoginLoadingDialog,val isLoggedIn:Boolean,var cartList:ArrayList<Cart>) : DialogFragment(){
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Choose Action")
                    .setItems(R.array.long_clicked_row_action,
                        DialogInterface.OnClickListener { _, which ->
                            if(which == 0){
                                if (!loadingDialog.isShowing()){
                                    loadingDialog.startLoading()
                                    if (isLoggedIn){
                                        cartViewModel.deleteItemFromCartFun(member_id!!,productDetails.id!!)
                                    }
                                    else{
                                        val spMyCart = SharedPref(requireContext(),getString(R.string.spMyCart))
                                        val spKeyMyCart = getString(R.string.spKeyCartStringSet)
                                        val productsInCart = spMyCart.getValueString(spKeyMyCart)
                                        val productInMyCartList = Gson().fromJson(productsInCart.toString(),ProductCartListNotIn::class.java)
                                        val index = productInMyCartList.carts.indexOfFirst { it2 ->
                                            it2.product_id == productDetails.id
                                        }
                                        productInMyCartList.carts.removeAt(index)
                                        spMyCart.save(spKeyMyCart,Gson().toJson(productInMyCartList).toString())
                                        cartViewModel.getMyCartNotIn(productInMyCartList)
                                        Log.d("cart-result","Index: $cartList")
                                    }
                                }
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
        btnCheckout.isEnabled = false
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

    fun initViewModel(member_id: String?){
        cartViewModel.getMyCartObservable().observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()){
                    Log.d("cart-result","CartFragment: ${it.toString()}")
                resetTextViewPrice()
                recyclerView.visibility = View.VISIBLE
                rvAdapter.CartAdapter(requireContext(),cartViewModel,homeViewModel,viewLifecycleOwner,it,loadingDialog,tvSubtotal,isLoggedIn)
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