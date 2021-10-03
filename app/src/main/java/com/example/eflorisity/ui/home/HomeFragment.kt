package com.example.eflorisity.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eflorisity.*
import com.example.eflorisity.databinding.FragmentHomeBinding
import com.example.eflorisity.recyclerview.GridSpacingItemDecoration
import com.example.eflorisity.ui.home.data.ProductDetails
import com.squareup.picasso.Picasso


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var rvAdapter: CustomAdapter
    private lateinit var recyclerView:RecyclerView

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
        var memberDetailsSp = SharedPref(requireContext(),getString(R.string.spMemberDetails))

        recyclerView = binding.rvHomeProductsId
        initRecyclerView()
        homeViewModel.getProductsObservable().observe(viewLifecycleOwner, Observer<List<ProductDetails>>{

            if (it != null){
                Log.d("product-result","HomeFragment: ${it.toString()}")
                rvAdapter.CustomAdapter(it)
                rvAdapter.notifyDataSetChanged()
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
        homeViewModel.getProducts("Bearer " + memberDetailsSp.getValueString(getString(R.string.spKeyToken)))


        return root
    }


    fun initRecyclerView() {
        val spanCount = 2 // 2 columns
        val spacing = 25 // 25px
        val includeEdge = true
        recyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge,0))
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(activity,2)
        rvAdapter = CustomAdapter()
        recyclerView.adapter = rvAdapter
    }


    class CustomAdapter : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
        private var productDataSet: List<ProductDetails>? = null

        fun CustomAdapter(productInfos:List<ProductDetails>){
            this.productDataSet = productInfos
        }

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val productName: TextView
            val productImage: ImageView
            val productPrice: TextView
            init {
                // Define click listener for the ViewHolder's View.
                productName = view.findViewById(R.id.tv_home_products_name_id)
                productImage = view.findViewById(R.id.iv_home_product_image_id)
                productPrice = view.findViewById(R.id.tv_home_product_price_id)
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.rv_home_products_row, viewGroup, false)
//            val lp = view.getLayoutParams()
//            lp.height = viewGroup.getMeasuredHeight() / 3
//            view.setLayoutParams(lp)
            return ViewHolder(view)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that
            val config = Config()
            val imageUrl = "${config.startUrl}${config.baseUrl}${productDataSet?.get(position)!!.photo_url}"
            Picasso.get().load(imageUrl).placeholder(R.drawable.no_image_available).resize(450, 450).into(viewHolder.productImage);
            viewHolder.productName.text = productDataSet?.get(position)!!.name
            viewHolder.productPrice.text = "${viewHolder.productPrice.text}${productDataSet?.get(position)!!.price}"
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount():Int = if (productDataSet.isNullOrEmpty()) 0 else productDataSet?.size!!

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}