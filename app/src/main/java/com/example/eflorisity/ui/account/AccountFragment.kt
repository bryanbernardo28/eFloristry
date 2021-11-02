package com.example.eflorisity.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.eflorisity.R
import com.example.eflorisity.SharedPref
import com.example.eflorisity.databinding.FragmentAccountBinding
import com.example.eflorisity.login.LoginActivity

class AccountFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null
    private lateinit var memberDetailsSp:SharedPref
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

        memberDetailsSp = SharedPref(requireContext(),getString(R.string.spMemberDetails))
        val isLoggedIn = memberDetailsSp.getValueBoolean(getString(R.string.spKeyIsLoggedIn),false)

//        if (!isLoggedIn){
//            val goToLoginActivity = Intent(activity, LoginActivity::class.java)
//            startActivity(goToLoginActivity)
//            activity?.finish()
//        }


        val logoutButton:Button = binding.btnAccountLogoutId
        logoutButton.setOnClickListener {
            memberDetailsSp.clearSharedPreference()
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