package com.example.eflorisity.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eflorisity.*
import com.example.eflorisity.databinding.FragmentHomeBinding
import com.example.eflorisity.login.LoginLoadingDialog
import com.example.eflorisity.recyclerview.GridSpacingItemDecoration
import com.example.eflorisity.ui.home.data.ProductDetails
import com.squareup.picasso.Picasso


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var rvAdapter: ItemsAdapter
    private lateinit var recyclerView:RecyclerView
    private lateinit var loadingDialog : LoginLoadingDialog
    lateinit var memberDetailsSp:SharedPref
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        memberDetailsSp = SharedPref(requireContext(),getString(R.string.spMemberDetails))


        recyclerView = binding.rvHomeProductsId
        initRecyclerView()
        homeViewModel.getProductsObservable().observe(viewLifecycleOwner, Observer<List<ProductDetails>>{
            if (it != null){
                Log.d("product-result","HomeFragment: ${it.toString()}")
                rvAdapter.ItemsAdapter(requireContext(),it)
                rvAdapter.notifyDataSetChanged()
                loadingDialog.dismissLoading()
            }
            else{
                Log.d("product-result","HomeFragment: ${it.toString()}")
            }
        })

        homeViewModel.getErrorProductsObservable().observe(viewLifecycleOwner,{
            if(it != null){
                if(it == 401){
                    memberDetailsSp.clearSharedPreference()
                    val goToHomeActivity = Intent(requireContext(), MainActivity::class.java)
                    activity?.startActivity(goToHomeActivity)
                    activity?.finish()
                }
            }
        })
//        homeViewModel.getProducts("Bearer " + memberDetailsSp.getValueString(getString(R.string.spKeyToken)))


        return root
    }

    override fun onResume() {
        super.onResume()
        val description = getString(R.string.label_loading)
        loadingDialog = LoginLoadingDialog(description,requireActivity())
        loadingDialog.startLoading()
        homeViewModel.getProducts("Bearer " + memberDetailsSp.getValueString(getString(R.string.spKeyToken)))
    }


    fun initRecyclerView() {
        val spanCount = 2 // 2 columns
        val spacing = 25 // 25px
        val includeEdge = true
        recyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge,0))
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(activity,2)
        rvAdapter = ItemsAdapter()
        recyclerView.adapter = rvAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}