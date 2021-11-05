package com.example.credspoapp.ui.activity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credspoapp.models.ActivityCredsPo
import com.example.credspoapp.models.ActivityHistoryResponse
import com.example.credspoapp.models.util.ApiInterface
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityViewModel:ViewModel() {
    //activities add etc

    val activityHistoryAllLiveData = MutableLiveData<ActivityHistoryResponse?>(null)
    val activityHistorySkillsLiveData = MutableLiveData<ActivityHistoryResponse?>(null)
    val activityHistoryAttributesLiveData = MutableLiveData<ActivityHistoryResponse?>(null)
    val activityHistoryWaypointsLiveData = MutableLiveData<ActivityHistoryResponse?>(null)

    fun getActivityHistory(bearerToken: String?, badgeTypeId: Int) {
        val bearerTokenNew = "Bearer $bearerToken"
        val call = ApiInterface.create().getActivityHistory(bearerToken = bearerTokenNew, badge_type_id = badgeTypeId)
        call.enqueue(object : Callback<ActivityHistoryResponse> {
            override fun onResponse(call: Call<ActivityHistoryResponse>, response: Response<ActivityHistoryResponse>) {
                response?.let {
                    Log.e("RESPONSE HISTORY", "IN RESPONSE")
                    if (response.isSuccessful) {
                        response.body().let {
                            when( badgeTypeId ) {
                                Type.ALL.value -> activityHistoryAllLiveData.postValue(it)
                                Type.SKILLS.value -> activityHistorySkillsLiveData.postValue(it)
                                Type.ATTRIBUTES.value -> activityHistoryAttributesLiveData.postValue(it)
                                Type.WAYPOINTS.value -> activityHistoryWaypointsLiveData.postValue(it)
                            }
                            Log.e("ACTIVITY_HISTORY", it.toString())
                        }
                    } else {
                        Log.e("ACTIVITY_HISTORY", "FALSE")
                    }
                    if(response.code() == 422){
                        Log.e("ACTIVITY_HISTORY", "422")
                    }
                }
            }

            override fun onFailure(call: Call<ActivityHistoryResponse>, t: Throwable) {
                Log.e("ACTIVITY_HISTORY", "in onFailure")
            }
        })
    }

    fun deleteActivity(bearerToken: String?, activityItem: ActivityCredsPo) {
        val bearerTokenNew = "Bearer $bearerToken"
        viewModelScope.launch {
            val response = ApiInterface.create()
                .deleteActivty(bearerToken = bearerTokenNew, id = activityItem.id)
            response?.let {
                when (response.isSuccessful) {
                    true -> removeFromLists(activityItem)
                    false -> Log.e(
                        "DELETE ACT RESPONSE",
                        "Delete activity response was complement to succesfful"
                    )
                }
            }
        }
    }

    private fun removeFromLists( activityItem: ActivityCredsPo ){
        removeItemAndUpdateLiveData(activityItem, activityHistoryAllLiveData)
        removeItemAndUpdateLiveData(activityItem, activityHistorySkillsLiveData)
        removeItemAndUpdateLiveData(activityItem, activityHistoryAttributesLiveData)
        removeItemAndUpdateLiveData(activityItem, activityHistoryWaypointsLiveData)
    }

    private fun removeItemAndUpdateLiveData(activityItem: ActivityCredsPo, liveData: MutableLiveData<ActivityHistoryResponse?>){
        val newResponse = liveData.value
        newResponse?.let {
            it.dataActivity.activities.remove(activityItem)
            liveData.postValue(it)
        }
    }
}