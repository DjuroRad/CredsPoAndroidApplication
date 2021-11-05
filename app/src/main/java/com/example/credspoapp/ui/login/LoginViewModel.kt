package com.example.credspoapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.credspoapp.models.UserResponse
import com.example.credspoapp.models.util.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {

    val loginStatus = MutableLiveData<Boolean?>(null)
    val userResponseLiveData = MutableLiveData<UserResponse>(null)
    val verificationStatus = MutableLiveData<Boolean>(null)

    val refreshTokenUserLiveData = MutableLiveData<UserResponse?>(null)
    val refreshTokenStatus = MutableLiveData<Boolean?>(null)

    fun bearerToken(email: String, password: String) {
        val call = ApiInterface.create().login(email = email, password = password)
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                response?.let {
                    if (response.isSuccessful) {
                        response.body().let {
                            loginStatus.postValue(true);
                            userResponseLiveData.postValue(it)
                        }
                    } else {
                        loginStatus.postValue(false);
                    }
                    if(response.code() == 422){
                        verificationStatus.postValue(true)
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                loginStatus.postValue(false)
            }
        })
    }

    fun refreshToken(bearerToken: String) {
        val call = ApiInterface.create().refreshToken("Bearer $bearerToken")
        call.enqueue(object: Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if(response.isSuccessful){
                    response.body()?.let{
                        refreshTokenUserLiveData.postValue(it)
                        refreshTokenStatus.postValue(true)
                    }
                } else {
                    refreshTokenUserLiveData.postValue(null)
                    refreshTokenStatus.postValue(false)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                refreshTokenUserLiveData.postValue(null)
                refreshTokenStatus.postValue(false)
            }

        })
    }
}