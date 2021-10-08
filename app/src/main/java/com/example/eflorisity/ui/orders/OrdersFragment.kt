package com.example.eflorisity.ui.orders

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eflorisity.R
import com.example.eflorisity.databinding.OrdersFragmentBinding

class OrdersFragment : Fragment() {

    private lateinit var viewModel: OrdersViewModel

    private var _binding: OrdersFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)

        _binding = OrdersFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

}