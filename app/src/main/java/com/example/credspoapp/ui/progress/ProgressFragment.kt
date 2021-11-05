package com.example.credspoapp.ui.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.credspoapp.databinding.FragmentProgressBinding
import com.example.credspoapp.ui.activity.Type
import com.example.credspoapp.ui.activity.populateTheRecycler
import com.example.credspoapp.ui.activity.setButtonOnClickListeners
import com.example.credspoapp.ui.home.getBearerToken

class ProgressFragment: Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    private var bearerToken: String? = ""
    private lateinit var viewModel: ProgressViewModel
    private lateinit var buttonList: MutableList<TextView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)

        bearerToken = getBearerToken(requireContext())

        //initialize data in viewModel so that it can be observed in button listeners
        viewModel = ViewModelProvider(this).get(ProgressViewModel::class.java)
        viewModel.getUserProgress(bearerToken, Type.SKILLS)
        viewModel.getUserProgress(bearerToken, Type.ATTRIBUTES)
        viewModel.getUserProgress(bearerToken, Type.WAYPOINTS)

        //initialize buttons and set their listeners
        initializeButtonList()
        setButtonOnClickListeners(context = requireContext(), buttonList = buttonList, owner = viewLifecycleOwner, recyclerView = binding.progressRecyclerView, viewModel = viewModel, deleteListener = null)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layout:GridLayoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

        binding.progressRecyclerView.layoutManager =  layout

        //At first ALL is selected, so we make it our first choice here
        viewModel.skills?.let{
            populateTheRecycler(owner = viewLifecycleOwner, null, it, requireContext(), recyclerView = binding.progressRecyclerView, deleteListener = null)
        }
    }

    private fun initializeButtonList(){
        buttonList = mutableListOf()
        buttonList.add(Type.SKILLS.value-1, binding.skillsProgressSelectorTextView)
        buttonList.add(Type.ATTRIBUTES.value-1, binding.attributesProgressSelectorTextView)
        buttonList.add(Type.WAYPOINTS.value-1, binding.waypointsProgressSelectorTextView)
    }
}