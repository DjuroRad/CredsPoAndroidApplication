package com.example.credspoapp.ui.progress

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.credspoapp.models.ActivityHistoryResponse
import com.example.credspoapp.models.UserProgressResponse
import com.example.credspoapp.models.util.ApiInterface
import com.example.credspoapp.ui.activity.Type
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProgressViewModel: ViewModel() {
    val skills = MutableLiveData<UserProgressResponse?>(null)
    val attributes = MutableLiveData<UserProgressResponse?>(null)
    val waypoints = MutableLiveData<UserProgressResponse?>(null)

    fun getUserProgress(bearerToken: String?, progressType: Type) {
        val bearerTokenNew = "Bearer $bearerToken"
        var call: Call<UserProgressResponse>? = null
        when(progressType){
            Type.SKILLS -> call = ApiInterface.create().getUserSKills(bearerToken = bearerTokenNew)
            Type.ATTRIBUTES -> call = ApiInterface.create().getUserAttributes(bearerToken = bearerTokenNew)
            Type.WAYPOINTS -> call = ApiInterface.create().getUserWaypoints(bearerToken = bearerTokenNew)
        }

        call?.enqueue(object : Callback<UserProgressResponse> {
            override fun onResponse(call: Call<UserProgressResponse>, response: Response<UserProgressResponse>) {
                response?.let {
                    Log.e("RESPONSE PROGRESS", "IN RESPONSE")
                    if (response.isSuccessful) {
                        response.body().let {
                            when(progressType){
                                Type.SKILLS -> skills.postValue(it);
                                Type.ATTRIBUTES -> attributes.postValue(it);
                                Type.WAYPOINTS -> waypoints.postValue(it);
                            }
                            Log.e("PROGRESS", it.toString())
                        }
                    } else {
                        Log.e("PRGORESS", "FALSE")
                    }
                    if(response.code() == 422){
                        Log.e("PROGRESS", "422")
                    }
                }
            }

            override fun onFailure(call: Call<UserProgressResponse>, t: Throwable) {
                Log.e("PRGORESS", "FAILURE")
            }
        })
    }
}