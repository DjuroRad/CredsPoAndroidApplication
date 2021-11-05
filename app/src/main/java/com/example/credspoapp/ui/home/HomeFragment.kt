package com.example.credspoapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.credspoapp.databinding.FragmentHomeBinding
import com.example.credspoapp.models.User
import com.google.gson.Gson

class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get()= _binding!!

    private lateinit var user: User

    private var bearerToken: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        user = getUser(requireContext())
        bearerToken = getBearerToken(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var someText = "Hi ${user.firstName}"
        binding.hiNameTextView.text = someText

        super.onViewCreated(view, savedInstanceState)
    }

}

fun getUser(context: Context): User {
    val sp = context.getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
    val userJson = sp.getString("USER", "")
    val user = Gson().fromJson(userJson, User::class.java)
    return user
}

fun getBearerToken(context: Context): String? {

    val sp = context.getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
    val tokenUser = sp.getString("TOKEN", "")
//    val token = Gson().fromJson(tokenUser, String::class.java)
    return tokenUser
}