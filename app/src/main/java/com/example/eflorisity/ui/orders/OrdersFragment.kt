package com.example.eflorisity.ui.orders


import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eflorisity.R
import com.example.eflorisity.SharedPref
import com.example.eflorisity.databinding.OrdersFragmentBinding
import com.example.eflorisity.ui.orders.recyclerview_adapter.OrdersAdapter
import com.example.eflorisity.ui.orders.track.TrackOrdersActivity

class OrdersFragment : Fragment() {

    private lateinit var ordersViewModel: OrdersViewModel
    private lateinit var btnToPay:Button
    private lateinit var btnToShip:Button
    private lateinit var btnToReceive:Button
    private lateinit var btnToReview:Button
    private lateinit var rvAdapter: OrdersAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var memberDetailsSp:SharedPref
    private lateinit var tvNoOrder:TextView
    private lateinit var srlOrder:SwipeRefreshLayout



    private var _binding: OrdersFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ordersViewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)
        _binding = OrdersFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root



        btnToPay = binding.btnOrdersTopayId
        btnToShip = binding.btnOrdersToshipId
        btnToReceive = binding.btnOrdersToreceiveId
        btnToReview = binding.btnOrdersToreviewId
        tvNoOrder = binding.tvOrdersNoorderId
        memberDetailsSp = SharedPref(requireActivity(),getString(R.string.spMemberDetails))
        recyclerView = binding.rvOrdersOrderlistId
        srlOrder = binding.srlOrderId

        initRecyclerView()
        initViewModel()

        val trackIntent = Intent(requireContext(),TrackOrdersActivity::class.java)
        btnToPay.setOnClickListener {
            trackIntentStart(trackIntent,0)
        }

        btnToShip.setOnClickListener {
            trackIntentStart(trackIntent,1)
        }

        btnToReceive.setOnClickListener {
            trackIntentStart(trackIntent,2)
        }

        btnToReview.setOnClickListener {
            trackIntentStart(trackIntent,3)
        }

        val member_id = memberDetailsSp.getValueString(getString(R.string.spKeyId))
        ordersViewModel.getOrders(member_id!!,0)
        srlOrder.setOnRefreshListener {
            ordersViewModel.getOrders(member_id!!,0)
        }
        return root
    }


    private fun initViewModel() {
        ordersViewModel.getOrdersWithProductObservable().observe(viewLifecycleOwner,{
            if (!it.isNullOrEmpty()){
                Log.d("orders-result",it.toString())
                rvAdapter.ordersAdapter(requireContext(),it)
                rvAdapter.notifyDataSetChanged()

            }
            else{
                recyclerView.visibility = View.GONE
                tvNoOrder.visibility = View.VISIBLE
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

    fun trackIntentStart(trackIntent:Intent,value:Int){
        trackIntent.putExtra("toTrack",value)
        startActivity(trackIntent)
    }

    override fun onResume() {
        super.onResume()
        Log.d("orders-result","Orders Fragment")
    }
}