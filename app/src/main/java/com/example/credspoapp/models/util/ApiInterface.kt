package com.example.credspoapp.models.util

import com.example.credspoapp.models.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {
    companion object {
        var BASE_URL = "https://credspo-dev.amplitudo.me/"
        fun create(): ApiInterface {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor {
                    it.proceed(
                        it.request().newBuilder().addHeader("Accept", "application/json").build()
                    )
                }
                .build()

            val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }

    @POST("/api/auth/login")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<UserResponse>

    @POST("/api/auth/verify")
    fun verifyUser(@Body registerVerificationBody: RegisterVerificationBody): Call<UserResponse>

    @POST("/api/auth/logout")
    suspend fun logout(@Header("Authorization") bearerToken: String?): Response<LogoutResponse>

    @POST("/api/auth/register")
    fun registerUser(@Body registerUserBody: RegisterUserbody): Call<UserResponse>

    @GET("/api/activity-history")
    fun getActivityHistory(
    @Header("Authorization") bearerToken: String?,
    @Query("badge_type_id") badge_type_id: Int = 0,
    @Query("badge_id") badge_id: Int  = 0
        ): Call<ActivityHistoryResponse>

    @GET("/api/user-skills")
    fun getUserSKills(
        @Header("Authorization") bearerToken: String?,
    ): Call<UserProgressResponse>

    @GET("/api/user-attributes")
    fun getUserAttributes(
        @Header("Authorization") bearerToken: String?,
    ): Call<UserProgressResponse>

    @GET("/api/user-waypoints")
    fun getUserWaypoints(
        @Header("Authorization") bearerToken: String?,
    ): Call<UserProgressResponse>

    @DELETE("/api/delete-activity")
    suspend fun deleteActivty(
        @Header("Authorization") bearerToken: String?,
        @Query("id") id: Int
    ): Response<DeleteActivityResponse>

    @DELETE("/api/user")
    suspend fun deleteUser(
        @Header("Authorization") bearerToken: String?
    ): Response<DeleteAccountResponse>

    @POST("/api/auth/refresh")
    fun refreshToken(@Header("Authorization") bearerToken: String?): Call<UserResponse>
}

