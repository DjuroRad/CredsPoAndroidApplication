package com.example.credspoapp.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.credspoapp.R
import com.example.credspoapp.databinding.FragmentSplashScreenBinding
import com.example.credspoapp.ui.home.getBearerToken

class SplashScreenFragment : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
        val bearerToken = sharedPreferences.getString("TOKEN", "")
        viewModel.refreshToken(bearerToken ?: "")

        viewModel.refreshTokenUserLiveData.observe(viewLifecycleOwner, {
            it?.let {
                saveToSharedPreferences(requireContext(), it)
                findNavController().navigate(R.id.action_splashScreenFragment_to_mainMenuFragment)
            }
        })
        viewModel.refreshTokenStatus.observe(viewLifecycleOwner, {
            it?.let {
                if(!it){
                        findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
                }
            }
        })
    }

}