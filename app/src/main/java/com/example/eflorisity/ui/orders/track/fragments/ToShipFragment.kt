package com.example.eflorisity.ui.orders.track.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eflorisity.R
import com.example.eflorisity.SharedPref
import com.example.eflorisity.ui.orders.OrdersViewModel
import com.example.eflorisity.ui.orders.recyclerview_adapter.OrdersAdapter

class ToShipFragment : Fragment() {
    private lateinit var ordersViewModel: OrdersViewModel
    private lateinit var rvAdapter: OrdersAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var memberDetailsSp: SharedPref
    private lateinit var tvNoOrder: TextView
    private lateinit var srlOrder: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_to_ship, container, false)

        ordersViewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)
        tvNoOrder = root.findViewById(R.id.tv_orders_noorder_id)
        memberDetailsSp = SharedPref(requireActivity(),getString(R.string.spMemberDetails))
        recyclerView = root.findViewById(R.id.rv_orders_orderlist_id)
        srlOrder = root.findViewById(R.id.srl_toship_order_id)

        initRecyclerView()
        initViewModel()

        getData()
        srlOrder.setOnRefreshListener {
            getData()
        }

        return root
    }

    fun getData(){
        val memberId = memberDetailsSp.getValueString(getString(R.string.spKeyId))
        ordersViewModel.getOrders(memberId!!,2)
    }

    private fun checkIfHasData(hasData:Boolean){
        if(hasData){
            tvNoOrder.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        else{
            recyclerView.visibility = View.GONE
            tvNoOrder.visibility = View.VISIBLE
        }
    }

    private fun initViewModel() {
        ordersViewModel.getOrdersWithProductObservable().observe(viewLifecycleOwner,{
            if (!it.isNullOrEmpty()){
                Log.d("orders-result","To Ship: $it")
                rvAdapter.ordersAdapter(requireContext(),it)
                rvAdapter.notifyDataSetChanged()
                checkIfHasData(true)
            }
            else{
                checkIfHasData(false)
            }
            srlOrder.isRefreshing = false
        })
    }

    fun initRecyclerView() {
        rvAdapter = OrdersAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(
            DividerItemDecoration(activity,
                DividerItemDecoration.VERTICAL)
        )
        recyclerView.adapter = rvAdapter
    }
}