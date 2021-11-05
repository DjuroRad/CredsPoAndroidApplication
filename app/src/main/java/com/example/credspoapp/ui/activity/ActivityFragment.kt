package com.example.credspoapp.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.credspoapp.R
import com.example.credspoapp.databinding.FragmentActivityBinding
import com.example.credspoapp.models.ActivityCredsPo
import com.example.credspoapp.models.ActivityHistoryResponse
import com.example.credspoapp.models.UserProgressResponse
import com.example.credspoapp.ui.home.getBearerToken
import com.example.credspoapp.ui.profile.CameraPromptDialogFragment
import com.example.credspoapp.ui.progress.ProgressAdapter
import com.example.credspoapp.ui.progress.ProgressViewModel


class ActivityFragment: Fragment(), ActivityHistoryAdapter.OnDeleteActivityListener {
    private var _binding: FragmentActivityBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ActivityViewModel
    private var bearerToken: String? = ""
    private lateinit var buttonList: MutableList<TextView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActivityBinding.inflate(inflater, container, false)

        bearerToken = getBearerToken(requireContext())
        viewModel = ViewModelProvider(this).get(ActivityViewModel::class.java)
        initializeButtonList()
        //Prepare all the data that would just change on click at the menu
        //This is done before the view is created
        viewModel.getActivityHistory(bearerToken, Type.ALL.value)
        viewModel.getActivityHistory(bearerToken, Type.SKILLS.value)
        viewModel.getActivityHistory(bearerToken, Type.ATTRIBUTES.value)
        viewModel.getActivityHistory(bearerToken, Type.WAYPOINTS.value)

        setButtonOnClickListeners(context = requireContext(), buttonList = buttonList, owner = viewLifecycleOwner, recyclerView = binding.activityRecyclerView, viewModel = viewModel,
            deleteListener = this
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.activityRecyclerView.layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //At first ALL is selected, so we make it our first choice here
        viewModel?.let {
            populateTheRecycler(owner = viewLifecycleOwner, it.activityHistoryAllLiveData, null, requireContext(), recyclerView = binding.activityRecyclerView, this)
        }
    }

    private fun initializeButtonList(){
        buttonList = mutableListOf()
        buttonList.add(Type.ALL.value, binding.allSelectorTextView)
        buttonList.add(Type.SKILLS.value, binding.skillsSelectorTextView)
        buttonList.add(Type.ATTRIBUTES.value, binding.attributesSelectorTextView)
        buttonList.add(Type.WAYPOINTS.value, binding.waypointsSelectorTextView)
    }

    private fun showDialogFragment(onNoClick: View.OnClickListener?, onYesClick: View.OnClickListener?){
        val cameraDialogFragment = CameraPromptDialogFragment.newInstance( onYesClick, onNoClick, "Delete activity", "Do you want to delete activity?", "Yes", "No")
        cameraDialogFragment.show(childFragmentManager, "camera_dialog_fragment")
    }


    override fun onDeleteActivity(activityItem: ActivityCredsPo) {
        val onNoClickListener = null
        val onYesClickListener = View.OnClickListener {
            viewModel.deleteActivity(bearerToken = bearerToken,activityItem = activityItem)
        }
        showDialogFragment(onNoClickListener, onYesClickListener)

    }

}

fun setButtonOnClickListeners(context: Context, buttonList: List<TextView>, viewModel: ViewModel, owner: LifecycleOwner, recyclerView: RecyclerView, deleteListener: ActivityHistoryAdapter.OnDeleteActivityListener?){

    val unselectedConstantState = ContextCompat.getDrawable(context, R.drawable.activity_unselected)?.constantState

    for( button_id in 0..buttonList.size-1 ){
        buttonList[button_id].setOnClickListener{
            if( it.background.constantState == unselectedConstantState )
                selectButton(button_id, buttonList[button_id], context, buttonList, viewModel, owner, recyclerView = recyclerView, deleteListener = deleteListener)
        }
    }
}

