package com.example.credspoapp.ui.registration

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.credspoapp.models.UserResponse
import com.example.credspoapp.models.RegisterUserbody
import com.example.credspoapp.models.RegisterVerificationBody
import com.example.credspoapp.models.util.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationViewModel: ViewModel() {

    val registrationStatus = MutableLiveData<Boolean?>(null)

    val validationStatus = MutableLiveData<Boolean?>(null)

    val userLiveData = MutableLiveData<UserResponse?>(null)
    //response for registration
    fun registerUser(registrationUserBody: RegisterUserbody){
        val call = ApiInterface.create().registerUser(registrationUserBody)
        call.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.e("registerUser API: ", response.toString())
                registrationStatus.postValue(true)
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("ERROR: ", t.toString())
                registrationStatus.postValue(false)
            }
        })
    }

    fun verifyUser(registerVerificationBody: RegisterVerificationBody){
        val call = ApiInterface.create().verifyUser(registerVerificationBody)
        call.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if(response.isSuccessful) {
                    Log.e("verifyUser API: ", response.toString())
                    validationStatus.postValue(true)
                    response.body()?.let{
                        userLiveData.postValue(it)
                    }
                } else {
                    validationStatus.postValue(false)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            }
        })
    }
}