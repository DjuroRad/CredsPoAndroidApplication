package com.example.credspoapp.ui.profile


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credspoapp.models.ActivityHistoryResponse
import com.example.credspoapp.models.DeleteAccountResponse
import com.example.credspoapp.models.UserProgressResponse
import com.example.credspoapp.models.util.ApiInterface
import com.example.credspoapp.ui.activity.Type
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel: ViewModel() {

    val logoutStatus = MutableLiveData<Boolean?>(null)
    val deleteAccountStatus = MutableLiveData<Boolean?>(null)

    fun deleteAccount(bearerToken: String?) {
        viewModelScope.launch {
            val response = ApiInterface.create().deleteUser("Bearer $bearerToken")
            if (response.isSuccessful) {
                deleteAccountStatus.postValue(true)
            } else {
                deleteAccountStatus.postValue(false)
            }
        }
    }


    fun logout(bearerToken: String?) {
        viewModelScope.launch {
            val response = ApiInterface.create().logout("Bearer $bearerToken")
            if (response.isSuccessful) {
                logoutStatus.postValue(true)
            } else {
                logoutStatus.postValue(false)
            }
        }
    }

}