fun selectButton(button_id: Int, viewButton: View, context: Context, buttonList: List<TextView>, viewModel: ViewModel, owner: LifecycleOwner, recyclerView: RecyclerView, deleteListener: ActivityHistoryAdapter.OnDeleteActivityListener?){
    //When button is selected 2 things are performed
    //1 - change the style of the menu
    //2 - change the recyclerView's adapter

    //1
    val unselected_drawable  = ContextCompat.getDrawable(context, R.drawable.activity_unselected)
    for( button in buttonList ) {
        button.background = unselected_drawable
        button.setTextColor(context.resources.getColor(R.color.default_font_color))
    }
    viewButton.background = ContextCompat.getDrawable(context, R.drawable.activity_selected)
    (viewButton as TextView).setTextColor(context.resources.getColor(R.color.white))

    //2
    //these should be initialized before the view is created so the observers in
    //the recycler view might not be necessary since it is not possible for the to be null at this moment
    //it is possible only if there was a mistake at the server side
    var viewModelActivity:ActivityViewModel? = null
    var viewModelProgess:ProgressViewModel? = null

    //only one of these two will be used
    //only one of these two will have value different from null

    if( buttonList.size == 4 )
        viewModelActivity = (viewModel as ActivityViewModel)
    else if( buttonList.size == 3)
        viewModelProgess = viewModel as ProgressViewModel

    viewModelActivity?.let{
        when (button_id) {
            Type.ALL.value -> populateTheRecycler(owner = owner, it.activityHistoryAllLiveData, null, context, recyclerView = recyclerView, deleteListener)
            Type.SKILLS.value -> populateTheRecycler(owner = owner, it.activityHistorySkillsLiveData, null, context, recyclerView = recyclerView, deleteListener)
            Type.ATTRIBUTES.value -> populateTheRecycler(owner = owner, it.activityHistoryAttributesLiveData, null, context, recyclerView = recyclerView,deleteListener)
            Type.WAYPOINTS.value -> populateTheRecycler(owner = owner, it.activityHistoryWaypointsLiveData, null, context, recyclerView = recyclerView, deleteListener)
        }
    }
    viewModelProgess?.let{
        when (button_id) {
            //-1 because there are 3 elements in buttonList for Progress so this way I can start from skills
            Type.SKILLS.value-1 -> populateTheRecycler(owner = owner, null, it.skills, context, recyclerView = recyclerView, null)
            Type.ATTRIBUTES.value-1 -> populateTheRecycler(owner = owner, null, it.attributes, context, recyclerView = recyclerView, null)
            Type.WAYPOINTS.value-1 -> populateTheRecycler(owner = owner, null, it.waypoints, context, recyclerView = recyclerView, null)
        }
    }

}

fun populateTheRecycler(owner: LifecycleOwner, responseActivityHistoryResponseLiveData: MutableLiveData<ActivityHistoryResponse?>?, responseProgressResponseLiveData: MutableLiveData<UserProgressResponse?>?, context: Context, recyclerView: RecyclerView, deleteListener: ActivityHistoryAdapter.OnDeleteActivityListener?){

    responseActivityHistoryResponseLiveData?.let {  liveData ->
        liveData.observe(owner, { activities_response ->
            activities_response?.let {
                val adapter = ActivityHistoryAdapter( context, it.dataActivity.activities, deleteListener)
                recyclerView.adapter = adapter
                Log.e("RECYCLER HISTORY", it.toString())
            }
        })
    }

    responseProgressResponseLiveData?.let {
        it.observe(owner, { activities_response ->
            activities_response?.let {
                val adapter = ProgressAdapter( context, it.data)
                recyclerView.adapter = adapter
            }
        })
    }
}
//ALL is put list in this enum
//so that it would be avoided when called from ProgressActivity as it does not have ALL option
enum class Type(val value:Int) {
    ALL(0), SKILLS(1), ATTRIBUTES(2), WAYPOINTS(3)
}