package com.example.eflorisity.ui.account

import android.accounts.Account
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.eflorisity.HomeActivity
import com.example.eflorisity.R
import com.example.eflorisity.SQLiteDbHelper
import com.example.eflorisity.SharedPref
import com.example.eflorisity.databinding.FragmentAccountBinding
import com.example.eflorisity.login.LoginActivity

class AccountFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel
    private lateinit var sqliteDbHelper:SQLiteDbHelper
    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textNotifications
//        accountViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        sqliteDbHelper = SQLiteDbHelper(requireContext())

        val logoutButton:Button = binding.btnAccountLogoutId
        logoutButton.setOnClickListener {
            var memberDetailsSp = SharedPref(requireContext(),getString(R.string.spMemberDetails))
            memberDetailsSp.clearSharedPreference()
            sqliteDbHelper.truncateCart()
            val goToLoginActivity = Intent(activity, LoginActivity::class.java)
            startActivity(goToLoginActivity)
            activity?.finish()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}