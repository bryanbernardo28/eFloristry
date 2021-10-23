package com.example.eflorisity.ui.cart

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eflorisity.MainActivity
import com.example.eflorisity.R
import com.example.eflorisity.SharedPref
import com.example.eflorisity.databinding.FragmentCartBinding
import com.example.eflorisity.login.LoginActivity
import com.example.eflorisity.login.LoginLoadingDialog
import com.example.eflorisity.ui.cart.data.Cart
import com.example.eflorisity.ui.cart.gesture.Swipe
import com.example.eflorisity.ui.checkout.CheckoutActivity
import com.example.eflorisity.ui.home.data.ProductDetails

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

        memberDetailsSp = SharedPref(requireContext(),getString(R.string.spMemberDetails))
        val isLoggedIn = memberDetailsSp.getValueBoolean(getString(R.string.spKeyIsLoggedIn),false)
        if (!isLoggedIn) {
            val goToLoginActivity = Intent(activity, LoginActivity::class.java)
            startActivity(goToLoginActivity)
            activity?.finish()
        }
        else{
            tvSubtotal = binding.tvCartProductSubtotalPrice
            recyclerView = binding.rvCartProductId
            btnCheckout = binding.btnCartCheckoutId

            val member_id = memberDetailsSp.getValueString(getString(R.string.spKeyId))
            val description = getString(R.string.label_loading)
            loadingDialog = LoginLoadingDialog(description,requireActivity())
            loadingDialog.startLoading()
            initRecyclerView()


            cartViewModel.getMyCartObservable().observe(viewLifecycleOwner, Observer {
                if (!it.isNullOrEmpty()){
                    Log.d("cart-result","CartFragment: ${it.toString()}")
                    rvAdapter.CartAdapter(requireContext(),cartViewModel,it,loadingDialog)
                    rvAdapter.notifyDataSetChanged()
                    btnCheckout.isEnabled = true
                    cartList = it
                }
                else{
                    Log.d("cart-result","CartFragment: Else ${it.toString()}")
                    recyclerView.visibility = View.GONE
                    tvSubtotal.text = "Cart is empty"
                    tvSubtotal.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    tvSubtotal.setTextColor(Color.BLACK)
                    tvSubtotal.setTextSize(TypedValue.COMPLEX_UNIT_SP,30f)
                }
                loadingDialog.dismissLoading()
            })

            cartViewModel.getMyCartFun(member_id!!)

            cartViewModel.getDeleteFromCartResponseObservable().observe(viewLifecycleOwner, Observer {
                if (it.success){
                    Log.d("cart-result","CartFragment: ${it.toString()}")
                    cartViewModel.getMyCartFun(member_id!!)
                }
                else{
                    Log.d("cart-result","CartFragment: Else ${it.toString()}")
                }
                loadingDialog.dismissLoading()
            })

//        val swipeGesture = object :Swipe(requireContext()){
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                super.onSwiped(viewHolder, direction)
//                when(direction){
//                    ItemTouchHelper.LEFT -> {
//                        rvAdapter.swipeLeftGetProductId(viewHolder.adapterPosition)
//                    }
//                }
//            }
//        }

            btnCheckout.setOnClickListener {
                val goToCheckoutActivity = Intent(requireContext(), CheckoutActivity::class.java)
                goToCheckoutActivity.putExtra("cartList",cartList)
                startActivity(goToCheckoutActivity)
            }
        }

        return root
    }

    class LongClickedItem(val product:ProductDetails,val cartViewModel: CartViewModel,val loadingDialog:LoginLoadingDialog) : DialogFragment(){
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Choose Action")
                    .setItems(R.array.long_clicked_row_action,
                        DialogInterface.OnClickListener { dialog, which ->
                            if(which == 0){
                                loadingDialog.startLoading()
                                cartViewModel.deleteItemFromCartFun(product.id!!)
                            }
                        })
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
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