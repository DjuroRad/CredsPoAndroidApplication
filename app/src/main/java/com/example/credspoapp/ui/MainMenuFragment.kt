package com.example.credspoapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.credspoapp.R
import com.example.credspoapp.databinding.FragmentMainMenuBinding
import com.example.credspoapp.models.User
import com.example.credspoapp.ui.activity.ActivityFragment
import com.example.credspoapp.ui.home.HomeFragment
import com.example.credspoapp.ui.profile.ProfileFragment
import com.example.credspoapp.ui.progress.ProgressFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_main_menu.*

class MainMenuFragment: Fragment() {
    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeFragment: HomeFragment
    private lateinit var activityFragment: ActivityFragment
    private lateinit var progressFragment: ProgressFragment
    private lateinit var profileFragment: ProfileFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)

        homeFragment = HomeFragment()
        activityFragment = ActivityFragment()
        progressFragment = ProgressFragment()
        profileFragment = ProfileFragment()

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeFragment -> replaceFragment(homeFragment)
                R.id.activityFragment -> replaceFragment(activityFragment)
                R.id.progressFragment -> replaceFragment(progressFragment)
                R.id.profileFragment -> replaceFragment(profileFragment)
            }
            true
        }
        //making the first fragment HOME at the beginning
        replaceFragment(homeFragment)
        return binding.root
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.let {
            it.replace( R.id.fragmentContainer, fragment ).commit()
        }
    }
}